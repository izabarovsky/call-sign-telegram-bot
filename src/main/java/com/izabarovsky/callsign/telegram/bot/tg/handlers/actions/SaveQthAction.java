package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.service.CallSignModel;
import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgDialogDone;

public class SaveQthAction implements Handler<Update, HandlerResult> {
    private final CallSignService callSignService;
    private final DialogStateService dialogStateService;

    public SaveQthAction(CallSignService callSignService, DialogStateService dialogStateService) {
        this.callSignService = callSignService;
        this.dialogStateService = dialogStateService;
    }

    @Override
    public HandlerResult handle(Update payload) {
        var msg = payload.getMessage();
        var chatId = msg.getChatId();
        var tgId = msg.getFrom().getId();
        CallSignModel callSignModel = callSignService.getCallSign(tgId).orElseThrow();
        callSignModel.setQth(msg.getText());
        callSignService.save(callSignModel);
        dialogStateService.dropState(chatId);
        return msgDialogDone(chatId);
    }

}
