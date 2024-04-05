package com.izabarovsky.callsign.telegram.bot.service;

import com.izabarovsky.callsign.telegram.bot.persistence.entity.CallSignEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CallSignMapper {

    @Mapping(target = "creationTimestamp", ignore = true)
    @Mapping(target = "modificationTimestamp", ignore = true)
    CallSignEntity callSignModelToEntity(CallSignModel model);

    CallSignModel callSignEntityToModel(CallSignEntity entity);

}
