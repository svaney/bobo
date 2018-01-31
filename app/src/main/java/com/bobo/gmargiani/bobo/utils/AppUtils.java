package com.bobo.gmargiani.bobo.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.bobo.gmargiani.bobo.app.App;

import java.lang.reflect.Method;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class AppUtils {
    private static int displayHeight = -1, displayWidth = -1;

    public static int getAndroidVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static  boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm != null && cm.getActiveNetworkInfo() != null;
    }

    public static void closeKeyboard(View focused) {
        InputMethodManager imm = (InputMethodManager) focused.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(focused.getWindowToken(), 0);
    }

    public static int convertDpToPixels(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int getDisplayHeight(Context context) {
        if (displayHeight < 0) {
            Display display = getDisplay(context);
            displayHeight = getDisplaySize(display).y;
        }
        return displayHeight;
    }

    public static int getDisplayWidth(Context context) {
        if (displayWidth < 0) {
            Display display = getDisplay(context);
            displayWidth = getDisplaySize(display).x;
        }
        return displayWidth;
    }

    public static Display getDisplay(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Point getDisplaySize(Display display) {
        if (atLeastJellyBean17()) {
            Point outPoint = new Point();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getRealMetrics(metrics);
            outPoint.x = metrics.widthPixels;
            outPoint.y = metrics.heightPixels;
            return outPoint;
        }

        return getRealSize(display);
    }

    public static Point getRealSize(Display display) {
        Point outPoint = new Point();
        Method mGetRawH;
        try {
            mGetRawH = Display.class.getMethod("getRawHeight");
            Method mGetRawW = Display.class.getMethod("getRawWidth");
            outPoint.x = (Integer) mGetRawW.invoke(display);
            outPoint.y = (Integer) mGetRawH.invoke(display);
            return outPoint;
        } catch (Throwable e) {
            return null;
        }
    }

    public static boolean atLeastJellyBean18() {
        return getAndroidVersion() >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean atLeastJellyBean17() {
        return getAndroidVersion() >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean atLeastJellyBean16() {
        return getAndroidVersion() >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean atLeastLollipop() {
        return getAndroidVersion() >= Build.VERSION_CODES.LOLLIPOP;
    }


    public static boolean atLeastMarshmallow() {
        return getAndroidVersion() >= Build.VERSION_CODES.M;
    }

    public static int getDimen(int dimensionId, Context context) {
        return context.getResources().getDimensionPixelSize(dimensionId);
    }

    public static int getDimen(int dimenId) {
        return getDimen(dimenId, App.getInstance());
    }

}
