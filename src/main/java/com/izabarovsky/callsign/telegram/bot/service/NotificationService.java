package com.izabarovsky.callsign.telegram.bot.service;

import com.izabarovsky.callsign.telegram.bot.persistence.entity.CallSignEntity;
import com.izabarovsky.callsign.telegram.bot.tg.BotConfig;
import com.izabarovsky.callsign.telegram.bot.tg.WebHookCallSignBot;
import org.springframework.stereotype.Component;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.congratsDmrIdMsg;

public interface NotificationService {

    void send(CallSignEntity entity);

}
