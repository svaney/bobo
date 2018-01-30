package com.bobo.gmargiani.bobo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;

import com.bobo.gmargiani.bobo.app.App;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class Utils {

    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }
}
