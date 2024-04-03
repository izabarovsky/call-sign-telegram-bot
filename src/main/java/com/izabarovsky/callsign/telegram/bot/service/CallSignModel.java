package com.izabarovsky.callsign.telegram.bot.service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CallSignModel {
    private Long tgId;
    private String firstName;
    private String lastName;
    private String userName;
    private String k2CallSign;
    private String officialCallSign;
    private String qth;
    private String dmrId;
}
