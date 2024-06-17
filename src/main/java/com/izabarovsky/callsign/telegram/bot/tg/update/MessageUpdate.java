package com.izabarovsky.callsign.telegram.bot.tg.update;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

@AllArgsConstructor
public class MessageUpdate implements UpdateWrapper {
    private final Update update;

    @Override
    public Long getUserId() {
        return update.getMessage().getFrom().getId();
    }

    @Override
    public Long getChatId() {
        return update.getMessage().getChatId();
    }

    @Override
    public Integer getThreadId() {
        return update.getMessage().getMessageThreadId();
    }

    @Override
    public String getText() {
        return update.getMessage().getText();
    }

    @Override
    public String getUsername() {
        return update.getMessage().getFrom().getUserName();
    }

    @Override
    public String getFirstName() {
        return update.getMessage().getFrom().getFirstName();
    }

    @Override
    public String getLastName() {
        return update.getMessage().getFrom().getLastName();
    }

    @Override
    public Boolean isPrivate() {
        return update.getMessage().getChat().isUserChat();
    }

    @Override
    public Update getUpdate() {
        return update;
    }
}
