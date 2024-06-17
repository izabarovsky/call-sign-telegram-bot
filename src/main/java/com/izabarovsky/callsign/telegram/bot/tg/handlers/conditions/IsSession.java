package com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions;

import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;

import java.util.Objects;

public class IsSession implements Condition<UpdateWrapper> {
    private final DialogStateService dialogService;

    public IsSession(DialogStateService dialogService) {
        this.dialogService = dialogService;
    }

    @Override
    public boolean check(UpdateWrapper update) {
        return Objects.nonNull(dialogService.getState(update.getUserId()));
    }

}
