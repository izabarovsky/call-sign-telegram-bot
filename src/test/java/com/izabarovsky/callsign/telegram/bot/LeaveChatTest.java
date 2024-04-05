package com.izabarovsky.callsign.telegram.bot;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.groupadministration.LeaveChat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class LeaveChatTest {
    private static final String botName = "";
    private static final String botToken = "";
    private static final String chatToLeave = "";

    /**
     * Used manually to leave any groups created while developing process
     */
    @Disabled
    @Test
    void leaveChat() {
        LeaveBot testBot = new LeaveBot(botToken);
        testBot.leaveGroup(chatToLeave);
    }

    static class LeaveBot extends TelegramLongPollingBot {

        public LeaveBot(String botToken) {
            super(botToken);
        }

        @Override
        public void onUpdateReceived(Update update) {
            log.info("Update: {}", update);
        }

        @Override
        public String getBotUsername() {
            return botName;
        }

        public void leaveGroup(String chatId) {
            BotApiMethod<?> leaveChat = LeaveChat.builder()
                    .chatId(chatId)
                    .build();
            try {
                this.sendApiMethod(leaveChat);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        }
    }

}
