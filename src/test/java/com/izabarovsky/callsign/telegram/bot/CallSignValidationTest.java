package com.izabarovsky.callsign.telegram.bot;

import com.izabarovsky.callsign.telegram.bot.persistence.CallSignRepository;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogState;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.izabarovsky.callsign.telegram.bot.DataHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CallSignValidationTest {
    @Autowired
    private Handler<Update, HandlerResult> handler;
    @Autowired
    private DialogStateService dialogStateService;
    @Autowired
    private CallSignRepository repository;

    @Test
    void k2CallSignConflict() {
        var exists = getExistsCallSign(repository).getK2CallSign();
        var chatId = randomId();
        var tgId = randomId();
        dialogStateService.putState(tgId, DialogState.EXPECT_UNOFFICIAL);
        var msg = handler.handle(updFromUser(tgId, chatId, exists)).getResponseMsg();
        var expected = String.format("CallSign %s is booked! Please, take another one!", exists);
        assertEquals(expected, msg.getText());
        assertEquals(String.valueOf(chatId), msg.getChatId(), "Response to chatId");
    }

    @Test
    void officialCallSignConflict() {
        var exists = getExistsCallSign(repository).getOfficialCallSign();
        var chatId = randomId();
        var tgId = randomId();
        dialogStateService.putState(tgId, DialogState.EXPECT_OFFICIAL);
        var msg = handler.handle(updFromUser(tgId, chatId, exists)).getResponseMsg();
        var expected = String.format("CallSign %s is booked! Please, take another one!", exists);
        assertEquals(expected, msg.getText());
        assertEquals(String.valueOf(chatId), msg.getChatId(), "Response to chatId");
    }

    @Test
    void officialCallSignValidation() {
        var chatId = randomId();
        var tgId = randomId();
        dialogStateService.putState(tgId, DialogState.EXPECT_OFFICIAL);
        var msg = handler.handle(updFromUser(tgId, chatId, RandomStringUtils.randomAlphabetic(5)))
                .getResponseMsg();
        var expected = "CallSign invalid! Must match pattern [2 LETTER][DIGIT][2 or 3 LETTER]. Example: UT3UUU";
        assertEquals(expected, msg.getText());
        assertEquals(String.valueOf(chatId), msg.getChatId(), "Response to chatId");
    }

}
