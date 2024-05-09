package com.izabarovsky.callsign.telegram.bot.service;

import com.izabarovsky.callsign.telegram.bot.dmrid.RadioIdClient;
import com.izabarovsky.callsign.telegram.bot.persistence.CallSignRepository;
import com.izabarovsky.callsign.telegram.bot.persistence.IntegrationRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DmrIdService extends AbstractDmrIdService {

    public DmrIdService(RadioIdClient radioIdClient,
                        CallSignRepository callSignRepository,
                        IntegrationRepository integrationRepository,
                        NotificationDefaultService notificationService
    ) {
        super(radioIdClient, callSignRepository, integrationRepository, notificationService);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onSchedule() {
        setUpTasks();
        executeTasks();
    }

}
