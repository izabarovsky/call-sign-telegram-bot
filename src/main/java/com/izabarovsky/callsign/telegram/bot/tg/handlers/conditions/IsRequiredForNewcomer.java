package com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions;

import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogState;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;

public class IsRequiredForNewcomer implements Condition<UpdateWrapper> {
    private final DialogStateService dialogService;

    public IsRequiredForNewcomer(DialogStateService dialogService) {
        this.dialogService = dialogService;
    }

    @Override
    public boolean check(UpdateWrapper update) {
        return DialogState.EXPECT_UNOFFICIAL.equals(dialogService.getState(update.getUserId()));
    }

}
