package com.gcash.exam.delivery.service;

import com.gcash.exam.delivery.configuration.RuleConfiguration;
import com.gcash.exam.delivery.model.request.ParcelRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CalculateServiceImpl implements CalculateService {

    private static final Logger log = LoggerFactory.getLogger(CalculateServiceImpl.class);

    private static final String WEIGHT_EXCEED = "Rejected. Weight exceeds the maximum allowed";
    private static final String TOTAL_STRING = "TOTAL COST: PHP ";

    private final RuleConfiguration ruleConfiguration;
    private final VoucherService voucherService;

    public CalculateServiceImpl(RuleConfiguration ruleConfiguration, VoucherService voucherService) {
        this.ruleConfiguration = ruleConfiguration;
        this.voucherService = voucherService;
    }

    public String calculate(ParcelRequest request) {

        // FIRST rule exceeds specified weight in config file
        if (request.weight() > ruleConfiguration.getReject().weight()) {
            log.info("Reject weight exceeds {} kg", ruleConfiguration.getReject().weight());
            return WEIGHT_EXCEED;
        }

        // Calculate the volume
        double volume = request.height() * request.width() * request.length();  // cm³

        // SECOND weight rule exceeds specified weight in config file, but not rejected
        double weightCost = 0;
        if (request.weight() > ruleConfiguration.getHeavy().weight()) {
            weightCost = ruleConfiguration.getHeavy().cost() * request.weight();  // 20 * weight if weight > 10 kg
        }

        double volumeCost = ruleConfiguration.getLarge().cost() * volume; // default volume cost computation

        // VOLUME rule less than specified volume in config file
        if (volume < ruleConfiguration.getSmall().volume()) {
            volumeCost = ruleConfiguration.getSmall().cost() * volume;
        } else if (volume < ruleConfiguration.getMedium().volume()) {
            volumeCost = ruleConfiguration.getMedium().cost() * volume;
        }

        // Calculate total cost
        double totalCostBeforeDisc = weightCost + volumeCost;

        double totalCost = voucherService.getVoucherDiscount(totalCostBeforeDisc, "MYNT");

        return TOTAL_STRING.concat(String.valueOf(totalCost));

    }

}