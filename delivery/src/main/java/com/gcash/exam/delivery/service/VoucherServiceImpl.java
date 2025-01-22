package com.gcash.exam.delivery.service;

import com.gcash.exam.delivery.model.response.VoucherResponse;
import com.gcash.exam.delivery.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class VoucherServiceImpl implements VoucherService {

    private static final Logger log = LoggerFactory.getLogger(VoucherServiceImpl.class);

    // External MYNT API endpoint for fetching voucher data
    private static final String VOUCHER_API_URL = "https://mynt-exam.mocklab.io/voucher/{voucherCode}";

    // External MYNT API endpoint for fetching voucher data
    public double getVoucherDiscount(double totalCostBeforeDisc, String voucherCode) {

        RestTemplate restTemplate = new RestTemplate();

        // Build the URI for the external API request
        String url = UriComponentsBuilder.fromHttpUrl(VOUCHER_API_URL)
                .queryParam("key", "apikey")
                .toUriString();

        log.info("Calling outbound API...");

        // Send GET request to the external voucher API
        VoucherResponse response = restTemplate.getForObject(url, VoucherResponse.class, voucherCode);

        log.info("Success response from outbound API...");

        // Check if the voucher was found and not expired then return the discount rate
        if (!ObjectUtils.isEmpty(response) && !DateUtil.isExpired(response.expiry())) {
            log.info("Voucher found and not expired...Applying discount...");
            return applyDiscount(totalCostBeforeDisc, response.discount());
        } else {
            log.info("Voucher expired");
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
