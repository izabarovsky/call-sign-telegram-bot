package com.izabarovsky.callsign.telegram.bot.tg;

public enum Command {
    START("/start"),
    MY_K2_INFO("/MyK2Info"),
    K2_INFO("/K2Info"),
    GET_ALL("/GetAll"),
    STATISTICS("/Statistics"),
    SEARCH("/Search"),
    EDIT("/Edit"),
    CREATE("/Create"),
    CANCEL("/Cancel"),
    SKIP("/Skip"),
    REPEATERS("/Repeaters"),
    OFFICIAL("/Official"),
    NONOFFICIAL("/NonOfficial"),
    ECHOLINK("/Echolink");



    private final String value;

    Command(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
