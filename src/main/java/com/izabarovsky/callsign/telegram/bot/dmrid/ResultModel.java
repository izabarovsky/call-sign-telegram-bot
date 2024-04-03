package com.izabarovsky.callsign.telegram.bot.dmrid;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Jacksonized
@Data
@Builder
public class ResultModel {
    private int count;
    private List<DmrIdModel> results;
}
