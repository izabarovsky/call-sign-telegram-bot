package com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions;

import com.izabarovsky.callsign.telegram.bot.tg.Command;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CmdConditionsFactory {

    public static Condition<Update> cmdCondition(Command cmd) {
        return update -> update.getMessage().getText().toLowerCase().startsWith(cmd.value().toLowerCase());
    }

}
