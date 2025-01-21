package com.gcash.exam.delivery.service;

import com.gcash.exam.delivery.controller.DeliveryController;
import com.gcash.exam.delivery.model.response.VoucherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class VoucherServiceImpl implements VoucherService {

    private static final Logger log = LoggerFactory.getLogger(DeliveryController.class);

    // External MYNT API endpoint for fetching voucher data

    public double getVoucherDiscount(double totalCostBeforeDisc, String voucherCode) {

        log.info("Calling outbound API...");

        VoucherResponse response = new VoucherResponse("MYNT", 5, "2020-01-30");

        log.info("Success response from outbound API...");

        // Check if the voucher was found and return the discount rate
        if (!ObjectUtils.isEmpty(response)) {
            return applyDiscount(totalCostBeforeDisc, response.discount());
        } else {
            return totalCostBeforeDisc;
        }
    }

    public double applyDiscount(double totalCostBeforeDisc, double discountPercentage) {
        log.info("Cost before discount: {}", totalCostBeforeDisc);
        log.info("Discount percentage: {}", discountPercentage);
        log.info("Discounted price: {}", totalCostBeforeDisc * (1 - (discountPercentage / 100.0)));
        return totalCostBeforeDisc * (1 - (discountPercentage / 100.0));
    }

}
