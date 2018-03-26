package com.bobo.gmargiani.bobo.utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.bobo.gmargiani.bobo.R;

/**
 * Created by gmargiani on 2/2/2018.
 */

public class ViewUtils {

    public static GradientDrawable getErrorRectangleBackground(Context context) {
        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadius(AppUtils.convertDpToPixels(5, context));
        bg.setStroke(AppUtils.convertDpToPixels(1, context), ContextCompat.getColor(context, android.R.color.holo_red_light));
        bg.setColor(ContextCompat.getColor(context, android.R.color.transparent));

        return bg;
    }


    public static void closeKeyboard(View focused) {
        InputMethodManager imm = (InputMethodManager) focused.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(focused.getWindowToken(), 0);
    }

    public static void shakeView(View v) {
        Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.wiggle_anim);
        v.startAnimation(animation);
    }

    public static boolean validateEditText(TextInputEditText et, Context context){
        if (TextUtils.isEmpty(et.getText())) {
            et.setBackground(ViewUtils.getErrorRectangleBackground(context));
            ViewUtils.shakeView(et);
            return false;
        }
        return true;
    }

    public static void focusEditText(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static Animation collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
        return a;
    }

    public static Animation expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
        return a;
    }

    public static int getAccentColor(Context context){
        return getAttributeColor(context, R.attr.colorAccent, false);
    }

    public static int getAttributeColor(Context context, int attrId, boolean returnId) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attrId, typedValue, true);
        int colorResId = typedValue.resourceId;

        if (returnId) return colorResId;

        return ContextCompat.getColor(context, colorResId);
    }
}
