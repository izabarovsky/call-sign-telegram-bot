package com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions;

import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogState;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DialogConditionsFactory {

    public static Condition<Update> dialogCondition(DialogStateService dialogService, DialogState state) {
        return update -> {
            var id = update.getMessage().getFrom().getId();
            return state.equals(dialogService.getState(id));
        };
    }

}
