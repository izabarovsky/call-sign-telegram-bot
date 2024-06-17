package com.izabarovsky.callsign.telegram.bot.tg.update;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateWrapper {
    Long getUserId();

    Long getChatId();

    Integer getThreadId();
    String getText();

    String getUsername();

    String getFirstName();

    String getLastName();

    Boolean isPrivate();

    Update getUpdate();
}
