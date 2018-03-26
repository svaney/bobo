package com.bobo.gmargiani.bobo.ui.views;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.utils.AppUtils;
import com.bobo.gmargiani.bobo.utils.ImageLoader;
import com.bobo.gmargiani.bobo.utils.ViewUtils;

/**
 * Created by gmargiani on 3/26/2018.
 */

public class ToolbarWidget extends RelativeLayout implements View.OnFocusChangeListener {
    private static int EXPAND_COLLAPSE_ANIM_DURATION = 400;
    private View iconWrapper;
    private ImageView inbox;
    private ImageView favorite;
    private ImageView filter;
    private ImageView rightIcon;
    private EditText editText;
    private OnClickListener onClickListener;

    public ToolbarWidget(@NonNull Context context) {
        this(context, null);
    }

    public ToolbarWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }


    public ToolbarWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClipChildren(false);
        inflate(context, R.layout.component_toolbar_widget, this);
        findViews();
    }

    private void findViews() {
        inbox = findViewById(R.id.toolbar_inbox);
        favorite = findViewById(R.id.toolbar_favorite);
        filter = findViewById(R.id.toolbar_filter);
        editText = findViewById(R.id.toolbar_et);
        iconWrapper = findViewById(R.id.toolbar_icons);
        rightIcon = findViewById(R.id.edit_text_right_picture);

        ImageLoader.loadImage(inbox, R.drawable.ic_mail, false, true);
        ImageLoader.loadImage(favorite, R.drawable.ic_favorite_filled, false, true);
        ImageLoader.loadImage(filter, R.drawable.ic_filter, false, true);
        ImageLoader.loadImage(rightIcon, R.drawable.ic_search, false, true);

        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadius(AppUtils.getDimen(R.dimen.et_default_corner_radius));
        bg.setStroke(AppUtils.getDimen(R.dimen.one_dp), ContextCompat.getColor(getContext(), R.color.border_grey_color));

        editText.setBackground(bg);
        editText.setOnFocusChangeListener(this);

        showSearchIcon(true);

        setUpOnClickListeners();
    }

    public void setOnViewClickListener(OnClickListener listener) {
        this.onClickListener = listener;
        setUpOnClickListeners();
    }

    private void setUpOnClickListeners() {
        inbox.setOnClickListener(onClickListener);
        favorite.setOnClickListener(onClickListener);
        filter.setOnClickListener(onClickListener);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            fadeOutIcons();
        } else {
            editText.setText("");
            fadeInIcons();
        }
    }

    private void fadeOutIcons() {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(EXPAND_COLLAPSE_ANIM_DURATION);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iconWrapper.setVisibility(GONE);
                try {
                    TransitionManager.beginDelayedTransition(ToolbarWidget.this);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        iconWrapper.startAnimation(fadeOut);
        showSearchIcon(false);
    }

    private void fadeInIcons() {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(EXPAND_COLLAPSE_ANIM_DURATION);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                iconWrapper.setVisibility(VISIBLE);
                try {
                    TransitionManager.beginDelayedTransition(ToolbarWidget.this);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        iconWrapper.startAnimation(fadeIn);
        showSearchIcon(true);
    }


    private void showSearchIcon(boolean show) {
        rightIcon.setVisibility(show ? VISIBLE : GONE);

        if (!show) {
            if (onClickListener != null) {
                onClickListener.onClick(null);
            }
        }
    }

    public boolean isInSearchMode() {
        return rightIcon.getVisibility() == GONE;
    }

    public void closeSearch() {
        editText.setText("");
        editText.clearFocus();
        ViewUtils.closeKeyboard(editText);
    }
}
