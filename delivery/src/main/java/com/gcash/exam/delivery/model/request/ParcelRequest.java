package com.gcash.exam.delivery.model.request;

public record ParcelRequest(double weight, double height, double width, double length, String voucher) {
    // No need for getter methods or constructors – automatically provided by the record
}