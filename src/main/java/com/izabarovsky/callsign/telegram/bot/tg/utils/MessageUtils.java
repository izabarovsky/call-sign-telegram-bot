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
                        <b>Registered members</b>: %s
                        <b>With official callsign</b>: %s
                        <b>Without official</b>: %s
                        <b>With DMR_ID</b>: %s
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
        var msg = SendMessage.builder()
                .chatId(chatId)
                .messageThreadId(threadId)
                .replyMarkup(buildCreateMenu())
                .text(String.format("Hi, %s! Looks like you newcomer. Let's create your K2CallSign!", userName))
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgEnterValueRequired(Long chatId, String payload) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .text(String.format("Enter your %s. You can't skip it!", payload))
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgOnAnyUnknown(Long chatId) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(buildMainMenu())
                .text("Please, use menu buttons to interact")
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgOnNewcomer(Long chatId) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(buildCreateMenu())
                .text("Hi! Wellcome to K2 community!\nClick Create to start dialog")
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgEnterValueOrSkip(Long chatId, String payload) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(buildSkipOrCancelMenu())
                .text(String.format("Enter your %s. Or skip", payload))
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgEnterSearchOrCancel(Long chatId, Integer threadId) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .messageThreadId(threadId)
                .replyMarkup(buildCancelMenu())
                .text("Enter your search word or cancel")
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgCantSkip(Long chatId) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(buildCancelMenu())
                .text("You can't skip this step!")
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgCallSingIsBooked(Long chatId, String payload) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(buildSkipOrCancelMenu())
                .text(String.format("CallSign %s is booked! Please, take another one!", payload))
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgCallSingIsInvalid(Long chatId) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(buildSkipOrCancelMenu())
                .text("CallSign invalid! Must match pattern [2 LETTER][DIGIT][2 or 3 LETTER]. Example: UT3UUU")
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgDialogDone(Long chatId) {
        var msg = SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(buildMainMenu())
                .text("Dialog done")
                .build();
        return new HandlerResult(msg);
    }

    public static HandlerResult msgSearchResult(Long chatId, Integer threadId, List<CallSignModel> list) {
        String text = list.isEmpty() ? "Nothing found" : parseList(list);
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
                Can't find any info about [%s]
                Maybe he don't registered in bot or has hidden username...
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
        var payload = String.format("Use this command like %s@username", Command.K2_INFO.value());
        var msg = SendMessage.builder()
                .chatId(chatId)
                .messageThreadId(threadId)
                .parseMode(ParseMode.HTML)
                .replyMarkup(buildMainMenu())
                .text(payload)
                .build();
        return new HandlerResult(msg);
    }

    public static String parseList(List<CallSignModel> list) {
        StringBuilder text = new StringBuilder(String.format("Found %s members:\n\n", list.size()));
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
        return String.format("""
                        <b>Username</b>: %s
                        <b>K2CallSign</b>: %s
                        <b>OfficialCallSign</b>: %s
                        <b>QTH</b>: %s
                        <b>DMR_ID</b>: %s""",
                Objects.isNull(callSignModel.getUserName()) ? "hidden" : "@" + callSignModel.getUserName(),
                callSignModel.getK2CallSign(),
                callSignModel.getOfficialCallSign(),
                callSignModel.getQth(),
                callSignModel.getDmrId()
        );
    }

}
