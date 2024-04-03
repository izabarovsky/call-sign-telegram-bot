package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.service.CallSignModel;
import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogState;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.validator.OfficialCallSignValidator;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.*;

public class SaveOfficialCallSignAction implements Handler<Update, HandlerResult> {
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
    public HandlerResult handle(Update payload) {
        var msg = payload.getMessage();
        String officialCallSign = msg.getText().toUpperCase();
        if (!officialCallSignValidator.isValid(officialCallSign)) {
            return msgCallSingIsInvalid(msg.getChatId());
        }
        if (callSignService.findByOfficialSign(officialCallSign).isPresent()) {
            return msgCallSingIsBooked(msg.getChatId(), officialCallSign);
        }
        CallSignModel callSignModel = callSignService.getCallSign(msg.getFrom().getId())
                .orElseThrow(() -> new RuntimeException("Try to get callsign for official update, but not found"));
        callSignModel.setUserName(msg.getFrom().getUserName());
        callSignModel.setFirstName(msg.getFrom().getFirstName());
        callSignModel.setLastName(msg.getFrom().getLastName());
        callSignModel.setOfficialCallSign(officialCallSign);
        callSignService.save(callSignModel);
        dialogStateService.putState(msg.getFrom().getId(), DialogState.EXPECT_QTH);
        return msgEnterValueOrSkip(msg.getChatId(), "QTH");
    }

}
