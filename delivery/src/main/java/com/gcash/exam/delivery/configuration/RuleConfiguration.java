package com.gcash.exam.delivery.configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "calculate.parcel.rule")
@Data
public class RuleConfiguration {

    private Reject reject;
    private Heavy heavy;
    private Small small;
    private Medium medium;
    private Large large;

    public record Reject(double weight) {
    }

    public record Heavy(double weight, double cost) {
    }

    public record Small(double volume, double cost) {
    }

    public record Medium(double volume, double cost) {
    }

    public record Large(double cost) {
    }

}
