package com.solactive.service;

import com.solactive.model.InstrumentTicksStatistics;
import com.solactive.model.Tick;
import com.solactive.model.TickStatistics;

/**
 * created by farhad (farhad.yousefi@outlook.com) on 3/7/2021 AD
 */
public interface TickStatisticsHandler {
    InstrumentTicksStatistics storeTick(InstrumentTicksStatistics instrumentTicksStatistics, Tick tick);

    TickStatistics getStatistics(InstrumentTicksStatistics instrumentTicksStatistics, long baseTime);
}
