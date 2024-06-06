package com.izabarovsky.callsign.telegram.bot;

import com.izabarovsky.callsign.telegram.bot.persistence.CallSignRepository;
import com.izabarovsky.callsign.telegram.bot.service.CallSignMapper;
import com.izabarovsky.callsign.telegram.bot.tg.Command;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import com.izabarovsky.callsign.telegram.bot.tg.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.stream.Stream;

import static com.izabarovsky.callsign.telegram.bot.DataHelper.*;
import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.*;
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
        var threadId = (int) randomId();
        CallSignMapper mapper = Mappers.getMapper(CallSignMapper.class);
        var expected = parseCallSign(mapper.callSignEntityToModel(callSign));
        var cmd = Command.K2_INFO.value() + "@" + callSign.getUserName();
        var resultFromUserChat = handler.handle(updFromUser(callSign.getTgId(), chatId, cmd))
                .getResponseMsg();
        var resultFromGroupChat = handler.handle(updFromGroupChat(callSign.getTgId(), chatId, threadId, cmd))
                .getResponseMsg();
        assertAll(
                () -> assertEquals(expected, resultFromUserChat.getText(), "User chat"),
                () -> assertEquals(expected, resultFromGroupChat.getText(), "Group chat")
        );
    }

    @Test
    void shouldReturnMessageIfK2InfoNotFound() {
        var callSign = getExistsCallSign(repository);
        var chatId = randomId();
        var threadId = (int) randomId();
        var expected = TextUtils.textUserNotFound(callSign.getUserName());
        var cmd = Command.K2_INFO.value() + "@" + callSign.getUserName();
        var resultFromUserChat = handler.handle(updFromUser(callSign.getTgId(), chatId, cmd))
                .getResponseMsg();
        var resultFromGroupChat = handler.handle(updFromGroupChat(callSign.getTgId(), chatId, threadId, cmd))
                .getResponseMsg();
        assertAll(
                () -> assertEquals(expected, resultFromUserChat.getText(), "User chat"),
                () -> assertEquals(expected, resultFromGroupChat.getText(), "Group chat")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCommand")
    void shouldReturnMessageIfCommandInvalid(String cmd, String description) {
        var callSign = getExistsCallSign(repository);
        var chatId = randomId();
        var threadId = (int) randomId();
        var expected = TextUtils.textUseK2InfoCommandAsFollow();
        var resultFromUserChat = handler.handle(updFromUser(callSign.getTgId(), chatId, cmd))
                .getResponseMsg();
        var resultFromGroupChat = handler.handle(updFromGroupChat(callSign.getTgId(), chatId, threadId, cmd))
                .getResponseMsg();
        assertAll(
                () -> assertEquals(expected, resultFromUserChat.getText(), "User chat"),
                () -> assertEquals(expected, resultFromGroupChat.getText(), "Group chat")
        );
    }

    private static Stream<Arguments> provideInvalidCommand() {
        return Stream.of(
                Arguments.of(Command.K2_INFO.value() + "@", "NoUserName"),
                Arguments.of(Command.K2_INFO.value(), "NoCommandArg")
        );
    }

}
