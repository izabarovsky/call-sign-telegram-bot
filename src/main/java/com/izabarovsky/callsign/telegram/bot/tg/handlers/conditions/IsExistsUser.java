package com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions;

import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;

public class IsExistsUser implements Condition<UpdateWrapper> {
    private final CallSignService callSignService;

    public IsExistsUser(CallSignService callSignService) {
        this.callSignService = callSignService;
    }

    @Override
    public boolean check(UpdateWrapper update) {
        var tgId = update.getUserId();
        return callSignService.getCallSign(tgId).isPresent();
    }

}
