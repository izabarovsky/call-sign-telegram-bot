package com.izabarovsky.callsign.telegram.bot.service;

import com.izabarovsky.callsign.telegram.bot.persistence.CallSignRepository;
import com.izabarovsky.callsign.telegram.bot.persistence.entity.CallSignEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class CallSignService {
    private final CallSignRepository callSignRepository;
    private final CallSignMapper mapper;

    public CallSignService(CallSignRepository callSignRepository, CallSignMapper mapper) {
        this.callSignRepository = callSignRepository;
        this.mapper = mapper;
    }

    public Optional<CallSignModel> getCallSign(Long tgId) {
        Optional<CallSignEntity> result = callSignRepository.findByTgId(tgId);
        return result.map(mapper::callSignEntityToModel);
    }

    public Optional<CallSignModel> findByK2CallSign(String k2CallSign) {
        return callSignRepository.findByK2CallSign(k2CallSign).map(mapper::callSignEntityToModel);
    }

    public Optional<CallSignModel> findByOfficialSign(String officialCallSign) {
        return callSignRepository.findByOfficialCallSign(officialCallSign).map(mapper::callSignEntityToModel);
    }

    public Optional<CallSignModel> findByUsername(String username) {
        return callSignRepository.findByUserName(username).map(mapper::callSignEntityToModel);
    }

    public List<CallSignModel> findAll() {
        return callSignRepository.findAll()
                .stream()
                .map(mapper::callSignEntityToModel).toList();
    }

    public List<CallSignModel> findByK2PartialCallSign(String callSign) {
        return callSignRepository.findAll().stream()
                .filter(s -> s.getK2CallSign().toLowerCase().startsWith(callSign.toLowerCase()))
                .map(mapper::callSignEntityToModel)
                .toList();
    }

    public List<CallSignModel> findByOfficialPartialCallSign(String callSign) {
        return callSignRepository.findAll().stream()
                .filter(s -> Objects.nonNull(s.getOfficialCallSign()))
                .filter(s -> s.getOfficialCallSign().toLowerCase().startsWith(callSign.toLowerCase()))
                .map(mapper::callSignEntityToModel)
                .toList();
    }

    public Long save(@Validated CallSignModel callSignModel) {
        CallSignEntity temp = mapper.callSignModelToEntity(callSignModel);
        Optional<CallSignEntity> entity = callSignRepository.findByTgId(callSignModel.getTgId());
        entity.ifPresent(callSignEntity -> temp.setId(callSignEntity.getId()));
        return callSignRepository.save(temp).getId();
    }

}
