package com.izabarovsky.callsign.telegram.bot.dmrid;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class QueryParams {
    private List<String> callsign;

    public QueryParams(String callsign) {
        this.callsign = Collections.singletonList(callsign);
    }

    public QueryParams(List<String> callsigns) {
        this.callsign = callsigns;
    }

}
