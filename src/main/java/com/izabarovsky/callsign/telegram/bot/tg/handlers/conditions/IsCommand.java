package com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions;

import org.telegram.telegrambots.meta.api.objects.Update;

public class IsCommand implements Condition<Update> {

    @Override
    public boolean check(Update update) {
        return update.getMessage().getText().startsWith("/");
    }

}
