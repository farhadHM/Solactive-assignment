package com.solactive.model;

import lombok.Data;
import lombok.ToString;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * created by farhad (farhad.yousefi@outlook.com) on 3/8/2021 AD
 */
@Data
@ToString
public class InstrumentTicksStatistics {
    private ConcurrentLinkedDeque<Tick> allTicks;
    private TickStatistics statistics;
    private Tick first;

    public InstrumentTicksStatistics() {
        allTicks = new ConcurrentLinkedDeque();
        statistics = new TickStatistics();
        first = null;
    }

}
