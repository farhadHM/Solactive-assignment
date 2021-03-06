package com.solactive.repository;

import com.solactive.model.Tick;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * created by farhad (farhad.yousefi@outlook.com) on 3/5/2021 AD
 */
@SpringBootTest
public class StatisticsHandlerTest {

    @Autowired
    private StatisticsHandler statisticsHandler;

    @ParameterizedTest
    @CsvSource({"SOLACTIVE,14873.82", "ISC,345.9", "IBM,23.3"})
    @DisplayName("Test store tick service")
    public void storeTickTest(String instrument, double price) throws NoSuchFieldException {
        Tick tick = new Tick(instrument, price, ZonedDateTime.now().toInstant().toEpochMilli());
        statisticsHandler.storeTick(tick);

        //check if allTicks filed has value or not
        Field field = StatisticsHandlerImpl.class.getDeclaredField("allTicks");
        field.setAccessible(true);
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        Type value = type.getActualTypeArguments()[1];
        assertEquals(value, Tick.class);
    }

}
