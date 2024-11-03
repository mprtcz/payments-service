package com.mprtcz.initiator;

import com.mprtcz.statusholder.controller.PaymentStatusController;
import com.mprtcz.validator.controller.ValidationController;
import com.mprtcz.statusholder.dto.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig
@WebMvcTest
@ContextConfiguration(classes = IntegrationTestConfig.class)
class PaymentInitiatorIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PaymentStatusController paymentStatusController;
    @Autowired
    private ValidationController validationController;

    @BeforeEach
    public void setup() {
        when(validationController.validate(any(), any())).thenReturn(true);
    }

    @Test
    void testPaymentStatus_success() throws Exception {
        when(paymentStatusController.getPaymentStatus(any(String.class))).thenReturn(
                PaymentStatus.STARTED);

        var url = "/v1/payment/status/123";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
    }

    @Test
    void testPaymentInitiator_success() throws Exception {
        var uuid = "f042cedd-f424-44fe-b856-8d3958082e0f";
        when(paymentStatusController.createPaymentStatus()).thenReturn(uuid);

        var url = "/v1/payment/initiate";
        mockMvc.perform(MockMvcRequestBuilders.post(url).content("""
                        {
                            "paymentRequesterAccountNumber": "123456789012345678",
                            "paymentDestinationAccountNumber": "123456789012345679",
                            "amount": 1
                        }
                        """).contentType("application/json"))
                .andExpect(status().isAccepted())
                .andExpect(content().json(String.format("{id: %s}", uuid)));
    }

    @Test
    void testPaymentInitiator_badRequest() throws Exception {
        var uuid = "f042cedd-f424-44fe-b856-8d3958082e0f";
        when(paymentStatusController.createPaymentStatus()).thenReturn(uuid);

        var url = "/v1/payment/initiate";
        mockMvc.perform(MockMvcRequestBuilders.post(url).content("""
                        {
                            "paymentRequesterAccountNumber": "123456789012345678",
                            "paymentDestinationAccountNumber": "123456789012345678",
                            "amount": 1
                        }
                        """).contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPaymentInitiator_badRequest1() throws Exception {
        var uuid = "f042cedd-f424-44fe-b856-8d3958082e0f";
        when(validationController.validate(any(), any())).thenReturn(false);
        when(paymentStatusController.createPaymentStatus()).thenReturn(uuid);

        var url = "/v1/payment/initiate";
        mockMvc.perform(MockMvcRequestBuilders.post(url).content("""
                        {
                            "paymentRequesterAccountNumber": "123456789012345678",
                            "paymentDestinationAccountNumber": "123456789012345679",
                            "amount": 1
                        }
                        """).contentType("application/json"))
                .andExpect(status().isBadRequest());
        verify(paymentStatusController).markTransactionAsInvalid(uuid);
    }
}