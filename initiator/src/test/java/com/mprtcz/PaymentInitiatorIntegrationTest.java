package com.mprtcz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mprtcz.controller.PaymentStatusController;
import com.mprtcz.dto.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
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

    @Test
    void testPaymentStatus_success() throws Exception {
        Mockito.when(
                paymentStatusController.getPaymentStatus(any(String.class)))
            .thenReturn(PaymentStatus.STARTED);

        var url = "/v1/payment/status/123";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
            .andExpect(status().isOk())
            .andExpect(content().json("{}"));
    }

    @Test
    void testPaymentInitiator_success() throws Exception {
        var uuid = "f042cedd-f424-44fe-b856-8d3958082e0f";
        Mockito.when(paymentStatusController.createPaymentStatus())
            .thenReturn(uuid);

        var url = "/v1/payment/initiate";
        mockMvc.perform(MockMvcRequestBuilders.post(url).content("""
                {
                    "paymentRequesterAccountNumber": "123456789012345678",
                    "paymentDestinationAccountNumber": "876543210987654321",
                    "amount": 1
                }
                """).contentType("application/json"))
            .andExpect(status().isAccepted())
            .andExpect(content().json(String.format("{id: %s}", uuid)));
    }

}