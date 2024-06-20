package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import lombok.AllArgsConstructor;

import java.util.function.BiFunction;

@AllArgsConstructor
public class SimpleMessageAction implements Handler<UpdateWrapper, HandlerResult> {
    private final BiFunction<Long, Integer, HandlerResult> message;

    @Override
    public HandlerResult handle(UpdateWrapper payload) {
        var chatId = payload.getChatId();
        var threadId = payload.getThreadId();
        return message.apply(chatId, threadId);
    }

}
