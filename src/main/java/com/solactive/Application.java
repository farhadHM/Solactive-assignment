package com.solactive;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

import java.time.ZonedDateTime;

/**
 * created by farhad (farhad.yousefi@outlook.com) on 3/5/2021 AD
 */
@SpringBootApplication
@EnableWebFlux
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("current time:"+ ZonedDateTime.now().toInstant().toEpochMilli());
    }
}