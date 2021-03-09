package com.solactive.service;

import com.solactive.model.InstrumentTicksStatistics;
import com.solactive.model.Tick;
import com.solactive.model.TickStatistics;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;

/**
 * created by farhad (farhad.yousefi@outlook.com) on 3/9/2021 AD
 */
@SpringBootTest
public class TickStatisticsHandlerTest {

    private static InstrumentTicksStatistics instrumentTicksStatistics;
    private static int i = 0;
    private final static long BASE_TIME = 60000;

    @Autowired
    TickStatisticsHandler tickStatisticsHandler;

    private void init() {
        if (instrumentTicksStatistics == null)
            instrumentTicksStatistics = new InstrumentTicksStatistics();
    }

    @ParameterizedTest
    @CsvSource({"SOLACTIVE,2.00", "ISC,2.00", "ISC,4.00"})
    @DisplayName("Test store tick service")
    public void storeTickTest(String instrument, double price) throws InterruptedException {
        InstrumentTicksStatistics its = addingTicks(instrument, price);
        Assertions.assertEquals(++i, its.getAllTicks().size());
    }

    private InstrumentTicksStatistics addingTicks(String instrument, double price) throws InterruptedException {
        Thread.sleep(1);
        init();
        Tick tick = new Tick(instrument, price, ZonedDateTime.now().toInstant().toEpochMilli());
        InstrumentTicksStatistics its = tickStatisticsHandler.storeTick(instrumentTicksStatistics, tick);
        return its;
    }

    @ParameterizedTest
    @CsvSource({"SOLACTIVE,2.00", "ISC,2.00", "ISC,4.00"})
    @DisplayName("Test store tick service")
    public void getStatisticsTest(String instrument, double price) throws InterruptedException {
        if (instrumentTicksStatistics == null)
            instrumentTicksStatistics = addingTicks(instrument, price);

        TickStatistics expected = new TickStatistics(3.0, 2.0, 4.0, 3);
        TickStatistics actual = tickStatisticsHandler.getStatistics(instrumentTicksStatistics, BASE_TIME);
        Assertions.assertEquals(expected.toString(), (actual.toString()));
    }


}
