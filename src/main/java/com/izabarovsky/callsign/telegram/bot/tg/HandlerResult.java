package com.izabarovsky.callsign.telegram.bot.tg;

import lombok.Data;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.function.Consumer;

@Data
public class HandlerResult {

    private SendMessage responseMsg;
    private Consumer<DefaultAbsSender> consumer;

    public HandlerResult(SendMessage sendMessage) {
        this.responseMsg = sendMessage;
    }

}
