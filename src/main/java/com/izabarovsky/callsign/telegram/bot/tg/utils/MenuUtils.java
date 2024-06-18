package com.izabarovsky.callsign.telegram.bot.tg.utils;

import com.izabarovsky.callsign.telegram.bot.tg.Command;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow myK2Row = newRow(Command.MY_K2_INFO);
        KeyboardRow searchRow = newRow(Command.SEARCH);
        KeyboardRow membersRow = newRow(Command.STATISTICS, Command.GET_ALL);
        KeyboardRow notesRow = newRow(Command.FREQUENCY_NOTES);
        keyboardRows.add(myK2Row);
        keyboardRows.add(searchRow);
        keyboardRows.add(membersRow);
        keyboardRows.add(notesRow);
        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public static InlineKeyboardMarkup buildEditInlineMenu() {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Редагувати");
        inlineKeyboardButton.setCallbackData(Command.EDIT.value());
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(inlineKeyboardButton);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);
        return InlineKeyboardMarkup.builder()
                .keyboard(rowList)
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

    private static KeyboardRow newRow(Command... commands) {
        KeyboardRow row = new KeyboardRow();
        Stream.of(commands).forEach(s -> row.add(s.value()));
        return row;
    }

}
