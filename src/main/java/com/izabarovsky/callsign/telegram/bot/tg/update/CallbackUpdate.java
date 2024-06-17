package com.izabarovsky.callsign.telegram.bot.tg.update;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.telegram.telegrambots.meta.api.objects.Update;

@AllArgsConstructor
public class CallbackUpdate implements UpdateWrapper {
    private final Update update;

    @Override
    public Long getUserId() {
        throw new NotImplementedException();
    }

    @Override
    public Long getChatId() {
        throw new NotImplementedException();
    }

    @Override
    public Integer getThreadId() {
        throw new NotImplementedException();
    }

    @Override
    public String getUsername() {
        throw new NotImplementedException();
    }

    @Override
    public String getFirstName() {
        throw new NotImplementedException();
    }

    @Override
    public String getLastName() {
        throw new NotImplementedException();
    }

    @Override
    public Boolean isPrivate() {
        throw new NotImplementedException();
    }
}
