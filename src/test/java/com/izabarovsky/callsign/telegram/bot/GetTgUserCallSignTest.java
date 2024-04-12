package com.izabarovsky.callsign.telegram.bot;

import com.izabarovsky.callsign.telegram.bot.persistence.CallSignRepository;
import com.izabarovsky.callsign.telegram.bot.tg.Command;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

import static com.izabarovsky.callsign.telegram.bot.DataHelper.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
public class GetTgUserCallSignTest {

    @Autowired
    private Handler<Update, HandlerResult> handler;
    @Autowired
    private CallSignRepository repository;

    @Test
    void shouldReturnK2InfoByUsername() {
        var callSign = getExistsCallSignWithUsername(repository);
        var chatId = randomId();
        var expected = String.format("""
                        <b>Username</b>: %s
                        <b>K2CallSign</b>: %s
                        <b>OfficialCallSign</b>: %s
                        <b>QTH</b>: %s
                        <b>DMR_ID</b>: %s""",
                Objects.isNull(callSign.getUserName()) ? "hidden" : "@" + callSign.getUserName(),
                callSign.getK2CallSign(),
                callSign.getOfficialCallSign(),
                callSign.getQth(),
                callSign.getDmrId()
        );
        var cmd = Command.K2_INFO.value() + "@" + callSign.getUserName();
        var result = handler.handle(updFromUser(callSign.getTgId(), chatId, cmd))
                .getResponseMsg();
        assertEquals(expected, result.getText());
    }

    @Test
    void shouldReturnMessageIfK2InfoNotFound() {
        var callSign = getExistsCallSign(repository);
        var chatId = randomId();
        var expected = String.format("""
                Can't find any info about [%s]
                Maybe he don't registered in bot or has hidden username...
                """, callSign.getUserName());
        var cmd = Command.K2_INFO.value() + "@" + callSign.getUserName();
        var result = handler.handle(updFromUser(callSign.getTgId(), chatId, cmd))
                .getResponseMsg();
        assertEquals(expected, result.getText());
    }

    @Test
    void shouldReturnMessageIfCommandNotContainsMention() {
        var callSign = getExistsCallSign(repository);
        var chatId = randomId();
        var expected = String.format("Use this command like %s@username", Command.K2_INFO.value());
        var cmdNoUserName = Command.K2_INFO.value() + "@";
        var cmdNoAtSign = Command.K2_INFO.value();

        var resultNoUserName = handler.handle(updFromUser(callSign.getTgId(), chatId, cmdNoUserName))
                .getResponseMsg();
        var resultNoAtSign = handler.handle(updFromUser(callSign.getTgId(), chatId, cmdNoAtSign))
                .getResponseMsg();

        assertAll(
                () -> assertEquals(expected, resultNoUserName.getText()),
                () -> assertEquals(expected, resultNoAtSign.getText())
        );

    }

}
