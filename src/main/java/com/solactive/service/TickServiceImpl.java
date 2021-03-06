package com.solactive.service;

import com.solactive.model.Tick;
import com.solactive.model.TickStatistics;
import com.solactive.repository.StatisticsHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

/**
 * created by farhad (farhad.yousefi@outlook.com) on 3/5/2021 AD
 */
@Service
@Slf4j
public class TickServiceImpl implements TickService {
    private static final Logger LOGGER = LogManager.getLogger(TickServiceImpl.class);

    private static final long BASE_TIME = 60000;

    @Autowired
    private StatisticsHandler statisticsHandler;

    @Override
    public Mono<Tick> saveTick(Tick tick) {
        LOGGER.info("Saving new tick with instrument if is not expired:{}", tick.getInstrument());
        Long nowInSecond = ZonedDateTime.now().toInstant().toEpochMilli();

        if (nowInSecond - tick.getTimestamp() > BASE_TIME)
            return Mono.empty();

        statisticsHandler.storeTick(tick);
        return Mono.just(tick);
    }

    @Override
    public Mono<TickStatistics> getLatestMinStatisticsOfAllTicks() {
        return statisticsHandler.getLatestMinStatisticsOfAllTicks(BASE_TIME);
    }

    @Override
    public Mono<TickStatistics> getLatestMinStatisticsOfInstrument(String instrument) {
        return statisticsHandler.getLatestMinStatisticsOfInstrument(BASE_TIME, instrument);
    }
}
