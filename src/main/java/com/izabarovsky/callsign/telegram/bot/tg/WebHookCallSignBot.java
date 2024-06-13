package com.izabarovsky.callsign.telegram.bot.tg;

import com.izabarovsky.callsign.telegram.bot.tg.handlers.RootHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import static java.util.Objects.nonNull;

@Slf4j
@Component
public class WebHookCallSignBot extends SpringWebhookBot {
    private final BotConfig botConfig;
    private final RootHandler<Update, HandlerResult> handler;

    public WebHookCallSignBot(SetWebhook setWebhook, BotConfig botConfig, RootHandler<Update, HandlerResult> handler) {
        super(setWebhook, botConfig.getToken());
        this.botConfig = botConfig;
        this.handler = handler;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            update.getCallbackQuery();
        } else {
            if (nonNull(update.getMessage()) && isGroupMember(update.getMessage().getFrom().getId())) {
                var result = handler.handle(update);
                return handleResult(result);
            }
        }
        return null;
    }

    @Override
    public String getBotPath() {
        return botConfig.getHook();
    }

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    public void sendMessage(SendMessage message) {
        try {
            this.sendApiMethod(message);
        } catch (TelegramApiException e) {
            log.error("Error while send msg: {}", message.getText());
        }
    }

    private BotApiMethod<?> handleResult(HandlerResult result) {
        if (nonNull(result)) {
            if (nonNull(result.getConsumer())) result.getConsumer().accept(this);
            if (nonNull(result.getResponseMsg())) return result.getResponseMsg();
        }
        return null;
    }

    /**
     * Check if user is group member to restrict non-members use bot
     * Not moved outside because used api-call
     *
     * @param userId
     * @return
     */
    private boolean isGroupMember(Long userId) {
        String chatId = botConfig.getChat();
        BotApiMethod<ChatMember> getChatMember = GetChatMember.builder()
                .chatId(chatId)
                .userId(userId)
                .build();
        try {
            ChatMember chatMember = this.sendApiMethod(getChatMember);
            String status = chatMember.getStatus();
            return status.equals("administrator") || status.equals("member") || status.equals("creator");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
