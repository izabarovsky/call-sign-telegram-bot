package com.izabarovsky.callsign.telegram.bot.service;

import com.izabarovsky.callsign.telegram.bot.persistence.entity.CallSignEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface CallSignMapper {

    @Mapping(target = "creationTimestamp", ignore = true)
    @Mapping(target = "modificationTimestamp", ignore = true)
    CallSignEntity callSignModelToEntity(CallSignModel model);

    CallSignModel callSignEntityToModel(CallSignEntity entity);

    default Instant map(Timestamp timestamp) {
        return Objects.isNull(timestamp) ? null : timestamp.toInstant();
    }

}
