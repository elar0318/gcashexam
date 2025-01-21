package com.gcash.exam.delivery.service;

import com.gcash.exam.delivery.model.request.ParcelRequest;

public interface CalculateService {
    String calculate(ParcelRequest request);
}