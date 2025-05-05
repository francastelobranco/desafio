package com.desafio.logistica.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateFormatUtils {

    public static Date formatDate(LocalDate date) {
        return date != null ? Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
    }
}
