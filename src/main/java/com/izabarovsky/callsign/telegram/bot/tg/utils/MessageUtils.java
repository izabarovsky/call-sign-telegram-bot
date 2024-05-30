package com.izabarovsky.callsign.telegram.bot.tg.utils;

import com.izabarovsky.callsign.telegram.bot.service.CallSignModel;
import com.izabarovsky.callsign.telegram.bot.tg.Command;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import org.apache.commons.io.IOUtils;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MenuUtils.*;
import static java.util.Objects.nonNull;

public class MessageUtils {

    public static HandlerResult msgMyK2Info(Long chatId, Integer threadId, CallSignModel callSignModel) {
        String payload = parseMyCallSign(callSignModel);
        var msg = SendMessage.builder()
                .chatId(chatId)
                .messageThreadId(threadId)
                .parseMode(ParseMode.HTML)
                .replyMarkup(buildMainMenu())
                .text(payload)
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgK2Info(Long chatId, Integer threadId, CallSignModel callSignModel) {
        String payload = parseCallSign(callSignModel);
        var msg = SendMessage.builder()
                .chatId(chatId)
                .messageThreadId(threadId)
                .parseMode(ParseMode.HTML)
                .replyMarkup(buildMainMenu())
                .text(payload)
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgStatistics(Long chatId, Integer threadId, List<CallSignModel> list) {
        long total = list.size();
        long official = list.stream().filter(s -> nonNull(s.getOfficialCallSign())).count();
        long dmr = list.stream().filter(s -> nonNull(s.getDmrId())).count();
        long nonOfficial = total - official;
        String payload = String.format("""
                        <b>Зареєстровано через бот</b>: %s
                        <b>Мають офіційний позивний</b>: %s
                        <b>Не мають офіційного</b>: %s
                        <b>Мають DMR_ID</b>: %s
                        """,
                total,
                official,
                nonOfficial,
                dmr
        );
        var msg = SendMessage.builder()
                .chatId(chatId)
                .messageThreadId(threadId)
                .parseMode(ParseMode.HTML)
                .replyMarkup(buildMainMenu())
                .text(payload)
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgNewcomer(Long chatId, Integer threadId, String userName) {
        var text = String.format("""
                Вітаю, %s! Схоже ти ще не зареєстроаний. Давай зареєструємо твій позивний К2!
                Клікай сюди @K2CallSignBot
                """, Objects.nonNull(userName) ? userName : "[чел з прихованим username:)]");
        var msg = SendMessage.builder()
                .chatId(chatId)
                .messageThreadId(threadId)
                .replyMarkup(buildCreateMenu())
                .text(text)
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgEnterValueRequired(Long chatId) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .text("Введи свій позивний на К2. Це обов'язково!")
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgOnAnyUnknown(Long chatId) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(buildMainMenu())
                .text("Використовуй кнопки меню")
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgOnNewcomer(Long chatId) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(buildCreateMenu())
                .text("Привіт! Вітаю в комм'юніті К2! \nКлікай Create щоб почати реєстрацію")
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgEnterValueOrSkip(Long chatId, String payload) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(buildSkipOrCancelMenu())
                .text(String.format("Вкажи свій %s. Або пропусти (Skip)", payload))
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgEnterSearchOrCancel(Long chatId, Integer threadId) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .messageThreadId(threadId)
                .replyMarkup(buildCancelMenu())
                .text("Введи позивний або частину позивного для пошуку")
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgCantSkip(Long chatId) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(buildCancelMenu())
                .text("Цей крок не можна пропустити!")
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgCallSingIsBooked(Long chatId, String payload) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(buildSkipOrCancelMenu())
                .text(String.format("Позивний %s вже зайнятий!", payload))
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgCallSingIsInvalid(Long chatId) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(buildSkipOrCancelMenu())
                .text("""
                        Позивний невалідний! 
                        Має відповідати паттерну [2 LETTER][DIGIT][2 or 3 LETTER]
                        Якщо ще не маєш офіційного позивного, просто тисни Skip""")
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgDialogDone(Long chatId) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(buildMainMenu())
                .text("Діалог завершено")
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgSearchResult(Long chatId, Integer threadId, List<CallSignModel> list) {
        String text = list.isEmpty() ? "Нічого не знайдено" : parseList(list);
        var msg = SendMessage.builder()
                .chatId(chatId)
                .messageThreadId(threadId)
                .replyMarkup(buildCancelMenu())
                .parseMode(ParseMode.HTML)
                .text(text)
                .build();
        return new HandlerResult(msg);
    }

    public static SendDocument msgOnGetAll(Long chatId, Integer threadId, String payload) {
        InputFile media = new InputFile();
        media.setMedia(IOUtils.toInputStream(payload, Charset.defaultCharset()), "k2_call_signs.csv");
        return SendDocument.builder()
                .chatId(String.valueOf(chatId))
                .messageThreadId(threadId)
                .parseMode(ParseMode.HTML)
                .replyMarkup(buildMainMenu())
                .document(media)
                .build();
    }

    public static HandlerResult msgK2InfoNotFound(Long chatId, Integer threadId, String username) {
        String payload = String.format("""
                Учасника [%s] не знайдено
                Можливо він не реєструвався...
                """, username);
        var msg = SendMessage.builder()
                .chatId(chatId)
                .messageThreadId(threadId)
                .parseMode(ParseMode.HTML)
                .replyMarkup(buildMainMenu())
                .text(payload)
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgK2InfoHowTo(Long chatId, Integer threadId) {
        var payload = String.format("Використовуй команду так: %s@username", Command.K2_INFO.value());
        var msg = SendMessage.builder()
                .chatId(chatId)
                .messageThreadId(threadId)
                .parseMode(ParseMode.HTML)
                .replyMarkup(buildMainMenu())
                .text(payload)
                .build();
        return new HandlerResult(msg);
    }

    public static SendMessage congratsDmrIdMsg(String chatId, String threadId, CallSignModel callSign) {
        var payload = newDmrId(callSign);
        return SendMessage.builder()
                .chatId(chatId)
                .messageThreadId(Integer.valueOf(threadId))
                .parseMode(ParseMode.HTML)
                .text(payload)
                .build();
    }

    public static String parseList(List<CallSignModel> list) {
        StringBuilder text = new StringBuilder(String.format("Знайдено %s учасників:\n\n", list.size()));
        list.forEach(s -> text.append(parseCallSign(s)).append("\n\n"));
        return text.toString();
    }

    public static String parseMyCallSign(CallSignModel callSignModel) {
        return String.format("<b>K2CallSign</b>: %s\n<b>OfficialCallSign</b>: %s\n<b>QTH</b>: %s \n<b>DMR_ID</b>: %s",
                callSignModel.getK2CallSign(),
                callSignModel.getOfficialCallSign(),
                callSignModel.getQth(),
                callSignModel.getDmrId()
        );
    }

    public static String parseCallSign(CallSignModel callSignModel) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        return String.format("""
                        <b>Username</b>: %s
                        <b>K2CallSign</b>: %s
                        <b>OfficialCallSign</b>: %s
                        <b>QTH</b>: %s
                        <b>DMR_ID</b>: %s
                        <b>Registered</b>: %s""",
                Objects.isNull(callSignModel.getUserName()) ? "hidden" : "@" + callSignModel.getUserName(),
                callSignModel.getK2CallSign(),
                callSignModel.getOfficialCallSign(),
                callSignModel.getQth(),
                callSignModel.getDmrId(),
                formatter.format(callSignModel.getCreationTimestamp())
        );
    }

    public static String newDmrId(CallSignModel k2CallSign) {
        return String.format("""
                        Комм'юніті К2 поздоровляє %s [%s]
                        з отриманням DMRID [%s]!
                        Ласкаво просимо в цифру!""",
                k2CallSign.getK2CallSign(),
                k2CallSign.getOfficialCallSign(),
                k2CallSign.getDmrId()
        );
    }

}
