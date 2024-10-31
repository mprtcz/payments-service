package com.mprtcz.payments.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class OpenApiConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry viewControllerRegistry) {
        viewControllerRegistry.addRedirectViewController("/", "/swagger-ui/index.html");
        viewControllerRegistry.addRedirectViewController("/v1/api-docs", "/swagger-ui/index.html");
    }

    @Bean
    public GroupedOpenApi paymentsApi() {
        return GroupedOpenApi.builder().group("payments").pathsToMatch("/v1/payment/**").build();
    }

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info().title("Payments Service"));
    }
}