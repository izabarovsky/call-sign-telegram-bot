package com.izabarovsky.callsign.telegram.bot.persistence;

import com.izabarovsky.callsign.telegram.bot.persistence.entity.IntegrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntegrationRepository extends JpaRepository<IntegrationEntity, Long> {

}
