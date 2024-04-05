package com.izabarovsky.callsign.telegram.bot.tg.handlers.validator;

import org.springframework.stereotype.Component;

@Component
public class OfficialCallSignValidator {
    private final String pattern = "[a-zA-Z]{2}\\d[a-zA-Z]{2,3}";

    public boolean isValid(String payload) {
        return payload.matches(pattern);
    }

}
