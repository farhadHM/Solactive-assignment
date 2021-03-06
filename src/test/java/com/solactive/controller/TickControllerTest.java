package com.solactive.controller;

import com.solactive.model.Tick;
import com.solactive.model.TickStatistics;
import com.solactive.service.TickService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

import static org.mockito.Mockito.doReturn;

/**
 * created by farhad (farhad.yousefi@outlook.com) on 3/6/2021 AD
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@AutoConfigureWebTestClient
@Slf4j
public class TickControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TickService tickService;

    @BeforeEach
    public void setup() {
    }

    @Test
    @DisplayName("Test if tick added successfully")
    public void testCreateTick() {
        Tick tick = new Tick("SOLACTIVE", 14873.82, ZonedDateTime.now().toInstant().toEpochMilli());
        doReturn(Mono.just(tick)).when(tickService).saveTick(tick);

        webTestClient.post().uri("/ticks").contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(tick), Tick.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Void.class);
    }

    @Test
    @DisplayName("Test if expired tick not added")
    public void testCreateExpiredTick() {
        Tick tick = new Tick("IBM.N", 90.76, ZonedDateTime.now().toInstant().toEpochMilli() - 60001);
        doReturn(Mono.empty()).when(tickService).saveTick(tick);

        webTestClient.post().uri("/ticks").contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(tick), Tick.class)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class);
    }

    @Test
    @DisplayName("Test if last minute statistics of all tick work correctly")
    public void testGetLatestMinStatisticsOfAllTicks() {
        TickStatistics mockTickStatistics = new TickStatistics(12.00, 4.06, 42.430, 12);
        doReturn(Mono.just(mockTickStatistics)).when(tickService).getLatestMinStatisticsOfAllTicks();

        webTestClient.get().uri("/ticks/statistics").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.avg").isEqualTo(12.00)
                .jsonPath("$.min").isEqualTo(4.06)
                .jsonPath("$.max").isEqualTo(42.430)
                .jsonPath("$.count").isEqualTo(12);
    }

    @Test
    @DisplayName("Test if last minute statistics of all tick work correctly")
    public void testGetLatestMinStatisticsOfInstrument() {
        TickStatistics mockTickStatistics = new TickStatistics(21.00, 3.60, 24.40, 9);
        doReturn(Mono.just(mockTickStatistics)).when(tickService).getLatestMinStatisticsOfInstrument(ArgumentMatchers.anyString());

        webTestClient.get().uri("/ticks/statistics/IBM").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.avg").isEqualTo(21.00)
                .jsonPath("$.min").isEqualTo(3.60)
                .jsonPath("$.max").isEqualTo(24.40)
                .jsonPath("$.count").isEqualTo(9);
    }


}
