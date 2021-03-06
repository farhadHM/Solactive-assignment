package com.solactive.repository;

import com.solactive.model.Tick;
import com.solactive.model.TickStatistics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.ZonedDateTime;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * created by farhad (farhad.yousefi@outlook.com) on 3/5/2021 AD
 */
@SpringBootTest
public class StatisticsTest {
    private static final long BASE_TIME = 60000;
    @Autowired
    private StatisticsHandler statisticsHandler;

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        String[] instruments = new String[]{"SOLACTIVE", "SOLACTIVE", "ORACLE", "SOLACTIVE", "SOLACTIVE", "BSM"};
        IntStream.rangeClosed(1, 6).forEach(e -> {
            statisticsHandler.storeTick(
                    new Tick(instruments[e - 1],
                            100.00 * e,
                            ZonedDateTime.now().toInstant().toEpochMilli() + e)
            );
        });
    }

    @AfterEach
    public void clean() throws NoSuchFieldException, IllegalAccessException {
        statisticsHandler.cleanStore();
    }

    @Test
    public void getLatestMinStatisticsOfAllTicksTest() {
        Mono<TickStatistics> result = statisticsHandler.getLatestMinStatisticsOfAllTicks(BASE_TIME);
        TickStatistics ts = new TickStatistics(350.00, 100.00, 600.00, 6);
        StepVerifier.create(result).consumeNextWith(e -> {
            assertEquals(ts, e);
        }).verifyComplete();
    }

    @Test
    public void getLatestMinStatisticsOfInstrumentTest() {
        Mono<TickStatistics> result = statisticsHandler.getLatestMinStatisticsOfInstrument(BASE_TIME, "SOLACTIVE");
        TickStatistics ts = new TickStatistics(300.00, 100.00, 500.00, 4);
        StepVerifier.create(result).consumeNextWith(e -> {
            assertEquals(ts, e);
        }).verifyComplete();
    }


}
