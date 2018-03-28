package com.bobo.gmargiani.bobo.ui.views;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.utils.AppUtils;
import com.bobo.gmargiani.bobo.utils.ImageLoader;
import com.bobo.gmargiani.bobo.utils.ViewUtils;

/**
 * Created by gmargiani on 3/26/2018.
 */

public class ToolbarWidget extends RelativeLayout implements View.OnFocusChangeListener, View.OnClickListener, TextView.OnEditorActionListener {
    private static int EXPAND_COLLAPSE_ANIM_DURATION = 400;

    private View iconWrapper;

    private ImageView iconInbox;
    private ImageView iconFavorites;
    private ImageView iconFilter;
    private ImageView iconSearch;

    private EditText editText;

    private ToolbarWidgetListener toolbarWidgetListener;

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
        iconInbox = findViewById(R.id.toolbar_inbox);
        iconFavorites = findViewById(R.id.toolbar_favorite);
        iconFilter = findViewById(R.id.toolbar_filter);
        editText = findViewById(R.id.toolbar_et);
        iconWrapper = findViewById(R.id.toolbar_icons);
        iconSearch = findViewById(R.id.toolbar_search);

        ImageLoader.load(iconInbox)
                .setRes(R.drawable.ic_mail)
                .applyTint(true)
                .build();

        ImageLoader.load(iconFavorites)
                .setRes(R.drawable.ic_favorite_filled)
                .applyTint(true)
                .build();

        ImageLoader.load(iconFilter)
                .setRes(R.drawable.ic_filter)
                .applyTint(true)
                .build();

        ImageLoader.load(iconSearch)
                .setRes(R.drawable.ic_search)
                .applyTint(true)
                .build();

        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadius(AppUtils.getDimen(R.dimen.et_default_corner_radius));
        bg.setStroke(AppUtils.getDimen(R.dimen.one_dp), ContextCompat.getColor(getContext(), R.color.border_grey_color));

        editText.setBackground(bg);
        editText.setOnFocusChangeListener(this);
        editText.setOnEditorActionListener(this);

        iconInbox.setOnClickListener(this);
        iconFavorites.setOnClickListener(this);
        iconFilter.setOnClickListener(this);
        iconSearch.setOnClickListener(this);
    }

    public void setOnViewClickListener(ToolbarWidgetListener listener) {
        this.toolbarWidgetListener = listener;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            expandSearchView();
        } else {
            editText.setText("");
            collapseSearchView();
        }
    }

    private void expandSearchView() {
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

                if (toolbarWidgetListener != null) {
                    toolbarWidgetListener.onSearchViewExpand();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        iconWrapper.startAnimation(fadeOut);

    }

    private void collapseSearchView() {
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
                if (toolbarWidgetListener != null) {
                    toolbarWidgetListener.onSearchViewCollapse();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        iconWrapper.startAnimation(fadeIn);
    }

    public void collapseSearch() {
        editText.setText("");
        editText.clearFocus();
        ViewUtils.closeKeyboard(editText);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            onClick(iconSearch);
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_search) {
            if (!isSearchViewExpanded()) {
                ViewUtils.focusEditText(editText);
            } else {
                if (toolbarWidgetListener != null && !TextUtils.isEmpty(editText.getText())) {
                    toolbarWidgetListener.onSearchString(editText.getText().toString());
                }
            }
        } else if (toolbarWidgetListener != null) {
            toolbarWidgetListener.onMenuIconClick(v.getId());
        }
    }

    public boolean isSearchViewExpanded() {
        return iconWrapper.getVisibility() == GONE;
    }

    public interface ToolbarWidgetListener {
        void onSearchViewExpand();

        void onSearchViewCollapse();

        void onSearchString(String query);

        void onMenuIconClick(int iconResId);
    }
}
