package com.gcash.exam.delivery.service;

import com.gcash.exam.delivery.configuration.RuleConfiguration;
import com.gcash.exam.delivery.model.request.ParcelRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CalculateServiceImplTest {

    @Autowired
    private CalculateServiceImpl calculateService;

    @Autowired
    private RuleConfiguration ruleConfiguration;

    private ParcelRequest validParcelRequest;
    private ParcelRequest overweightParcelRequest;
    private ParcelRequest smallVolumeParcelRequest;
    private ParcelRequest mediumVolumeParcelRequest;

    @BeforeEach
    void setUp() {
        // Prepare test data for different scenarios
        validParcelRequest = new ParcelRequest(11.0, 50.0, 30.0, 20.0, "");
        overweightParcelRequest = new ParcelRequest(60.0, 50.0, 30.0, 20.0, "");
        smallVolumeParcelRequest = new ParcelRequest(5.0, 5.0, 5.0, 5.0, "");
        mediumVolumeParcelRequest = new ParcelRequest(5.0, 15, 10, 15, "");
    }

    @Test
    void testCalculate_RejectsOverweightParcel() {
        String result = calculateService.calculate(overweightParcelRequest);

        assertEquals("Rejected. Weight exceeds the maximum allowed", result);
    }

    @Test
    void testCalculate_ValidParcelWithWeightCost() {
        String result = calculateService.calculate(validParcelRequest);

        double expectedWeightCost = 20.0 * 11.0;
        double expectedVolumeCost = 0.05 * (50.0 * 30.0 * 20.0);
        double expectedTotalCost = expectedWeightCost + expectedVolumeCost;

        assertEquals("TOTAL COST: PHP " + expectedTotalCost, result);
    }

    @Test
    void testCalculate_SmallVolumeParcel() {
        String result = calculateService.calculate(smallVolumeParcelRequest);

        double expectedVolumeCost = 0.03 * (5.0 * 5.0 * 5.0);
        assertEquals("TOTAL COST: PHP " + expectedVolumeCost, result);
    }

    @Test
    void testCalculate_ValidParcelWithMediumVolume() {
        String result = calculateService.calculate(mediumVolumeParcelRequest);

        double expectedVolumeCost = 0.04 * (15 * 10 * 15);
        double expectedWeightCost = 0;
        double expectedTotalCost = expectedWeightCost + expectedVolumeCost;

        assertEquals("TOTAL COST: PHP " + expectedTotalCost, result);
    }
}