package com.izabarovsky.callsign.telegram.bot.tg.utils;

import com.izabarovsky.callsign.telegram.bot.tg.Command;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class MenuUtils {

    public static ReplyKeyboardMarkup buildSkipOrCancelMenu() {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow skip = new KeyboardRow();
        skip.add(Command.SKIP.value());
        keyboardRows.add(skip);
        KeyboardRow cancel = new KeyboardRow();
        cancel.add(Command.CANCEL.value());
        keyboardRows.add(cancel);
        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public static ReplyKeyboardMarkup buildCancelMenu() {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow cancel = new KeyboardRow();
        cancel.add(Command.CANCEL.value());
        keyboardRows.add(cancel);
        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public static ReplyKeyboardMarkup buildMainMenu() {
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow myK2Info = new KeyboardRow();
        myK2Info.add(Command.MY_K2_INFO.value());
        keyboardRows.add(myK2Info);

        KeyboardRow statistics = new KeyboardRow();
        statistics.add(Command.STATISTICS.value());
        keyboardRows.add(statistics);

        KeyboardRow search = new KeyboardRow();
        search.add(Command.SEARCH.value());
        keyboardRows.add(search);

        KeyboardRow edit = new KeyboardRow();
        edit.add(Command.EDIT.value());
        keyboardRows.add(edit);

        KeyboardRow getAll = new KeyboardRow();
        getAll.add(Command.GET_ALL.value());
        keyboardRows.add(getAll);

        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public static ReplyKeyboardMarkup buildCreateMenu() {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow create = new KeyboardRow();
        create.add(Command.CREATE.value());
        keyboardRows.add(create);
        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

}
