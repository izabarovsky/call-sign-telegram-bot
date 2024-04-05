package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgStatistics;

public class K2StatisticsAction implements Handler<Update, HandlerResult> {
    private final CallSignService callSignService;

    public K2StatisticsAction(CallSignService callSignService) {
        this.callSignService = callSignService;
    }

    @Override
    public HandlerResult handle(Update payload) {
        var chatId = payload.getMessage().getChatId();
        var threadId = payload.getMessage().getMessageThreadId();
        return msgStatistics(chatId, threadId, callSignService.findAll());
    }
}
