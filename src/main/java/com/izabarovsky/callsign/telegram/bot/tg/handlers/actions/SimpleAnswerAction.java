package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.answerRepeatersNonOfficial;
import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgOnGetAll;

@AllArgsConstructor
public class SimpleAnswerAction implements Handler<UpdateWrapper, HandlerResult> {
    private final Function<String, AnswerCallbackQuery> message;

    @Override
    public HandlerResult handle(UpdateWrapper payload) {
        HandlerResult handlerResult = new HandlerResult(null);
        Consumer<DefaultAbsSender> consumer = defaultAbsSender -> {
            try {
                String id = payload.getUpdate().getCallbackQuery().getId();
                defaultAbsSender.execute(message.apply(id));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        };
        handlerResult.setConsumer(consumer);
        return handlerResult;
    }

}
