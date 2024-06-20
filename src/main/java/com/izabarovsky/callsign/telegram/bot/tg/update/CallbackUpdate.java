package com.izabarovsky.callsign.telegram.bot.tg.update;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@AllArgsConstructor
public class CallbackUpdate implements UpdateWrapper {
    private final Update update;

    @Override
    public Long getUserId() {
        return update.getCallbackQuery().getFrom().getId();
    }

    @Override
    public Long getChatId() {
        return update.getCallbackQuery().getMessage().getChatId();
    }

    @Override
    public Integer getThreadId() {
        return null;
    }

    @Override
    public String getText() {
        return update.getCallbackQuery().getData();
    }

    @Override
    public String getUsername() {
        return update.getCallbackQuery().getFrom().getUserName();
    }

    @Override
    public String getFirstName() {
        return update.getCallbackQuery().getFrom().getFirstName();
    }

    @Override
    public String getLastName() {
        return update.getCallbackQuery().getFrom().getLastName();
    }

    @Override
    public Boolean isPrivate() {
        return update.getCallbackQuery().getMessage().isUserMessage();
    }

    @Override
    public Update getUpdate() {
        return update;
    }
}
