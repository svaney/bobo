package com.bobo.gmargiani.bobo.utils;

import android.app.Activity;
import android.support.v4.content.ContextCompat;

import com.bobo.gmargiani.bobo.R;
import com.tapadoo.alerter.Alerter;

/**
 * Created by gmargiani on 1/31/2018.
 */

public class AlertManager {
    public static final long SHORT = 2000L;
    public static final long VERY_LONG = 5000L;

    public static void showError(Activity activity, String text) {
        showError(activity, text, SHORT);
    }

    public static void showError(Activity activity, String text, long duration) {
        if (activity != null) {
            Alerter.create(activity)
                    .setText(text)
                    .setBackgroundColorInt(ContextCompat.getColor(activity, (R.color.error_red_color)))
                    .enableSwipeToDismiss()
                    .setDuration(duration)
                    .show();
        }
    }
}
