package com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions;

import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

public class IsSession implements Condition<Update> {
    private final DialogStateService dialogService;

    public IsSession(DialogStateService dialogService) {
        this.dialogService = dialogService;
    }

    @Override
    public boolean check(Update update) {
        return Objects.nonNull(dialogService.getState(update.getMessage().getFrom().getId()));
    }

}
