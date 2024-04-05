package com.izabarovsky.callsign.telegram.bot.controller;

import com.izabarovsky.callsign.telegram.bot.tg.WebHookCallSignBot;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@RestController
@AllArgsConstructor
public class WebhookController {
    private final WebHookCallSignBot webHookCallSignBot;

    @PostMapping("/")
    public PartialBotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return webHookCallSignBot.onWebhookUpdateReceived(update);
    }

}
