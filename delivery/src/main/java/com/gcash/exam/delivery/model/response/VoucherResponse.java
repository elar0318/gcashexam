package com.gcash.exam.delivery.model.response;

import java.util.List;
import java.util.Optional;

public record VoucherResponse(String voucherCode, double discount, String expiry) {
    public static List<VoucherResponse> getDefaultVouchers() {
        // Creating an immutable list with default voucher values
        return List.of(
                new VoucherResponse("MYNT", 15, "2025-12-30"),
                new VoucherResponse("GFI", 10, "2025-12-30"),
                new VoucherResponse("skdlks", 5, "2025-12-30"),
                new VoucherResponse("expired", 10, "2024-12-30")
        );
    }

    public static Optional<VoucherResponse> getValidVoucherDiscount(String inputVoucherCode) {
        return getDefaultVouchers().stream()
                .filter(voucher -> voucher.voucherCode().equals(inputVoucherCode)) // Match voucher code
                .findFirst(); // Return the first match (if any)
    }
}
