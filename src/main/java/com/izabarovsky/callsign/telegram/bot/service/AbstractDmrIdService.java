package com.izabarovsky.callsign.telegram.bot.service;

import com.izabarovsky.callsign.telegram.bot.dmrid.QueryParams;
import com.izabarovsky.callsign.telegram.bot.dmrid.RadioIdClient;
import com.izabarovsky.callsign.telegram.bot.dmrid.ResultModel;
import com.izabarovsky.callsign.telegram.bot.persistence.CallSignRepository;
import com.izabarovsky.callsign.telegram.bot.persistence.IntegrationRepository;
import com.izabarovsky.callsign.telegram.bot.persistence.entity.CallSignEntity;
import com.izabarovsky.callsign.telegram.bot.persistence.entity.IntegrationEntity;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@AllArgsConstructor
public abstract class AbstractDmrIdService {
    protected final RadioIdClient radioIdClient;
    protected final CallSignRepository callSignRepository;
    protected final IntegrationRepository integrationRepository;
    protected final NotificationService notificationService;

    /**
     * Find callSigns with official callsign, but without dmrId
     * Save all of them to integration repository
     */
    protected void setUpTasks() {
        List<Long> scheduled = integrationRepository.findAll().stream()
                .map(s -> s.getCallSignEntity().getId())
                .toList();
        List<IntegrationEntity> tasks = callSignRepository.findAll().stream()
                .filter(s -> nonNull(s.getOfficialCallSign()) && isNull(s.getDmrId()))
                .filter(s -> !scheduled.contains(s.getId()))
                .map(mapCallSignToTask())
                .toList();
        integrationRepository.saveAll(tasks);
        log.info("Scheduled {} integration tasks", tasks.size());
    }

    public void executeTasks() {
        Predicate<IntegrationEntity> required = integrationEntity -> {
            Timestamp timeToRecall = Timestamp.from(Instant.now().minus(1, ChronoUnit.DAYS));
            return isNull(integrationEntity.getLastCallTimestamp()) ||
                    integrationEntity.getLastCallTimestamp().before(timeToRecall);
        };
        List<IntegrationEntity> tasks = integrationRepository.findAll().stream()
                .filter(required)
                .toList();
        log.info("Called executeTasks: {} tasks found", tasks.size());
        tasks.forEach(enrichDmrId());
    }

    /**
     * Consumer, contains integration logic
     * Perform request to radioid api
     * If id found, save it to my db
     * Else, just save to db last apicall timestamp
     *
     * @return consumer
     */
    protected Consumer<IntegrationEntity> enrichDmrId() {
        return integrationEntity -> {
            var callSign = integrationEntity.getCallSignEntity();
            var queryParams = new QueryParams(callSign.getOfficialCallSign());
            try {
                var response = radioIdClient.getDmrId(queryParams);
                Optional<String> id = extractDmrId(response);
                if (id.isPresent()) {
                    callSign.setDmrId(id.get());
                    callSign = callSignRepository.save(callSign);
                    integrationRepository.delete(integrationEntity);
                    log.info("{} dmrId saved success!", queryParams.getCallsign());
                    notificationService.send(callSign);
                }
            } catch (FeignException e) {
                log.warn("Exception call radioid ({}): {}", queryParams.getCallsign(), e.getMessage());
                integrationEntity.setLastCallTimestamp(Timestamp.from(Instant.now()));
                integrationRepository.save(integrationEntity);
            }
        };
    }

    protected Function<CallSignEntity, IntegrationEntity> mapCallSignToTask() {
        return callSignEntity -> {
            var task = new IntegrationEntity();
            task.setCallSignEntity(callSignEntity);
            return task;
        };
    }

    protected Optional<String> extractDmrId(ResultModel resultModel) {
        if (resultModel.getCount() == 1) {
            return Optional.of(resultModel.getResults().get(0).getId());
        }
        log.warn("Can't handle response: {}", resultModel);
        return Optional.empty();
    }
}
