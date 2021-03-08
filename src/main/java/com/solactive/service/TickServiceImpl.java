package com.solactive.service;

import com.solactive.model.InstrumentTicksStatistics;
import com.solactive.model.Tick;
import com.solactive.model.TickStatistics;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.concurrent.ConcurrentHashMap;

/**
 * created by farhad (farhad.yousefi@outlook.com) on 3/5/2021 AD
 */
@Service
@Slf4j
public class TickServiceImpl implements TickService {
    private static final Logger LOGGER = LogManager.getLogger(TickServiceImpl.class);

    private static final long BASE_TIME = 990000;
    private ConcurrentHashMap<String, InstrumentTicksStatistics> map = new ConcurrentHashMap<>();

    @Autowired
    private TickStatisticsHandler tickHandler;

    @Override
    public Mono<Tick> saveTick(Tick tick) {
        LOGGER.info("Saving new tick with instrument if is not expired:{}", tick.getInstrument());
        Long now = ZonedDateTime.now().toInstant().toEpochMilli();

        if (now - tick.getTimestamp() > BASE_TIME)
            return Mono.empty();

        if (map.containsKey(tick.getInstrument())) {
            map.replace(tick.getInstrument(), tickHandler.storeTick(map.get(tick.getInstrument()), tick));
        } else {
            map.put(tick.getInstrument(), tickHandler.storeTick(new InstrumentTicksStatistics(), tick));
        }
        return Mono.just(tick);
    }

    @Override
    public Mono<TickStatistics> getLatestMinStatisticsOfAllTicks() {
        TickStatistics result = new TickStatistics(0, Double.MAX_VALUE, Double.MIN_VALUE, 0);
        map.entrySet().stream().forEach((e) -> {
            TickStatistics ts = tickHandler.getStatistics(map.get(e.getKey()), BASE_TIME);
            result.setMin(ts.getMin() < result.getMin() ? ts.getMin() : result.getMin());
            result.setMax(ts.getMax() > result.getMax() ? ts.getMax() : result.getMax());
            result.setAvg((ts.getAvg() + result.getAvg()) / 2);
            result.setCount(ts.getCount() + result.getCount());

        });
        return Mono.just(result);
    }

    @Override
    public Mono<TickStatistics> getLatestMinStatisticsOfInstrument(String instrument) {
        return Mono.just(tickHandler.getStatistics(map.get(instrument), BASE_TIME));
    }
}
