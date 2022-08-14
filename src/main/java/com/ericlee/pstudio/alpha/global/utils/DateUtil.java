package com.ericlee.pstudio.alpha.global.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtil {

    private final DateTimeFormatter YEAR_MONTH_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public String yearMonthDate(LocalDateTime time) {
        if(time == null) {
            return "-";
        }
        return YEAR_MONTH_DATE.format(time);
    }

    private final DateTimeFormatter YEAR_MONTH_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
    public String yearMonthDateWithTime(LocalDateTime time) {
        if(time == null) {
            return "-";
        }
        return YEAR_MONTH_DATE_TIME.format(time);
    }
}
