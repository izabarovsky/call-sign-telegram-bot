package com.izabarovsky.callsign.telegram.bot.dmrid;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Data
public class DmrIdModel {
    private String callsign;
    private String city;
    private String country;
    private String fname;
    private String id;
    private String remarks;
    private String state;
    private String surname;
}
