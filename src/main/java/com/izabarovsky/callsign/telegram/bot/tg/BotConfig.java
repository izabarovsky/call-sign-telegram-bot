package com.izabarovsky.callsign.telegram.bot.tg;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("bot")
@Getter
@Setter
public class BotConfig {
    private String name;
    private String token;
    private String chat;
    private String thread;
    private String api;
    private String hook;
}
