package com.izabarovsky.callsign.telegram.bot.tg.handlers;

import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import com.izabarovsky.callsign.telegram.bot.tg.Command;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogState;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.actions.*;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions.*;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.validator.OfficialCallSignValidator;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import com.izabarovsky.callsign.telegram.bot.utils.CsvUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions.CmdConditionsFactory.cmdCondition;
import static com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions.DialogConditionsFactory.dialogCondition;
import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.*;

@Slf4j
@Component
public class DefaultRootHandler implements Handler<UpdateWrapper, HandlerResult>, RootHandler<UpdateWrapper, HandlerResult> {
    private final Handler<UpdateWrapper, HandlerResult> dummyHandler = s -> null;
    private final Condition<UpdateWrapper> isMyK2Info;
    private final Condition<UpdateWrapper> isK2Info;
    private final Condition<UpdateWrapper> isStatistics;
    private final Condition<UpdateWrapper> isFrequencyNotes;
    private final Condition<UpdateWrapper> isCommand;
    private final Condition<UpdateWrapper> isPersonalChat;
    private final Condition<UpdateWrapper> isSession;
    private final Condition<UpdateWrapper> isExistsUser;
    private final Condition<UpdateWrapper> isSkip;
    private final Condition<UpdateWrapper> isCancel;
    private final Condition<UpdateWrapper> isRequiredForNewcomer;
    private final Condition<UpdateWrapper> isCreate;
    private final Condition<UpdateWrapper> isEdit;
    private final Condition<UpdateWrapper> isGetAll;
    private final Condition<UpdateWrapper> isSearch;
    private final Condition<UpdateWrapper> isWaitForK2CallSign;
    private final Condition<UpdateWrapper> isWaitForOfficialCallSign;
    private final Condition<UpdateWrapper> isWaitForQth;
    private final Condition<UpdateWrapper> isWaitForSearch;

    private final Handler<UpdateWrapper, HandlerResult> myK2InfoGroupAction;
    private final Handler<UpdateWrapper, HandlerResult> myK2InfoPrivateAction;
    private final Handler<UpdateWrapper, HandlerResult> k2InfoAction;
    private final Handler<UpdateWrapper, HandlerResult> k2StatisticsAction;
    private final Handler<UpdateWrapper, HandlerResult> frequencyNotesAction;
    private final Handler<UpdateWrapper, HandlerResult> nextStateAction;
    private final Handler<UpdateWrapper, HandlerResult> cleanStateAction;
    private final Handler<UpdateWrapper, HandlerResult> saveK2CallSignAction;
    private final Handler<UpdateWrapper, HandlerResult> saveOfficialCallSignAction;
    private final Handler<UpdateWrapper, HandlerResult> saveQthAction;
    private final Handler<UpdateWrapper, HandlerResult> performSearchAction;
    private final Handler<UpdateWrapper, HandlerResult> startDialogCreateAction;
    private final Handler<UpdateWrapper, HandlerResult> startDialogEditAction;
    private final Handler<UpdateWrapper, HandlerResult> startDialogSearchAction;
    private final Handler<UpdateWrapper, HandlerResult> getAllCallSignsAction;


    public DefaultRootHandler(DialogStateService dialogService,
                              CallSignService callSignService,
                              OfficialCallSignValidator officialCallSignValidator,
                              CsvUtil csvUtil
    ) {
        isMyK2Info = cmdCondition(Command.MY_K2_INFO);
        isK2Info = cmdCondition(Command.K2_INFO);
        isStatistics = cmdCondition(Command.STATISTICS);
        isFrequencyNotes = cmdCondition(Command.REPEATERS);
        isCommand = new IsCommand();
        isPersonalChat = new IsPersonalChat();
        isSession = new IsSession(dialogService);
        isExistsUser = new IsExistsUser(callSignService);
        isSkip = cmdCondition(Command.SKIP);
        isCancel = cmdCondition(Command.CANCEL);
        isRequiredForNewcomer = new IsRequiredForNewcomer(dialogService);
        isCreate = cmdCondition(Command.CREATE);
        isEdit = cmdCondition(Command.EDIT);
        isGetAll = cmdCondition(Command.GET_ALL);
        isSearch = cmdCondition(Command.SEARCH);
        isWaitForK2CallSign = dialogCondition(dialogService, DialogState.EXPECT_UNOFFICIAL);
        isWaitForOfficialCallSign = dialogCondition(dialogService, DialogState.EXPECT_OFFICIAL);
        isWaitForQth = dialogCondition(dialogService, DialogState.EXPECT_QTH);
        isWaitForSearch = dialogCondition(dialogService, DialogState.EXPECT_SEARCH);

        myK2InfoGroupAction = new MyK2InfoGroupAction(callSignService);
        myK2InfoPrivateAction = new MyK2InfoPrivateAction(callSignService);
        k2InfoAction = new K2InfoAction(callSignService);
        k2StatisticsAction = new K2StatisticsAction(callSignService);
        frequencyNotesAction = new FrequencyNotesAction();
        nextStateAction = new NextStateAction(dialogService);
        cleanStateAction = new CleanStateAction(dialogService);
        saveK2CallSignAction = new SaveK2CallSignAction(callSignService, dialogService);
        saveOfficialCallSignAction = new SaveOfficialCallSignAction(callSignService, dialogService,
                officialCallSignValidator);
        saveQthAction = new SaveQthAction(callSignService, dialogService);
        performSearchAction = new PerformSearchAction(callSignService);
        startDialogCreateAction = new StartDialogCreateAction(dialogService);
        startDialogEditAction = new StartDialogEditAction(dialogService);
        startDialogSearchAction = new StartDialogSearchAction(dialogService);
        getAllCallSignsAction = new GetAllCallSignsAction(callSignService, csvUtil);
    }

