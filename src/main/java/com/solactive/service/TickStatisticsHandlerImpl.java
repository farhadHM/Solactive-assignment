package com.solactive.service;

import com.solactive.controller.TickController;
import com.solactive.model.InstrumentTicksStatistics;
import com.solactive.model.Tick;
import com.solactive.model.TickStatistics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

/**
 * created by farhad (farhad.yousefi@outlook.com) on 3/7/2021 AD
 */
@Component
public class TickStatisticsHandlerImpl implements TickStatisticsHandler {
    private static final Logger LOGGER = LogManager.getLogger(TickController.class);

    private void removeTick(Tick tick) {
        Object o = new Object();
        synchronized (o) {
            Tick nextTick = tick.getNext();
            Tick prevTick = tick.getPrev();
            nextTick.setPrev(tick.getPrev());
            prevTick.setNext(tick.getNext());
        }
    }

    private void addTick(Tick tick, InstrumentTicksStatistics instrumentTicksStatistics) {
        Object o = new Object();
        synchronized (o) {
            if (instrumentTicksStatistics.getFirst() == null) {
                tick.setPrev(null);
                tick.setNext(null);
                instrumentTicksStatistics.setFirst(tick);
                return;
            }

            Tick iterator = instrumentTicksStatistics.getFirst();

            while (iterator.getPrice() < tick.getPrice() && iterator.getNext() != null) {
                iterator = iterator.getNext();
            }

            if (iterator.getNext() == null) {

                if (iterator.getPrice() < tick.getPrice()) {
                    iterator.setNext(tick);
                    tick.setNext(null);
                    tick.setPrev(iterator);
                }
            }

            tick.setNext(iterator);
            tick.setPrev(iterator.getPrev());
            iterator.setPrev(tick);
        }
    }

    public InstrumentTicksStatistics storeTick(InstrumentTicksStatistics instrumentTicksStatistics, Tick tick) {
        addTick(tick, instrumentTicksStatistics);
        instrumentTicksStatistics.getAllTicks().add(tick);
        instrumentTicksStatistics.getStatistics().updateStatistics(tick);
        return instrumentTicksStatistics;
    }

    public TickStatistics getStatistics(InstrumentTicksStatistics instrumentTicksStatistics, long baseTime) {
        if (instrumentTicksStatistics.getAllTicks().size() == 0)
            return instrumentTicksStatistics.getStatistics();

        Tick tick = instrumentTicksStatistics.getAllTicks().getFirst();
        long now = ZonedDateTime.now().toInstant().toEpochMilli();

        while (tick.getTimestamp() < now - baseTime) {
            instrumentTicksStatistics.getAllTicks().removeFirst();
            instrumentTicksStatistics.getStatistics().refreshStatistics(tick);
            removeTick(tick);
            tick = instrumentTicksStatistics.getAllTicks().getFirst();
        }
        return instrumentTicksStatistics.getStatistics();
    }

}
