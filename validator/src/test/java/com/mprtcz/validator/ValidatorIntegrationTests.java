package com.mprtcz.validator;

import com.mprtcz.validator.controller.ValidationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@EnableConfigurationProperties
@ContextConfiguration(classes = {IntegrationTestConfig.class})
class ValidatorIntegrationTests {

    @Autowired
    private ValidationController validationController;

    @Test
    void shouldReturnTrueForCorrectData() {
        var notBlacklistedAccountNumber1 = "12345678900987654";
        var notBlacklistedAccountNumber2 = "12345678900987655";

        assertTrue(validationController.validate(notBlacklistedAccountNumber1,
            notBlacklistedAccountNumber2
        ));
    }

    @Test
    void shouldReturnFalseForBlacklistedAccountNumber() {
        var notBlacklistedAccountNumber = "12345678900987654";
        var blacklistedAccountNumber = "123456789012345678";

        assertFalse(validationController.validate(notBlacklistedAccountNumber,
            blacklistedAccountNumber
        ));
    }

    @Test
    void shouldReturnFalseForAccountNumbersInDifferentCountries() {
        var notBlacklistedAccountNumber = "12345678900987654";
        var blacklistedAccountNumber = "21345678900987654";

        assertFalse(validationController.validate(notBlacklistedAccountNumber,
            blacklistedAccountNumber
        ));
    }
}
