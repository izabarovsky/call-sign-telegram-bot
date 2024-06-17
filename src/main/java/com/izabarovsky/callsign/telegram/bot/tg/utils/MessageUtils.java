package com.izabarovsky.callsign.telegram.bot.tg.utils;

import com.izabarovsky.callsign.telegram.bot.service.CallSignModel;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import org.apache.commons.io.IOUtils;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.nio.charset.Charset;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MenuUtils.*;
import static com.izabarovsky.callsign.telegram.bot.tg.utils.TextUtils.textStatistics;
import static java.util.Objects.nonNull;

public class MessageUtils {

    public static HandlerResult msgGroupMyK2Info(Long chatId, Integer threadId, CallSignModel callSignModel) {
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

    public static HandlerResult msgPrivateMyK2Info(Long chatId, Integer threadId, CallSignModel callSignModel) {
        String payload = parseMyCallSign(callSignModel);
        var msg = SendMessage.builder()
                .chatId(chatId)
                .messageThreadId(threadId)
                .parseMode(ParseMode.HTML)
                .replyMarkup(buildEditInlineMenu())
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
        var msg = SendMessage.builder()
                .chatId(chatId)
                .messageThreadId(threadId)
                .parseMode(ParseMode.HTML)
                .replyMarkup(buildMainMenu())
                .text(textStatistics(total, official, nonOfficial, dmr))
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgNewcomer(Long chatId, Integer threadId, String userName) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .messageThreadId(threadId)
                .replyMarkup(buildCreateMenu())
                .text(TextUtils.textHelloNewcomer(userName))
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgEnterValueRequired(Long chatId) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .text(TextUtils.textK2CallSignRequired())
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgOnAnyUnknown(Long chatId) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(buildMainMenu())
                .text(TextUtils.textUseMenuButtons())
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgEnterValueOrSkip(Long chatId, String payload) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(buildSkipOrCancelMenu())
                .text(TextUtils.textEnterValueOrSkip(payload))
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgEnterSearchOrCancel(Long chatId, Integer threadId) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .messageThreadId(threadId)
                .replyMarkup(buildCancelMenu())
                .text(TextUtils.textEnterSearch())
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgCantSkip(Long chatId) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(buildCancelMenu())
                .text(TextUtils.textStepCantSkip())
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgCallSingIsBooked(Long chatId, String callSign) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(buildSkipOrCancelMenu())
                .text(TextUtils.textCallSingIsBooked(callSign))
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgCallSingIsInvalid(Long chatId) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(buildSkipOrCancelMenu())
                .text(TextUtils.textCallSingIsInvalid())
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgDialogDone(Long chatId) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(buildMainMenu())
                .text(TextUtils.textDialogDone())
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgSearchResult(Long chatId, Integer threadId, List<CallSignModel> list) {
        String text = list.isEmpty() ? TextUtils.textNothingFound() : parseList(list);
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
        var msg = SendMessage.builder()
                .chatId(chatId)
                .messageThreadId(threadId)
                .parseMode(ParseMode.HTML)
                .replyMarkup(buildMainMenu())
                .text(TextUtils.textUserNotFound(username))
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgK2InfoHowTo(Long chatId, Integer threadId) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .messageThreadId(threadId)
                .parseMode(ParseMode.HTML)
                .replyMarkup(buildMainMenu())
                .text(TextUtils.textUseK2InfoCommandAsFollow())
                .build();
        return new HandlerResult(msg);
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

    public static HandlerResult msgFrequencyNotes(Long chatId, Integer threadId) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .messageThreadId(threadId)
                .parseMode(ParseMode.HTML)
                .replyMarkup(buildMainMenu())
                .text(TextUtils.textFrequencyNotes())
                .build();
        return new HandlerResult(msg);
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

}
