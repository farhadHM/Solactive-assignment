package com.solactive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * created by farhad (farhad.yousefi@outlook.com) on 3/6/2021 AD
 */
@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        http.authorizeExchange()
                .pathMatchers("/**").permitAll()
                .anyExchange().authenticated();

        http.httpBasic().disable()
                .formLogin().disable()
                .logout().disable().csrf().disable();

        return http.build();
    }
}
