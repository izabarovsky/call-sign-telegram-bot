package com.izabarovsky.callsign.telegram.bot.tg.handlers;

import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions.Condition;
import lombok.Builder;
import org.telegram.telegrambots.meta.api.objects.Update;

@Builder
public class BranchHandler implements Handler<Update, HandlerResult> {

    private Condition<Update> condition;
    private Handler<Update, HandlerResult> branchTrue;
    private Handler<Update, HandlerResult> branchFalse;

    @Override
    public HandlerResult handle(Update payload) {
        return condition.check(payload) ? branchTrue.handle(payload) : branchFalse.handle(payload);
    }

}
