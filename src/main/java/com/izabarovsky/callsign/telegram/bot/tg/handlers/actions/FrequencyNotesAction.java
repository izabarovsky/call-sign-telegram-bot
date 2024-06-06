package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgFrequencyNotes;

public class FrequencyNotesAction implements Handler<Update, HandlerResult> {

    @Override
    public HandlerResult handle(Update payload) {
        var chatId = payload.getMessage().getChatId();
        var threadId = payload.getMessage().getMessageThreadId();
        return msgFrequencyNotes(chatId, threadId);
    }
}
