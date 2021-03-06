package com.solactive.service;

import com.solactive.model.Tick;
import com.solactive.model.TickStatistics;
import reactor.core.publisher.Mono;

/**
 * created by farhad (farhad.yousefi@outlook.com) on 3/5/2021 AD
 */
public interface TickService {
    Mono<Tick> saveTick(Tick tick);

    Mono<TickStatistics> getLatestMinStatisticsOfAllTicks();

    Mono<TickStatistics> getLatestMinStatisticsOfInstrument(String instrument);
}
