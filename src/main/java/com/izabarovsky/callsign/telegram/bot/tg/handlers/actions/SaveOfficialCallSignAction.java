package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.service.CallSignModel;
import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogState;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.validator.OfficialCallSignValidator;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.*;

public class SaveOfficialCallSignAction implements Handler<UpdateWrapper, HandlerResult> {
    private final CallSignService callSignService;
    private final DialogStateService dialogStateService;
    private final OfficialCallSignValidator officialCallSignValidator;

    public SaveOfficialCallSignAction(CallSignService callSignService,
                                      DialogStateService dialogStateService,
                                      OfficialCallSignValidator officialCallSignValidator) {
        this.callSignService = callSignService;
        this.dialogStateService = dialogStateService;
        this.officialCallSignValidator = officialCallSignValidator;
    }

    @Override
    public HandlerResult handle(UpdateWrapper payload) {
        String officialCallSign = payload.getText().toUpperCase();
        if (!officialCallSignValidator.isValid(officialCallSign)) {
            return msgCallSingIsInvalid(payload.getChatId());
        }
        if (callSignService.findByOfficialSign(officialCallSign).isPresent()) {
            return msgCallSingIsBooked(payload.getChatId(), officialCallSign);
        }
        CallSignModel callSignModel = callSignService.getCallSign(payload.getUserId())
                .orElseThrow(() -> new RuntimeException("Try to get callsign for official update, but not found"));
        callSignModel.setUserName(payload.getUsername());
        callSignModel.setFirstName(payload.getFirstName());
        callSignModel.setLastName(payload.getLastName());
        callSignModel.setOfficialCallSign(officialCallSign);
        callSignService.save(callSignModel);
        dialogStateService.putState(payload.getUserId(), DialogState.EXPECT_QTH);
        return msgEnterValueOrSkip(payload.getChatId(), "QTH");
    }

}
