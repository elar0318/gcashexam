package com.gcash.exam.delivery.controller;

import com.gcash.exam.delivery.model.request.ParcelRequest;
import com.gcash.exam.delivery.service.CalculateService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gcash/api/delivery")
public class DeliveryController {

    private static final Logger log = LoggerFactory.getLogger(DeliveryController.class);
    private static final String REJECTED = "REJECTED";

    private final CalculateService calculateService;

    @Autowired
    public DeliveryController(CalculateService calculateService) {
        this.calculateService = calculateService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<String> calculateDeliveryCost(@NonNull @RequestBody ParcelRequest request) {

        String finalCalculation = calculateService.calculate(request);

        if (finalCalculation.toUpperCase().contains(REJECTED)) {
            return new ResponseEntity<>(finalCalculation, HttpStatus.BAD_REQUEST);
        }

        // Return the response with the final cost
        return new ResponseEntity<>(finalCalculation, HttpStatus.OK);
    }
}