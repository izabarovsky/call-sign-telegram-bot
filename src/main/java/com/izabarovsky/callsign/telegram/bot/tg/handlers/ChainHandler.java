package com.izabarovsky.callsign.telegram.bot.tg.handlers;

import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions.Condition;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.LinkedList;
import java.util.List;

public class ChainHandler implements Handler<Update, HandlerResult> {

    private final List<ConditionHandlerPair> list;
    private final Handler<Update, HandlerResult> defaultHandler;

    public ChainHandler(Handler<Update, HandlerResult> defaultHandler) {
        this.defaultHandler = defaultHandler;
        this.list = new LinkedList<>();
    }

    public ChainHandler setHandler(Condition<Update> condition, Handler<Update, HandlerResult> handler) {
        this.list.add(new ConditionHandlerPair(condition, handler));
        return this;
    }

    @Override
    public HandlerResult handle(Update payload) {
        return list.stream()
                .filter(s -> s.condition().check(payload))
                .findFirst()
                .map(ConditionHandlerPair::handler)
                .orElse(defaultHandler)
                .handle(payload);
    }

    record ConditionHandlerPair(Condition<Update> condition, Handler<Update, HandlerResult> handler) {
    }

}
