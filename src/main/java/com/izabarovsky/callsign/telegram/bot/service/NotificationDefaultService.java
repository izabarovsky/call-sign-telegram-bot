package com.izabarovsky.callsign.telegram.bot.service;

import com.izabarovsky.callsign.telegram.bot.persistence.entity.CallSignEntity;
import com.izabarovsky.callsign.telegram.bot.tg.BotConfig;
import com.izabarovsky.callsign.telegram.bot.tg.WebHookCallSignBot;
import org.springframework.stereotype.Component;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgCongratsDmrIdMsg;

@Component
public class NotificationDefaultService implements NotificationService {
    private final WebHookCallSignBot webHookCallSignBot;
    private final CallSignMapper mapper;
    private final BotConfig botConfig;

    public NotificationDefaultService(WebHookCallSignBot webHookCallSignBot, CallSignMapper mapper, BotConfig botConfig) {
        this.webHookCallSignBot = webHookCallSignBot;
        this.mapper = mapper;
        this.botConfig = botConfig;
    }

    public void send(CallSignEntity entity) {
        var k2CallSign = mapper.callSignEntityToModel(entity);
        var congrats = msgCongratsDmrIdMsg(botConfig.getChat(), botConfig.getThread(), k2CallSign);
        webHookCallSignBot.sendMessage(congrats);
    }

}
