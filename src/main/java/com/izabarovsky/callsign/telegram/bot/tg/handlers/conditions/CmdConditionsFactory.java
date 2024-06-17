package com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions;

import com.izabarovsky.callsign.telegram.bot.tg.Command;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;

public class CmdConditionsFactory {

    public static Condition<UpdateWrapper> cmdCondition(Command cmd) {
        return update -> update.getText().toLowerCase().startsWith(cmd.value().toLowerCase());
    }

}
