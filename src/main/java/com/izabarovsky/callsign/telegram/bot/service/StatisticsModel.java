package com.izabarovsky.callsign.telegram.bot.service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatisticsModel {
    private Long total;
    private Long official;
    private Long dmr;
    private Long nonOfficial;
}
