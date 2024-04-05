package com.izabarovsky.callsign.telegram.bot.tg;

public enum Command {
    START("/start"),
    MY_K2_INFO("/MyK2Info"),
    GET_ALL("/GetAll"),
    STATISTICS("/Statistics"),
    SEARCH("/Search"),
    EDIT("/Edit"),
    CREATE("/Create"),
    CANCEL("/Cancel"),
    SKIP("/Skip");

    private final String value;

    Command(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
