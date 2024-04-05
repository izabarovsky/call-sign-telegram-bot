package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.service.CallSignModel;
import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogState;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;
import java.util.function.Supplier;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgCallSingIsBooked;
import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgEnterValueOrSkip;

public class SaveK2CallSignAction implements Handler<Update, HandlerResult> {
    private final CallSignService callSignService;
    private final DialogStateService dialogStateService;

    public SaveK2CallSignAction(CallSignService callSignService, DialogStateService dialogStateService) {
        this.callSignService = callSignService;
        this.dialogStateService = dialogStateService;
    }

    @Override
    public HandlerResult handle(Update payload) {
        var id = payload.getMessage().getFrom().getId();
        var chatId = payload.getMessage().getChatId();
        String k2CallSign = payload.getMessage().getText();
        if (callSignService.findByK2CallSign(k2CallSign).isPresent()) {
            return msgCallSingIsBooked(chatId, k2CallSign);
        }
        Optional<CallSignModel> callSignModelOpt = callSignService.getCallSign(id);
        Supplier<CallSignModel> newest = () -> CallSignModel.builder()
                .tgId(id)
                .build();
        CallSignModel callSignModel = callSignModelOpt.orElseGet(newest);
        callSignModel.setUserName(payload.getMessage().getFrom().getUserName());
        callSignModel.setFirstName(payload.getMessage().getFrom().getFirstName());
        callSignModel.setLastName(payload.getMessage().getFrom().getLastName());
        callSignModel.setK2CallSign(k2CallSign);
        callSignService.save(callSignModel);
        dialogStateService.putState(payload.getMessage().getFrom().getId(), DialogState.EXPECT_OFFICIAL);
        return msgEnterValueOrSkip(chatId, "OfficialCallSign");
    }

}
