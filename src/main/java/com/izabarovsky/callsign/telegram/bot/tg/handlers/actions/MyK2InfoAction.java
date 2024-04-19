package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.service.CallSignModel;
import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgMyK2Info;
import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgNewcomer;

public class MyK2InfoAction implements Handler<Update, HandlerResult> {
    private final CallSignService callSignService;

    public MyK2InfoAction(CallSignService callSignService) {
        this.callSignService = callSignService;
    }

    @Override
    public HandlerResult handle(Update payload) {
        var chatId = payload.getMessage().getChatId();
        var threadId = payload.getMessage().getMessageThreadId();
        var tgId = payload.getMessage().getFrom().getId();

        Optional<CallSignModel> callSignModel = callSignService.getCallSign(tgId);
        return callSignModel.map(signModel -> msgMyK2Info(chatId, threadId, signModel))
                .orElseGet(() -> msgNewcomer(chatId, threadId, payload.getMessage().getFrom().getUserName()));
    }
}
