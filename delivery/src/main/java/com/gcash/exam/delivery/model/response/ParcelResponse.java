package com.gcash.exam.delivery.model.response;

public record ParcelResponse(
        double weight,
        double volume,
        double weightCost,
        double volumeCost,
        double totalCost
) {
}