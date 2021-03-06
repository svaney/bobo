package com.bobo.gmargiani.bobo.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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

    public static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm != null && cm.getActiveNetworkInfo() != null;
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

    public static boolean atLesatnNugat() {
        return getAndroidVersion() >= Build.VERSION_CODES.N;
    }

    @SuppressLint("NewApi")
    public static boolean hasPermission(Activity activity, String permission) {
        if (atLeastMarshmallow()) {
            return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    public static int getDimen(int dimensionId, Context context) {
        return context.getResources().getDimensionPixelSize(dimensionId);
    }

    public static int getDimen(int dimenId) {
        return getDimen(dimenId, App.getInstance());
    }

    @SuppressLint("NewApi")
    public static void callNumber(Activity activity, String number) {
        try {
            if (AppUtils.atLeastMarshmallow()) {
                if (AppUtils.hasPermission(activity, Manifest.permission.CALL_PHONE)) {
                    createCallIntent(activity, number);
                } else {
                    activity.requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, AppConsts.PERMISSION_PHONE);
                }
            } else {
                createCallIntent(activity, number);
            }
        } catch (Exception e) {
        }
    }

    @SuppressLint("MissingPermission")
    private static void createCallIntent(Activity activity, String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        activity.startActivity(callIntent);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Drawable getDrawable(int resId, Context context) {
        return atLeastMarshmallow() ?
                context.getResources().getDrawable(resId, context.getTheme()) : context.getResources().getDrawable(resId);

    }

}
