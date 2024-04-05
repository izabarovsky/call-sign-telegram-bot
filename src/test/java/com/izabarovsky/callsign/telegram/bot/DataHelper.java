package com.izabarovsky.callsign.telegram.bot;

import com.izabarovsky.callsign.telegram.bot.persistence.CallSignRepository;
import com.izabarovsky.callsign.telegram.bot.persistence.entity.CallSignEntity;
import com.izabarovsky.callsign.telegram.bot.tg.Command;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Random;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.apache.commons.lang3.RandomUtils.nextInt;

public class DataHelper {

    public static Update updFromGroupChat(long userId, long chatId, int threadId, String text) {
        var update = updFromUser(userId, chatId, text);
        update.getMessage().getChat().setType("supergroup");
        update.getMessage().setMessageThreadId(threadId);
        return update;
    }

    public static Update updFromUser(long userId, long chatId, Command command) {
        return updFromUser(userId, chatId, command.value());
    }

    public static Update updFromUser(long userId, long chatId, String text) {
        Update update = new Update();
        Message message = new Message();
        message.setText(text);
        Chat chat = new Chat();
        chat.setId(chatId);
        chat.setType("private");
        message.setChat(chat);
        User user = new User();
        user.setId(userId);
        message.setFrom(user);
        update.setMessage(message);
        return update;
    }

    public static String k2CallSign() {
        return randomAlphabetic(10);
    }

    public static String officialCallSign() {
        var callSign = randomAlphabetic(2) + nextInt(1, 10) + randomAlphabetic(3);
        return callSign.toUpperCase();
    }

    public static String dmrId() {
        return randomNumeric(6);
    }

    public static long randomId() {
        return new Random().nextLong();
    }

    public static CallSignEntity getExistsCallSign(CallSignRepository repository) {
        CallSignEntity entity = new CallSignEntity();
        entity.setTgId(randomId());
        entity.setOfficialCallSign(officialCallSign());
        entity.setK2CallSign(k2CallSign());
        entity = repository.saveAndFlush(entity);
        return entity;
    }

}
