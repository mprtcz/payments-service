package com.mprtcz.transaction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ClockConfig {

    @Bean
    public Clock getClock() {
        return Clock.systemDefaultZone();
    }
}
