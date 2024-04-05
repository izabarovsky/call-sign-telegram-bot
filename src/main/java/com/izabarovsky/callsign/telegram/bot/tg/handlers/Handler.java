package com.izabarovsky.callsign.telegram.bot.tg.handlers;

public interface Handler<T, S> {
    S handle(T payload);

}
