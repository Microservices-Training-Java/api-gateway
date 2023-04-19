package com.sub.authen.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

public class DateUtils {
    private static final String VI_ZONE = "Asia/Ho_Chi_Minh";
    public static String getCurrentDateTimeStr() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        String currentDateTimeStr = dateFormat.format(currentDate);
        return currentDateTimeStr;
    }
    public static Long getCurrentEpoch() {
        return LocalDateTime.now(ZoneId.of(VI_ZONE)).toEpochSecond(ZoneOffset.UTC);
    }
}

