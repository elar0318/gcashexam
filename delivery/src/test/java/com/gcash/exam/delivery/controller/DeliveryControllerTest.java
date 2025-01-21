package com.gcash.exam.delivery.controller;

import com.gcash.exam.delivery.model.request.ParcelRequest;
import com.gcash.exam.delivery.service.CalculateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DeliveryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CalculateService calculateService;

    @InjectMocks
    private DeliveryController deliveryController;

    private ParcelRequest validParcelRequest;
    private ParcelRequest invalidParcelRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(deliveryController).build();

        // Sample requests
        validParcelRequest = new ParcelRequest(10.0, 50.0, 30.0, 20.0, "");  // Valid request
        invalidParcelRequest = new ParcelRequest(60.0, 50.0, 30.0, 20.0, "");  // Invalid request (Reject case)
    }

    @Test
    void testCalculateDeliveryCost_RejectsInvalidParcel() throws Exception {
        String rejectedResult = "REJECTED: Weight exceeds limit.";
        when(calculateService.calculate(any(ParcelRequest.class))).thenReturn(rejectedResult);

        mockMvc.perform(post("/gcash/api/delivery/calculate")
                        .contentType("application/json")
                        .content("{ \"weight\": 60.0, \"height\": 50.0, \"width\": 30.0, \"length\": 20.0 }"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(rejectedResult));
    }

    @Test
    void testCalculateDeliveryCost_ValidParcel() throws Exception {
        String validCalculation = "Final cost: 200.0";
        when(calculateService.calculate(any(ParcelRequest.class))).thenReturn(validCalculation);

        mockMvc.perform(post("/gcash/api/delivery/calculate")
                        .contentType("application/json")
                        .content("{ \"weight\": 10.0, \"height\": 50.0, \"width\": 30.0, \"length\": 20.0 }"))
                .andExpect(status().isOk())
                .andExpect(content().string(validCalculation));
    }
}
