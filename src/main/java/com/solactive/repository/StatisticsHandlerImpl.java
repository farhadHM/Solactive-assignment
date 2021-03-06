package com.solactive.repository;

import com.solactive.controller.TickController;
import com.solactive.model.Tick;
import com.solactive.model.TickStatistics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * created by farhad (farhad.yousefi@outlook.com) on 3/5/2021 AD
 */
@Component
public class StatisticsHandlerImpl implements StatisticsHandler {
    private static final Logger LOGGER = LogManager.getLogger(TickController.class);

    private ConcurrentSkipListMap<Long, Tick> allTicks;
    private Map<String, ConcurrentSkipListMap<Long, Tick>> instrumentTicks;

    public StatisticsHandlerImpl() {
        this.allTicks = new ConcurrentSkipListMap<>();
        this.instrumentTicks = new ConcurrentHashMap<>();
    }

    public void storeTick(Tick tick) {
        storeAllTick(tick);
        storeInstrumentTick(tick);
    }

    private void storeAllTick(Tick tick) {
        allTicks.put(tick.getTimestamp(), tick);
        LOGGER.info("size of allTick: " + allTicks.size());
    }

    private void storeInstrumentTick(Tick tick) {
        String instrument = tick.getInstrument().trim();

        if (instrumentTicks.containsKey(instrument)) {
            instrumentTicks.get(instrument).put(tick.getTimestamp(), tick);
        } else {
            ConcurrentSkipListMap<Long, Tick> innerMap = new ConcurrentSkipListMap<>();
            innerMap.put(tick.getTimestamp(), tick);
            instrumentTicks.put(instrument, innerMap);
        }
        LOGGER.info("size of instrumentTicks: " + instrumentTicks.size());
    }

    public Mono<TickStatistics> getLatestMinStatisticsOfAllTicks(long baseTime) {
        return latestMinStatistics(allTicks, baseTime);
    }

    public Mono<TickStatistics> getLatestMinStatisticsOfInstrument(long baseTime, String instrument) {
        if (instrumentTicks.containsKey(instrument)) {
            ConcurrentSkipListMap<Long, Tick> innerMap = instrumentTicks.get(instrument);
            return latestMinStatistics(innerMap, baseTime);
        } else
            return Mono.empty();
    }

    @Override
    public void cleanStore() {
        this.allTicks.clear();
        this.instrumentTicks.clear();
    }

    private Mono<TickStatistics> latestMinStatistics(ConcurrentSkipListMap<Long, Tick> innerMap, long baseTime) {
        long nowInSecond = ZonedDateTime.now().toInstant().toEpochMilli();
        double[] prices = innerMap.tailMap(nowInSecond - baseTime, true).values()
                .stream().mapToDouble(Tick::getPrice).toArray();

        TickStatistics tickStatistics = new TickStatistics(0, 0, 0, 0);
        if (prices.length > 0) {
            double sum = 0;
            for (double price : prices) {
                sum += price;
            }

            tickStatistics = new TickStatistics(
                    sum / prices.length,
                    prices[0],
                    prices[prices.length - 1],
                    prices.length);
        }

        /*DoubleSummaryStatistics summaryStatistics = innerMap.headMap(nowInSecond - baseTime, true).values()
                .stream().mapToDouble(Tick::getPrice).summaryStatistics();

        TickStatistics tickStatistics = new TickStatistics(
                summaryStatistics.getAverage(),
                summaryStatistics.getMin(),
                summaryStatistics.getMax(),
                summaryStatistics.getCount()
        );*/

        return Mono.just(tickStatistics);
    }

}
