package com.security.reactivesecurity.util;

import java.util.Date;

public class DateUtils {

    public static Date currentUtilDate() {
        return new Date();
    }

    public static Date extendDateBy(Date date, long milliseconds) {
        return new Date(date.getTime() + milliseconds);
    }

}
