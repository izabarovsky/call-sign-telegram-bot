package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.service.CallSignModel;
import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogState;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;

import java.util.Optional;
import java.util.function.Supplier;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgCallSingIsBooked;
import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgEnterValueOrSkip;

public class SaveK2CallSignAction implements Handler<UpdateWrapper, HandlerResult> {
    private final CallSignService callSignService;
    private final DialogStateService dialogStateService;

    public SaveK2CallSignAction(CallSignService callSignService, DialogStateService dialogStateService) {
        this.callSignService = callSignService;
        this.dialogStateService = dialogStateService;
    }

    @Override
    public HandlerResult handle(UpdateWrapper payload) {
        var id = payload.getUserId();
        var chatId = payload.getChatId();
        String k2CallSign = payload.getText();
        if (callSignService.findByK2CallSign(k2CallSign).isPresent()) {
            return msgCallSingIsBooked(chatId, k2CallSign);
        }
        Optional<CallSignModel> callSignModelOpt = callSignService.getCallSign(id);
        Supplier<CallSignModel> newest = () -> CallSignModel.builder()
                .tgId(id)
                .build();
        CallSignModel callSignModel = callSignModelOpt.orElseGet(newest);
        callSignModel.setUserName(payload.getUsername());
        callSignModel.setFirstName(payload.getFirstName());
        callSignModel.setLastName(payload.getLastName());
        callSignModel.setK2CallSign(k2CallSign);
        callSignService.save(callSignModel);
        dialogStateService.putState(id, DialogState.EXPECT_OFFICIAL);
        return msgEnterValueOrSkip(chatId, "OfficialCallSign");
    }

}
