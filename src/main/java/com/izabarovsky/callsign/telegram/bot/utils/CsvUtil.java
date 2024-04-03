package com.izabarovsky.callsign.telegram.bot.utils;

import com.izabarovsky.callsign.telegram.bot.service.CallSignModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

@Component
public class CsvUtil {
    private static final String HEADERS = "K2CallSign,OfficialCallSign,QTH,DMR_ID\n";
    private static final String SEPARATOR = ",";

    public String generateCsv(List<CallSignModel> callSigns) {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(HEADERS);
        Consumer<CallSignModel> consumer = callSignModel -> csvContent
                .append("\"").append(callSignModel.getK2CallSign()).append("\"").append(SEPARATOR)
                .append("\"").append(callSignModel.getOfficialCallSign()).append("\"").append(SEPARATOR)
                .append("\"").append(callSignModel.getQth()).append("\"").append(SEPARATOR)
                .append("\"").append(callSignModel.getDmrId()).append("\"").append("\n");
        callSigns.forEach(consumer);
        return csvContent.toString();
    }

}
