package com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions;

import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogState;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;

public class DialogConditionsFactory {

    public static Condition<UpdateWrapper> dialogCondition(DialogStateService dialogService, DialogState state) {
        return update -> {
            var id = update.getUserId();
            return state.equals(dialogService.getState(id));
        };
    }

}
