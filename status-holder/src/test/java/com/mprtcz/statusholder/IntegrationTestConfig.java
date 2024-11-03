package com.mprtcz.statusholder;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@ComponentScan(basePackages = "com.mprtcz.statusholder")
@ActiveProfiles("it")
public class IntegrationTestConfig {
}
