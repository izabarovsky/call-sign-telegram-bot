package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.service.CallSignModel;
import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgSearchResult;

public class PerformSearchAction implements Handler<Update, HandlerResult> {
    private final CallSignService callSignService;

    public PerformSearchAction(CallSignService callSignService) {
        this.callSignService = callSignService;
    }

    @Override
    public HandlerResult handle(Update payload) {
        var chatId = payload.getMessage().getChatId();
        var text = payload.getMessage().getText();
        List<CallSignModel> k2 = callSignService.findByK2PartialCallSign(text);
        List<CallSignModel> official = callSignService.findByOfficialPartialCallSign(text);
        List<CallSignModel> all = new ArrayList<>(k2);
        all.addAll(official);
        return msgSearchResult(chatId, payload.getMessage().getMessageThreadId(), all);
    }

}
