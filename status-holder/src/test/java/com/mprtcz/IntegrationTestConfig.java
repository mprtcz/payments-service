package com.mprtcz;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@ComponentScan(basePackages = "com.mprtcz")
@ActiveProfiles("it")
public class IntegrationTestConfig {
}
