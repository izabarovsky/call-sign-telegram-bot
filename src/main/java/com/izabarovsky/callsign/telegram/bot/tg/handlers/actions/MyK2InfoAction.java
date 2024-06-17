package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.service.CallSignModel;
import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;

import java.util.Optional;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgMyK2Info;
import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgNewcomer;

public class MyK2InfoAction implements Handler<UpdateWrapper, HandlerResult> {
    private final CallSignService callSignService;

    public MyK2InfoAction(CallSignService callSignService) {
        this.callSignService = callSignService;
    }

    @Override
    public HandlerResult handle(UpdateWrapper payload) {
        var chatId = payload.getChatId();
        var threadId = payload.getThreadId();
        var tgId = payload.getUserId();

        Optional<CallSignModel> callSignModel = callSignService.getCallSign(tgId);
        return callSignModel.map(signModel -> msgMyK2Info(chatId, threadId, signModel))
                .orElseGet(() -> msgNewcomer(chatId, threadId, payload.getUsername()));
    }
}
