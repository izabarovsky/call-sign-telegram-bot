package com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions;

import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class IsExistsUser implements Condition<Update> {
    private final CallSignService callSignService;

    public IsExistsUser(CallSignService callSignService) {
        this.callSignService = callSignService;
    }

    @Override
    public boolean check(Update update) {
        var tgId = update.getMessage().getFrom().getId();
        return callSignService.getCallSign(tgId).isPresent();
    }

}
