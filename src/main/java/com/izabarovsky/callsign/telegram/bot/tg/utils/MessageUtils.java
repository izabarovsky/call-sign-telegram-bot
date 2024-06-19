package com.izabarovsky.callsign.telegram.bot.tg.utils;

import com.izabarovsky.callsign.telegram.bot.service.CallSignModel;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import org.apache.commons.io.IOUtils;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.nio.charset.Charset;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MenuUtils.*;
import static com.izabarovsky.callsign.telegram.bot.tg.utils.TextUtils.*;
import static java.util.Objects.nonNull;

public class MessageUtils {

    public static HandlerResult msgGroupMyK2Info(Long chatId, Integer threadId, CallSignModel callSignModel) {
        String payload = parseMyCallSign(callSignModel);
        return newMessage(chatId, threadId, payload, buildMainMenu());
    }

    public static HandlerResult msgPrivateMyK2Info(Long chatId, Integer threadId, CallSignModel callSignModel) {
        String payload = parseMyCallSign(callSignModel);
        return newMessage(chatId, threadId, payload, buildEditInlineMenu());
    }

    public static HandlerResult msgK2Info(Long chatId, Integer threadId, CallSignModel callSignModel) {
        String payload = parseCallSign(callSignModel);
        return newMessage(chatId, threadId, payload, buildMainMenu());
    }

    public static HandlerResult msgStatistics(Long chatId, Integer threadId, List<CallSignModel> list) {
        long total = list.size();
        long official = list.stream().filter(s -> nonNull(s.getOfficialCallSign())).count();
        long dmr = list.stream().filter(s -> nonNull(s.getDmrId())).count();
        long nonOfficial = total - official;
        return newMessage(chatId, threadId, textStatistics(total, official, nonOfficial, dmr), buildMainMenu());
    }

    public static HandlerResult msgNewcomer(Long chatId, Integer threadId, String userName) {
        return newMessage(chatId, threadId, textHelloNewcomer(userName), buildCreateMenu());
    }

    public static HandlerResult msgEnterValueRequired(Long chatId) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .text(TextUtils.textK2CallSignRequired())
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgOnAnyUnknown(Long chatId) {
        return newMessage(chatId, null, textUseMenuButtons(), buildMainMenu());
    }

    public static HandlerResult msgEnterValueOrSkip(Long chatId, String payload) {
        return newMessage(chatId, null, textEnterValueOrSkip(payload), buildSkipOrCancelMenu());
    }

    public static HandlerResult msgEnterSearchOrCancel(Long chatId, Integer threadId) {
        return newMessage(chatId, threadId, textEnterSearch(), buildCancelMenu());
    }

    public static HandlerResult msgCantSkip(Long chatId) {
        return newMessage(chatId, null, textStepCantSkip(), buildCancelMenu());
    }

    public static HandlerResult msgCallSingIsBooked(Long chatId, String callSign) {
        return newMessage(chatId, null, textCallSingIsBooked(callSign), buildSkipOrCancelMenu());
    }

    public static HandlerResult msgCallSingIsInvalid(Long chatId) {
        return newMessage(chatId, null, textCallSingIsInvalid(), buildSkipOrCancelMenu());
    }

    public static HandlerResult msgDialogDone(Long chatId) {
        return newMessage(chatId, null, textDialogDone(), buildMainMenu());
    }

    public static HandlerResult msgSearchResult(Long chatId, Integer threadId, List<CallSignModel> list) {
        String text = list.isEmpty() ? TextUtils.textNothingFound() : parseList(list);
        return newMessage(chatId, threadId, text, buildCancelMenu());
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
        return newMessage(chatId, threadId, textUserNotFound(username), buildMainMenu());
    }

    public static HandlerResult msgK2InfoHowTo(Long chatId, Integer threadId) {
        return newMessage(chatId, threadId, textUseK2InfoCommandAsFollow(), buildMainMenu());
    }

    public static SendMessage msgCongratsDmrIdMsg(String chatId, String threadId, CallSignModel callSign) {
        var payload = TextUtils.textOnNewDmrId(callSign);
        return SendMessage.builder()
                .chatId(chatId)
                .messageThreadId(Integer.valueOf(threadId))
                .parseMode(ParseMode.HTML)
                .text(payload)
                .build();
    }

    public static HandlerResult msgRepeaters(Long chatId, Integer threadId) {
        return newMessage(chatId, threadId, textRepeaters(), buildRepeatersInlineMenu());
    }

    public static HandlerResult msgRepeatersNonOfficial(Long chatId, Integer threadId) {
        return newMessage(chatId, threadId, textRepeatersNonOfficial(), null);
    }

    public static HandlerResult msgRepeatersOfficial(Long chatId, Integer threadId) {
        return newMessage(chatId, threadId, textRepeatersOfficial(), null);
    }

    public static HandlerResult msgRepeatersEcholink(Long chatId, Integer threadId) {
        return newMessage(chatId, threadId, textRepeatersEcholink(), null);
    }

    public static String parseList(List<CallSignModel> list) {
        StringBuilder text = new StringBuilder(String.format("Знайдено %s учасників:\n\n", list.size()));
        list.forEach(s -> text.append(parseCallSign(s)).append("\n\n"));
        return text.toString();
    }

    public static String parseMyCallSign(CallSignModel callSignModel) {
        return String.format("""
                        <b>K2CallSign</b>: %s
                        <b>OfficialCallSign</b>: %s
                        <b>QTH</b>: %s
                        <b>DMR_ID</b>: %s""",
                callSignModel.getK2CallSign(),
                callSignModel.getOfficialCallSign(),
                callSignModel.getQth(),
                callSignModel.getDmrId()
        );
    }

    public static String parseCallSign(CallSignModel callSignModel) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE.withZone(ZoneId.systemDefault());
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

    private static HandlerResult newMessage(Long chatId, Integer threadId, String text, ReplyKeyboard replyMarkup) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .messageThreadId(threadId)
                .parseMode(ParseMode.HTML)
                .text(text)
                .build();
        return new HandlerResult(msg);
    }

}
