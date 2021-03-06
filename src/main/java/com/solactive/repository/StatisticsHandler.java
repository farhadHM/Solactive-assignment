package com.solactive.repository;

import com.solactive.model.Tick;
import com.solactive.model.TickStatistics;
import reactor.core.publisher.Mono;

/**
 * created by farhad (farhad.yousefi@outlook.com) on 3/5/2021 AD
 */
public interface StatisticsHandler {
    void storeTick(Tick tick);

    Mono<TickStatistics> getLatestMinStatisticsOfAllTicks(long baseTime);

    Mono<TickStatistics> getLatestMinStatisticsOfInstrument(long baseTime, String instrument);

    void cleanStore();

}
