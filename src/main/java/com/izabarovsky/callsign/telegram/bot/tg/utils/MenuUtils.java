package com.izabarovsky.callsign.telegram.bot.tg.utils;

import com.izabarovsky.callsign.telegram.bot.tg.Command;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MenuUtils {

    public static ReplyKeyboardMarkup buildSkipOrCancelMenu() {
        List<KeyboardRow> keyboardRows = keyboardRows(Command.SKIP, Command.CANCEL);
        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public static ReplyKeyboardMarkup buildCancelMenu() {
        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboardRows(Command.CANCEL))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public static ReplyKeyboardMarkup buildMainMenu() {
        List<KeyboardRow> keyboardRows = keyboardRows(
                Command.MY_K2_INFO,
                Command.STATISTICS,
                Command.SEARCH,
                Command.EDIT,
                Command.GET_ALL,
                Command.FREQUENCY_NOTES
        );
        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public static ReplyKeyboardMarkup buildCreateMenu() {
        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboardRows(Command.CREATE))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    private static List<KeyboardRow> keyboardRows(Command... commands) {
        Function<Command, KeyboardRow> mapper = s -> {
            KeyboardRow row = new KeyboardRow();
            row.add(s.value());
            return row;
        };
        return Stream.of(commands).map(mapper).collect(Collectors.toList());
    }

}
