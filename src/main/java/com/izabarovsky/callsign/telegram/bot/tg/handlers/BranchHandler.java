package com.izabarovsky.callsign.telegram.bot.tg.handlers;

import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions.Condition;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import lombok.Builder;

@Builder
public class BranchHandler implements Handler<UpdateWrapper, HandlerResult> {

    private Condition<UpdateWrapper> condition;
    private Handler<UpdateWrapper, HandlerResult> branchTrue;
    private Handler<UpdateWrapper, HandlerResult> branchFalse;

    @Override
    public HandlerResult handle(UpdateWrapper payload) {
        return condition.check(payload) ? branchTrue.handle(payload) : branchFalse.handle(payload);
    }

}
