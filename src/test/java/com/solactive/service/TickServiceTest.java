package com.solactive.service;

import com.solactive.model.Tick;
import com.solactive.repository.StatisticsHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

/**
 * created by farhad (farhad.yousefi@outlook.com) on 3/5/2021 AD
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TickServiceTest {
    @Autowired
    private TickService tickService;

    @MockBean
    private StatisticsHandler statisticsHandler;

    @ParameterizedTest
    @CsvSource({"SOLACTIVE,14873.82", "ISC,345.9", "IBM,23.3"})
    @DisplayName("Test if tick added successfully")
    public void testCreateTick(String instrument, double price) {
        Tick tick = new Tick(instrument, price, ZonedDateTime.now().toInstant().toEpochMilli());
        doNothing().when(statisticsHandler).storeTick(tick);
        Mono<Tick> result = tickService.saveTick(tick);

        StepVerifier.create(result).consumeNextWith(r -> {
            assertEquals(tick, r);
        }).verifyComplete();
    }

    @ParameterizedTest
    @CsvSource({"SOLACTIVE,14873.82", "ISC,345.9", "IBM,23.3"})
    @DisplayName("Test if expired tick not add to map")
    public void testExpiredTick(String instrument, double price) {
        Tick tick = new Tick(instrument, price, ZonedDateTime.now().toInstant().toEpochMilli() - 60001);
        doNothing().when(statisticsHandler).storeTick(tick);
        Mono<Tick> result = tickService.saveTick(tick);

        StepVerifier.create(result).expectNextCount(0).verifyComplete();
    }
}
