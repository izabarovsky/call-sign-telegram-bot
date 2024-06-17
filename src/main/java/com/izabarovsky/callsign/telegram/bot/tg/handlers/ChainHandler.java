package com.izabarovsky.callsign.telegram.bot.tg.handlers;

import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions.Condition;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;

import java.util.LinkedList;
import java.util.List;

public class ChainHandler implements Handler<UpdateWrapper, HandlerResult> {

    private final List<ConditionHandlerPair> list;
    private final Handler<UpdateWrapper, HandlerResult> defaultHandler;

    public ChainHandler(Handler<UpdateWrapper, HandlerResult> defaultHandler) {
        this.defaultHandler = defaultHandler;
        this.list = new LinkedList<>();
    }

    public ChainHandler setHandler(Condition<UpdateWrapper> condition, Handler<UpdateWrapper, HandlerResult> handler) {
        this.list.add(new ConditionHandlerPair(condition, handler));
        return this;
    }

    @Override
    public HandlerResult handle(UpdateWrapper payload) {
        return list.stream()
                .filter(s -> s.condition().check(payload))
                .findFirst()
                .map(ConditionHandlerPair::handler)
                .orElse(defaultHandler)
                .handle(payload);
    }

    record ConditionHandlerPair(Condition<UpdateWrapper> condition, Handler<UpdateWrapper, HandlerResult> handler) {
    }

}
