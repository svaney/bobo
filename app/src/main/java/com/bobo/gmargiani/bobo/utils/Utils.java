package com.bobo.gmargiani.bobo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;

import com.bobo.gmargiani.bobo.app.App;

import java.math.BigDecimal;

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
}

