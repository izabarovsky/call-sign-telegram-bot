package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgPrivateStatistics;

public class K2StatisticsPrivateAction implements Handler<UpdateWrapper, HandlerResult> {
    private final CallSignService callSignService;

    public K2StatisticsPrivateAction(CallSignService callSignService) {
        this.callSignService = callSignService;
    }

    @Override
    public HandlerResult handle(UpdateWrapper payload) {
        var chatId = payload.getChatId();
        var threadId = payload.getThreadId();
        return msgPrivateStatistics(chatId, threadId, callSignService.findAll());
    }
}
