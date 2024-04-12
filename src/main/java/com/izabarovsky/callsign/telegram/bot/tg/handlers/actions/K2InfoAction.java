package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.service.CallSignModel;
import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import com.izabarovsky.callsign.telegram.bot.tg.Command;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;
import java.util.regex.Pattern;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgK2Info;
import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgK2InfoNotFound;

public class K2InfoAction implements Handler<Update, HandlerResult> {
    private final CallSignService callSignService;
    private final String pattern = "^" + Command.K2_INFO.value() + "@(.+)";

    public K2InfoAction(CallSignService callSignService) {
        this.callSignService = callSignService;
    }

    @Override
    public HandlerResult handle(Update payload) {
        var chatId = payload.getMessage().getChatId();
        var threadId = payload.getMessage().getMessageThreadId();
        var tgId = payload.getMessage().getFrom().getId();
        var matcher = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)
                .matcher(payload.getMessage().getText());
        if (matcher.find()) {
            String username = matcher.group(1);
            Optional<CallSignModel> callSignModel = callSignService.findByUsername(username);
            return callSignModel.map(signModel -> msgK2Info(chatId, threadId, signModel))
                    .orElseGet(() -> msgK2InfoNotFound(chatId, threadId, username));
        } else {
            return null;
        }
    }

}
