package com.izabarovsky.callsign.telegram.bot.tg.update;

public interface UpdateWrapper {
    Long getUserId();

    Long getChatId();

    Integer getThreadId();

    String getUsername();

    String getFirstName();

    String getLastName();

    Boolean isPrivate();
}
