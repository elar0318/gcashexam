package com.gcash.exam.delivery.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static boolean isExpired(String inputDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate inputDate = LocalDate.parse(inputDateString, formatter);

        LocalDate currentDate = LocalDate.now();

        return inputDate.isBefore(currentDate);
    }

}