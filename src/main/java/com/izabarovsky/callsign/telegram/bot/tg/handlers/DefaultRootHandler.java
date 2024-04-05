package com.izabarovsky.callsign.telegram.bot.tg.handlers;

import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import com.izabarovsky.callsign.telegram.bot.tg.Command;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogState;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.actions.*;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions.*;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.validator.OfficialCallSignValidator;
import com.izabarovsky.callsign.telegram.bot.utils.CsvUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions.CmdConditionsFactory.cmdCondition;
import static com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions.DialogConditionsFactory.dialogCondition;
import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.*;

@Slf4j
@Component
public class DefaultRootHandler implements Handler<Update, HandlerResult>, RootHandler<Update, HandlerResult> {
    private final DialogStateService dialogService;
    private final CallSignService callSignService;
    private final OfficialCallSignValidator officialCallSignValidator;
    private final CsvUtil csvUtil;
    private final Handler<Update, HandlerResult> dummyHandler = s -> null;

    public DefaultRootHandler(DialogStateService dialogService,
                              CallSignService callSignService,
                              OfficialCallSignValidator officialCallSignValidator,
                              CsvUtil csvUtil
    ) {
        this.dialogService = dialogService;
        this.callSignService = callSignService;
        this.officialCallSignValidator = officialCallSignValidator;
        this.csvUtil = csvUtil;
    }

    public HandlerResult handle(Update update) {
        log.info("{}", update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            return rootHandler().handle(update);
        }
        return null;
    }

    private Handler<Update, HandlerResult> rootHandler() {
        Condition<Update> isK2Info = cmdCondition(Command.MY_K2_INFO);
        Condition<Update> isStatistics = cmdCondition(Command.STATISTICS);
        Condition<Update> isCommand = new IsCommand();
        Condition<Update> isPersonalChat = new IsPersonalChat();
        Handler<Update, HandlerResult> k2InfoAction = new K2InfoAction(callSignService);
        Handler<Update, HandlerResult> k2StatisticsAction = new K2StatisticsAction(callSignService);

        var commandNode = BranchHandler.builder()
                .condition(isCommand)
                .branchTrue(commandBranch())
                .branchFalse(textBranch())
                .build();

        var groupChatCommandChain = new ChainHandler(dummyHandler)
                .setHandler(isK2Info, k2InfoAction)
                .setHandler(isStatistics, k2StatisticsAction);

        return BranchHandler.builder()
                .condition(isPersonalChat)
                .branchTrue(commandNode)
                .branchFalse(groupChatCommandChain)
                .build();
    }

    private BranchHandler commandBranch() {
        Condition<Update> isSession = new IsSession(dialogService);

        return BranchHandler.builder()
                .condition(isSession)
                .branchTrue(commandInSessionBranch())
                .branchFalse(commandOutSessionBranch())
                .build();
    }

    private BranchHandler commandInSessionBranch() {
        Condition<Update> isExistsUser = new IsExistsUser(callSignService);
        Condition<Update> isSkip = cmdCondition(Command.SKIP);
        Condition<Update> isCancel = cmdCondition(Command.CANCEL);
        Condition<Update> isRequiredForNewcomer = new IsRequiredForNewcomer(dialogService);

        Handler<Update, HandlerResult> nextStateAction = new NextStateAction(dialogService);
        Handler<Update, HandlerResult> cleanStateAction = new CleanStateAction(dialogService);

        var existsUser = BranchHandler.builder()
                .condition(isSkip)
                .branchTrue(nextStateAction)
                .branchFalse(s -> msgOnAnyUnknown(s.getMessage().getChatId()))
                .build();

        var newcomerSkipNode = BranchHandler.builder()
                .condition(isRequiredForNewcomer)
                .branchTrue(s -> msgCantSkip(s.getMessage().getChatId()))
                .branchFalse(s -> msgOnAnyUnknown(s.getMessage().getChatId()))
                .build();

        var newcomerUser = BranchHandler.builder()
                .condition(isSkip)
                .branchTrue(newcomerSkipNode)
                .branchFalse(s -> msgOnAnyUnknown(s.getMessage().getChatId()))
                .build();

        var existsUserNode = BranchHandler.builder()
                .condition(isExistsUser)
                .branchTrue(existsUser)
                .branchFalse(newcomerUser)
                .build();

        return BranchHandler.builder()
                .condition(isCancel)
                .branchTrue(cleanStateAction)
                .branchFalse(existsUserNode)
                .build();
    }

    private BranchHandler commandOutSessionBranch() {
        Condition<Update> isExistsUser = new IsExistsUser(callSignService);
        Condition<Update> isCreate = cmdCondition(Command.CREATE);
        Condition<Update> isEdit = cmdCondition(Command.EDIT);
        Condition<Update> isK2Info = cmdCondition(Command.MY_K2_INFO);
        Condition<Update> isGetAll = cmdCondition(Command.GET_ALL);
        Condition<Update> isSearch = cmdCondition(Command.SEARCH);
        Condition<Update> isStatistics = cmdCondition(Command.STATISTICS);

        var newcomerUser = BranchHandler.builder()
                .condition(isCreate)
                .branchTrue(new StartDialogCreateAction(dialogService))
                .branchFalse(s -> msgOnNewcomer(s.getMessage().getChatId()))
                .build();

        var existsUserChain = new ChainHandler(s -> msgOnAnyUnknown(s.getMessage().getChatId()))
                .setHandler(isEdit, new StartDialogEditAction(dialogService))
                .setHandler(isK2Info, new K2InfoAction(callSignService))
                .setHandler(isSearch, new StartDialogSearchAction(dialogService))
                .setHandler(isGetAll, new GatAllCallSignsAction(callSignService, csvUtil))
                .setHandler(isStatistics, new K2StatisticsAction(callSignService));

        return BranchHandler.builder()
                .condition(isExistsUser)
                .branchTrue(existsUserChain)
                .branchFalse(newcomerUser)
                .build();
    }

    private BranchHandler textBranch() {
        Condition<Update> isSession = new IsSession(dialogService);
        Condition<Update> isWaitForK2CallSign = dialogCondition(dialogService, DialogState.EXPECT_UNOFFICIAL);
        Condition<Update> isWaitForOfficialCallSign = dialogCondition(dialogService, DialogState.EXPECT_OFFICIAL);
        Condition<Update> isWaitForQth = dialogCondition(dialogService, DialogState.EXPECT_QTH);
        Condition<Update> isWaitForSearch = dialogCondition(dialogService, DialogState.EXPECT_SEARCH);

        var saveK2CallSignAction = new SaveK2CallSignAction(callSignService, dialogService);
        var saveOfficialCallSignAction = new SaveOfficialCallSignAction(callSignService, dialogService,
                officialCallSignValidator);
        var saveQthAction = new SaveQthAction(callSignService, dialogService);
        var performSearchAction = new PerformSearchAction(callSignService);

        var sessionChain = new ChainHandler(s -> msgOnAnyUnknown(s.getMessage().getChatId()))
                .setHandler(isWaitForK2CallSign, saveK2CallSignAction)
                .setHandler(isWaitForOfficialCallSign, saveOfficialCallSignAction)
                .setHandler(isWaitForQth, saveQthAction)
                .setHandler(isWaitForSearch, performSearchAction);

        return BranchHandler.builder()
                .condition(isSession)
                .branchTrue(sessionChain)
                .branchFalse(s -> msgOnAnyUnknown(s.getMessage().getChatId()))
                .build();
    }

}
