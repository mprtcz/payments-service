package com.mprtcz.validator.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Setter
@Component
@ConfigurationProperties(prefix = "validation-rules")
public class ValidationRulesProperties {
    private List<String> blacklistedAccounts = new ArrayList<>();

    public List<String> getBlacklistedAccounts() {
        return new ArrayList<>(blacklistedAccounts);
    }
}
