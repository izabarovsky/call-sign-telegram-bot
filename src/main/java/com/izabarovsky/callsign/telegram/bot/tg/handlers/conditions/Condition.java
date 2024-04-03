package com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions;

public interface Condition<T> {

    boolean check(T t);

}
