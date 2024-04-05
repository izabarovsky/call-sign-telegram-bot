package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgOnAnyUnknown;

public class CleanStateAction implements Handler<Update, HandlerResult> {

    private final DialogStateService dialogService;

    public CleanStateAction(DialogStateService dialogService) {
        this.dialogService = dialogService;
    }

    @Override
    public HandlerResult handle(Update payload) {
        var id = payload.getMessage().getFrom().getId();
        var chatId = payload.getMessage().getChatId();
        dialogService.dropState(id);
        return msgOnAnyUnknown(chatId);
    }

}
