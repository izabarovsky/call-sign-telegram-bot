package com.izabarovsky.callsign.telegram.bot.persistence;

import com.izabarovsky.callsign.telegram.bot.persistence.entity.CallSignEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CallSignRepository extends JpaRepository<CallSignEntity, Long> {

    Optional<CallSignEntity> findByTgId(Long tgId);

    Optional<CallSignEntity> findByUserName(String username);

    @Query("select u from CallSignEntity u where lower(u.k2CallSign) = lower(?1)")
    Optional<CallSignEntity> findByK2CallSign(String k2CallSign);

    @Query("select u from CallSignEntity u where lower(u.officialCallSign) = lower(?1)")
    Optional<CallSignEntity> findByOfficialCallSign(String officialCallSign);

}
