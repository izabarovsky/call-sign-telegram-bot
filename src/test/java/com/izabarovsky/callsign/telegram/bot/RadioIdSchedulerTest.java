package com.izabarovsky.callsign.telegram.bot;

import com.izabarovsky.callsign.telegram.bot.dmrid.RadioIdClient;
import com.izabarovsky.callsign.telegram.bot.persistence.CallSignRepository;
import com.izabarovsky.callsign.telegram.bot.persistence.IntegrationRepository;
import com.izabarovsky.callsign.telegram.bot.persistence.entity.CallSignEntity;
import com.izabarovsky.callsign.telegram.bot.persistence.entity.IntegrationEntity;
import com.izabarovsky.callsign.telegram.bot.service.AbstractDmrIdService;
import com.izabarovsky.callsign.telegram.bot.service.CallSignMapper;
import com.izabarovsky.callsign.telegram.bot.service.NotificationDefaultService;
import com.izabarovsky.callsign.telegram.bot.service.NotificationService;
import com.izabarovsky.callsign.telegram.bot.tg.BotConfig;
import com.izabarovsky.callsign.telegram.bot.tg.WebHookCallSignBot;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.function.Predicate;

import static com.izabarovsky.callsign.telegram.bot.DataHelper.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Disabled
public class RadioIdSchedulerTest {

    @Autowired
    private CallSignRepository callSignRepository;
    @Autowired
    private IntegrationRepository integrationRepository;
    @Autowired
    private RadioIdClient radioIdClient;
    @Autowired
    private NotificationService testNotificationService = new TestNotificationService();

    @Test
    void shouldScheduleIfOfficialExistsButNoDmrId() {
        var service = new TestDmrIdService(radioIdClient, callSignRepository, integrationRepository);
        var callSignValidToIntegrate = callSignWithOfficialWithoutDmrId();
        service.setUpTasks();
        List<IntegrationEntity> tasks = integrationRepository.findAll();
        assertTrue(isScheduled(tasks, callSignValidToIntegrate), "Should scheduled");
    }

    @Test
    void shouldNotScheduleIfDmrIdExists() {
        var service = new TestDmrIdService(radioIdClient, callSignRepository, integrationRepository);
        var callSignInvalidToIntegrate = callSignWithOfficialWithDmrId();
        service.setUpTasks();
        List<IntegrationEntity> tasks = integrationRepository.findAll();
        assertFalse(isScheduled(tasks, callSignInvalidToIntegrate), "Should not scheduled");
    }

    @Test
    void shouldNotScheduleTwice() {
        var service = new TestDmrIdService(radioIdClient, callSignRepository, integrationRepository);
        var callSignValidToIntegrate = callSignWithOfficialWithoutDmrId();
        service.setUpTasks();
        service.setUpTasks();
        List<IntegrationEntity> tasks = integrationRepository.findAll();
        assertEquals(1, tasks.stream().filter(byCallSign(callSignValidToIntegrate)).count());
    }

    class TestDmrIdService extends AbstractDmrIdService {

        public TestDmrIdService(RadioIdClient radioIdClient,
                                CallSignRepository callSignRepository,
                                IntegrationRepository integrationRepository) {
            super(radioIdClient, callSignRepository, integrationRepository, testNotificationService);
        }

        public void setUpTasks() {
            super.setUpTasks();
        }

    }

    class TestNotificationService implements NotificationService {
        @Override
        public void send(CallSignEntity entity) {
            log.info("Send notify...");
        }
    }

    private boolean isScheduled(List<IntegrationEntity> tasks, CallSignEntity callSign) {
        return tasks.stream()
                .anyMatch(byCallSign(callSign));
    }

    private Predicate<IntegrationEntity> byCallSign(CallSignEntity callSign) {
        return t -> t.getCallSignEntity().getId().equals(callSign.getId());
    }

    private CallSignEntity callSignWithOfficialWithoutDmrId() {
        CallSignEntity entity = new CallSignEntity();
        entity.setTgId(randomId());
        entity.setOfficialCallSign(officialCallSign());
        entity.setK2CallSign(k2CallSign());
        return callSignRepository.saveAndFlush(entity);
    }

    private CallSignEntity callSignWithOfficialWithDmrId() {
        CallSignEntity entity = new CallSignEntity();
        entity.setTgId(randomId());
        entity.setOfficialCallSign(officialCallSign());
        entity.setK2CallSign(k2CallSign());
        entity.setDmrId(dmrId());
        return callSignRepository.saveAndFlush(entity);
    }

}
