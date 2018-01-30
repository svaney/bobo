package com.bobo.gmargiani.bobo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;

import com.bobo.gmargiani.bobo.app.App;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class AppUtils {
    public static int getAndroidVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static  boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm != null && cm.getActiveNetworkInfo() != null;
    }
}
