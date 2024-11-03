package com.mprtcz.validator;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@ComponentScan(basePackages = "com.mprtcz.validator")
@ActiveProfiles("it")
public class IntegrationTestConfig {}
