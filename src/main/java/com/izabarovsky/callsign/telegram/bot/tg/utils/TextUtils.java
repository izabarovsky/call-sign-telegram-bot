package com.izabarovsky.callsign.telegram.bot.tg.utils;

import com.izabarovsky.callsign.telegram.bot.service.CallSignModel;
import com.izabarovsky.callsign.telegram.bot.service.StatisticsModel;
import com.izabarovsky.callsign.telegram.bot.tg.Command;

import java.util.Objects;

public class TextUtils {
    public static String textOnNewDmrId(CallSignModel k2CallSign) {
        return String.format("""
                        %s
                        Комм'юніті К2 поздоровляє %s [%s]
                        з отриманням DMRID [%s]!
                        Ласкаво просимо в цифру!""",
                Objects.isNull(k2CallSign.getUserName()) ? "hidden" : "@" + k2CallSign.getUserName(),
                k2CallSign.getK2CallSign(),
                k2CallSign.getOfficialCallSign(),
                k2CallSign.getDmrId()
        );
    }

    public static String textCallSingIsInvalid() {
        return """
                Позивний невалідний!
                Має відповідати паттерну [2 LETTER][DIGIT][2 or 3 LETTER]
                Якщо ще не маєш офіційного позивного, просто тисни Skip""";
    }

    public static String textCallSingIsBooked(String callSign) {
        return String.format("Позивний %s вже зайнятий!", callSign);
    }

    public static String textK2CallSignRequired() {
        return "Придумай свій позивний для репітера К2. Це обов'язково!";
    }

    public static String textUseMenuButtons() {
        return "Використовуй кнопки меню";
    }

    public static String textHelloNewcomer(String userName) {
        return String.format("""
                Вітаю, @%s! Схоже ти ще не зареєстроаний. Давай зареєструємо твій позивний К2!
                Клікай сюди @K2CallSignBot
                """, Objects.nonNull(userName) ? userName : "[чел з прихованим username:)]");
    }

    public static String textEnterValueOrSkip(String payload) {
        return String.format("Вкажи свій %s. Або пропусти (Skip)", payload);
    }

    public static String textDialogDone() {
        return "Діалог завершено";
    }

    public static String textEnterSearch() {
        return "Введи позивний або частину позивного, спробую знайти цього учасника";
    }

    public static String textNothingFound() {
        return "Нічого не знайдено";
    }

    public static String textStepCantSkip() {
        return "Цей крок не можна пропустити!";
    }

    public static String textUseK2InfoCommandAsFollow() {
        return String.format("Використовуй команду так: %s@username", Command.K2_INFO.value());
    }

    public static String textUserNotFound(String username) {
        return String.format("""
                Учасника [%s] не знайдено
                Можливо він не реєструвався...
                """, username);
    }

    public static String textStatistics(StatisticsModel statisticsModel) {
        return String.format("""
                        <b>Зареєстровано через бот</b>: %s
                        <b>Мають офіційний позивний</b>: %s
                        <b>Не мають офіційного</b>: %s
                        <b>Мають DMR_ID</b>: %s
                        """,
                statisticsModel.getTotal(),
                statisticsModel.getOfficial(),
                statisticsModel.getNonOfficial(),
                statisticsModel.getDmr()
        );
    }

    public static String textRepeatersPrivate() {
        return "Репітери Києва 🎙️";
    }

    public static String textRepeatersGroup() {
        return String.format("""
                        <b>Репітери Києва 🎙️</b>
                        Офіційні -> %s
                        Неофіційні -> %s
                        Папуги -> %s
                        Ехолінк -> %s
                        """,
                Command.OFFICIAL.value(),
                Command.NONOFFICIAL.value(),
                Command.PARROTS.value(),
                Command.ECHOLINK.value());
    }

    public static String textRepeatersNonOfficial() {
        return """
                🏴‍☠️ Можна без офіційного позивного,
                дотримуючись етики!
                            
                <b>Kyiv-1</b>
                RX 446.225 / TX 434.850 (offset: -11.375)
                CTCSS: 88.5Hz
                QTH - ТРЦ Апрель
                Icom IC-F211
                Босс Система
                                
                <b>Kyiv-2 🔋</b>
                ⚠️ Тимчасово не працює 🤞
                RX 446.150 / TX 434.950 (offset -11.2)
                CTCSS: 74.4Hz
                QTH - Батиєва гора
                435.375 - канал для прямих зв'язків
                Vertex Standard VXR-9000EU 40W
                Босс 131й
                """;
    }

    public static String textRepeatersOfficial() {
        return """
                <b>Тільки з офіційним позивним!</b>
                                
                <b>R3 🔋</b>
                RX 145.675 / TX 145.075 (offset: -0.6)
                CTCSS: 88.5Hz
                                
                <b>R76 🔋</b>
                RX 438.800 / TX 431.200 (offset -7.6)
                CTCSS: 88.5Hz
                QTH - Бровари
                                
                <b>R81</b>
                RX 438.925 / TX 431.325 (offset -7.6)
                CTCSS: 88.5Hz
                QTH - Кловський узвіз
                                
                <b>R85 🔋</b>
                RX 439.025 / TX 431.425 (offset -7.6)
                Analog
                CTCSS: 88.5Hz
                Digital
                TS: 1 CC: 1
                TG: 25501 (Kyiv)

                <b>R100 (DMR)</b>
                RX 439.400 / TX 431.800 (offset -7.6)
                TS: 1 CC: 1
                TG: 25501 (Kyiv)
                """;
    }

    public static String textParrots() {
        return """
                🦜 🦜 🦜
                <b>Brovary Parrot</b>
                RX/TX 436.700
                CTSS: 71.9Hz
                QTH - Бровари
                                
                <b>WhiteChurch Parrot</b>
                RX/TX 145.400
                QTH - Біла Церква
                                
                <b>Parrot R92</b>
                RX/TX 439.200
                CTSS: 88.5
                """;
    }

    public static String textRepeatersEcholink() {
        return """
                <b>Echolink</b>
                RX/TX 438.375
                CTSS: 123.0Hz
                QTH - Борщагівка
                """;
    }
}
