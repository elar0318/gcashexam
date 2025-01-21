package com.gcash.exam.delivery.service;

public interface VoucherService {
    double getVoucherDiscount(double totalCostBeforeDisc, String voucherCode);
}