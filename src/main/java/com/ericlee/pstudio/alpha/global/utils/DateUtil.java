package com.ericlee.pstudio.alpha.global.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@Component
public class DateUtil {

    private final SimpleDateFormat YEAR_MONTH_DATE = new SimpleDateFormat("yyyy-MM-dd");
    public String yearMonthDate(LocalDateTime lastModified) {
        if(lastModified == null) {
            return "-";
        }
        return YEAR_MONTH_DATE.format(lastModified);
    }
}
