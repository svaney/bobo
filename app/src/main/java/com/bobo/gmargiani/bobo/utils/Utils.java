package com.bobo.gmargiani.bobo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.app.App;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class Utils {
    public final static String GEL_SYM = "\u20BE";

    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    public static String getAmountWithGel(BigDecimal amount) {
        return getAmountWithGel(amount, false);
    }

    public static String getAmountWithGel(BigDecimal amount, boolean space) {
        return getAmountStringValue(amount) + (space ? " " : "") + GEL_SYM;
    }

    public static String getAmountStringValue(BigDecimal amount) {

        if ("null".equals(String.valueOf(amount))) {
            return "";
        }

        return String.valueOf(amount);
    }

    public static String getFullDate(long timeStamp, Context context) {
        if (timeStamp <= 0) {
            return null;
        }


        Calendar calendar = Calendar.getInstance();
        int yearNow = calendar.get(Calendar.YEAR);

        calendar.setTimeInMillis(timeStamp * 1000);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String[] months = context.getResources().getStringArray(R.array.months);

        String yearString = (yearNow == year) ? "" : ", " + year;
        return day + " " + months[month] + yearString;
    }
}

