package com.solactive.controller;

import com.solactive.model.Tick;
import com.solactive.model.TickStatistics;
import com.solactive.service.TickService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * created by farhad (farhad.yousefi@outlook.com) on 3/5/2021 AD
 */
@RestController
@RequestMapping("/ticks")
public class TickController {
    private static final Logger LOGGER = LogManager.getLogger(TickController.class);

    @Autowired
    TickService tickService;

    /**
     * last 60 second statistics of all instruments
     *
     * @return statistics(average, min, max, count) of ticks for all instruments
     */
    @GetMapping("/statistics")
    public Mono<TickStatistics> getLastMinStatisticsOfAllTicks() {
        return tickService.getLatestMinStatisticsOfAllTicks();
    }

    /**
     * last 60 second statistics of input instrument
     *
     * @param instrument to get its last minute statistics
     * @return statistics(average, min, max, count) of ticks for input instrument
     */
    @GetMapping("/statistics/{instrument}")
    public Mono<TickStatistics> getLatestMinStatisticsOfInstrument(@PathVariable String instrument) {
        return tickService.getLatestMinStatisticsOfInstrument(instrument);
    }

    /**
     * Saves a new tick
     *
     * @param tick to save
     * @return ResponseEntity with no body
     */
    @PostMapping()
    @ResponseStatus
    public Mono<ResponseEntity> saveTick(@Validated @RequestBody Tick tick) {
        LOGGER.info("start to Adding new tick with instrument:{} if is not expired", tick.getInstrument());
        Mono<Tick> result = tickService.saveTick(tick);

        return result.map(e -> ResponseEntity.status(HttpStatus.CREATED).body(null)).cast(ResponseEntity.class)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NO_CONTENT).body(null));
    }

}
