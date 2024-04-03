package com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions;

import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogState;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class IsRequiredForNewcomer implements Condition<Update> {
    private final DialogStateService dialogService;

    public IsRequiredForNewcomer(DialogStateService dialogService) {
        this.dialogService = dialogService;
    }

    @Override
    public boolean check(Update update) {
        return DialogState.EXPECT_UNOFFICIAL.equals(dialogService.getState(update.getMessage().getFrom().getId()));
    }

}
