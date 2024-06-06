package com.izabarovsky.callsign.telegram.bot.service;

import com.izabarovsky.callsign.telegram.bot.persistence.entity.CallSignEntity;

public interface NotificationService {

    void send(CallSignEntity entity);

}
