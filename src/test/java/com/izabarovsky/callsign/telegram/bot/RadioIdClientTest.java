package com.izabarovsky.callsign.telegram.bot;

import com.izabarovsky.callsign.telegram.bot.dmrid.QueryParams;
import com.izabarovsky.callsign.telegram.bot.dmrid.RadioIdClient;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@EnableFeignClients
@Import({FeignAutoConfiguration.class, JacksonAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class})
@SpringBootTest(classes = RadioIdClient.class)
public class RadioIdClientTest {

    @Autowired
    private RadioIdClient radioIdClient;

    @Test
    void dmrIdPresent() {
        var result = radioIdClient.getDmrId(new QueryParams("UT3UUG"));
        assertEquals(1, result.getCount());
    }

    @Test
    void dmrIdNotPresent() {
        Executable call = () -> radioIdClient.getDmrId(new QueryParams("UT2XX"));
        assertThrows(FeignException.class, call);
    }

}