    public HandlerResult handle(UpdateWrapper update) {
        log.info("{}", update);
        return rootHandler().handle(update);
    }

    private Handler<UpdateWrapper, HandlerResult> rootHandler() {
        var privateChatCommandChain = BranchHandler.builder()
                .condition(isCommand)
                .branchTrue(commandBranch())
                .branchFalse(textBranch())
                .build();

        var groupChatCommandChain = new ChainHandler(dummyHandler)
                .setHandler(isMyK2Info, myK2InfoGroupAction)
                .setHandler(isK2Info, k2InfoAction)
                .setHandler(isStatistics, k2StatisticsAction)
                .setHandler(isFrequencyNotes, frequencyNotesAction);

        return BranchHandler.builder()
                .condition(isPersonalChat)
                .branchTrue(privateChatCommandChain)
                .branchFalse(groupChatCommandChain)
                .build();
    }

    private BranchHandler commandBranch() {
        return BranchHandler.builder()
                .condition(isSession)
                .branchTrue(commandInSessionBranch())
                .branchFalse(commandOutSessionBranch())
                .build();
    }

    private BranchHandler commandInSessionBranch() {
        var existsUser = BranchHandler.builder()
                .condition(isSkip)
                .branchTrue(nextStateAction)
                .branchFalse(s -> msgOnAnyUnknown(s.getChatId()))
                .build();

        var newcomerSkipNode = BranchHandler.builder()
                .condition(isRequiredForNewcomer)
                .branchTrue(s -> msgCantSkip(s.getChatId()))
                .branchFalse(s -> msgOnAnyUnknown(s.getChatId()))
                .build();

        var newcomerUser = BranchHandler.builder()
                .condition(isSkip)
                .branchTrue(newcomerSkipNode)
                .branchFalse(s -> msgOnAnyUnknown(s.getChatId()))
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
        var newcomerUser = BranchHandler.builder()
                .condition(isCreate)
                .branchTrue(startDialogCreateAction)
                .branchFalse(s -> msgNewcomer(
                        s.getChatId(),
                        s.getThreadId(),
                        s.getUsername()))
                .build();

        var existsUserChain = new ChainHandler(s -> msgOnAnyUnknown(s.getChatId()))
                .setHandler(isEdit, startDialogEditAction)
                .setHandler(isMyK2Info, myK2InfoPrivateAction)
                .setHandler(isK2Info, k2InfoAction)
                .setHandler(isSearch, startDialogSearchAction)
                .setHandler(isGetAll, getAllCallSignsAction)
                .setHandler(isStatistics, k2StatisticsAction)
                .setHandler(isFrequencyNotes, frequencyNotesAction);

        return BranchHandler.builder()
                .condition(isExistsUser)
                .branchTrue(existsUserChain)
                .branchFalse(newcomerUser)
                .build();
    }

    private BranchHandler textBranch() {
        var sessionChain = new ChainHandler(s -> msgOnAnyUnknown(s.getChatId()))
                .setHandler(isWaitForK2CallSign, saveK2CallSignAction)
                .setHandler(isWaitForOfficialCallSign, saveOfficialCallSignAction)
                .setHandler(isWaitForQth, saveQthAction)
                .setHandler(isWaitForSearch, performSearchAction);

        return BranchHandler.builder()
                .condition(isSession)
                .branchTrue(sessionChain)
                .branchFalse(s -> msgOnAnyUnknown(s.getChatId()))
                .build();
    }

}
