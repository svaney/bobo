package com.bobo.gmargiani.bobo.ui.views;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public class ToolbarSearchWidget extends RelativeLayout implements View.OnFocusChangeListener, View.OnClickListener, TextView.OnEditorActionListener {

    public static final int MODE_STRICTLY_EXPANDED = 10;
    public static final int MODE_EXPAND_COLLAPSE = 20;

    private static int EXPAND_COLLAPSE_ANIM_DURATION = 400;

    private int currentMode;

    private View iconWrapper;

    private ImageView iconInbox;
    private ImageView iconFavorites;
    private ImageView iconFilter;
    private ImageView iconSearch;

    private EditText editText;

    private ToolbarWidgetListener toolbarWidgetListener;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (toolbarWidgetListener != null) {
                toolbarWidgetListener.onSearchStringChange(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public ToolbarSearchWidget(@NonNull Context context) {
        this(context, null);
    }

    public ToolbarSearchWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ToolbarSearchWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        iconSearch = findViewById(R.id.toolbar_action_icon);

        ImageLoader.load(iconInbox)
                .setRes(R.drawable.ic_mail)
                .applyTint(true)
                .build();
        iconInbox.setVisibility(GONE);

        ImageLoader.load(iconFavorites)
                .setRes(R.drawable.ic_favorite_filled)
                .applyTint(true)
                .build();

        ImageLoader.load(iconFilter)
                .setRes(R.drawable.ic_filter)
                .applyTint(true)
                .build();

        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setColor(ContextCompat.getColor(getContext(), R.color.color_transparent));
        bg.setCornerRadius(AppUtils.getDimen(R.dimen.et_default_corner_radius));
        bg.setStroke(AppUtils.getDimen(R.dimen.one_dp), ContextCompat.getColor(getContext(), R.color.border_grey_color));

        editText.setBackground(bg);
        editText.setOnFocusChangeListener(this);
        editText.setOnEditorActionListener(this);
        editText.removeTextChangedListener(textWatcher);
        editText.addTextChangedListener(textWatcher);

        iconInbox.setOnClickListener(this);
        iconFavorites.setOnClickListener(this);
        iconFilter.setOnClickListener(this);
        iconSearch.setOnClickListener(this);

        if (currentMode != MODE_EXPAND_COLLAPSE || currentMode != MODE_STRICTLY_EXPANDED) {
            setMode(MODE_EXPAND_COLLAPSE);
        }
    }

    public void setOnViewClickListener(ToolbarWidgetListener listener) {
        this.toolbarWidgetListener = listener;
    }

    public void setSearchText(String text) {
        editText.setText(text);
    }

    public void setMode(int mode) {
        currentMode = mode;

        if (currentMode == MODE_STRICTLY_EXPANDED) {
            ImageLoader.load(iconSearch)
                    .setRes(R.drawable.ic_close)
                    .applyTint(true)
                    .build();
            expandSearchView(false);
        } else {
            ImageLoader.load(iconSearch)
                    .setRes(R.drawable.ic_search)
                    .applyTint(true)
                    .build();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (currentMode == MODE_EXPAND_COLLAPSE) {
            if (hasFocus) {
                expandSearchView(true);
            } else {
                editText.setText("");
                collapseSearchView();
            }
        }
    }

    private void expandSearchView(boolean animate) {
        if (animate) {
            Animation fadeOut = new AlphaAnimation(1, 0);
            fadeOut.setInterpolator(new AccelerateInterpolator());
            fadeOut.setDuration(EXPAND_COLLAPSE_ANIM_DURATION);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if (toolbarWidgetListener != null) {
                        toolbarWidgetListener.onSearchViewExpand();
                    }
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    iconWrapper.setVisibility(GONE);
                    try {
                        TransitionManager.beginDelayedTransition(ToolbarSearchWidget.this);
                    } catch (Exception ignored) {
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            iconWrapper.startAnimation(fadeOut);
        } else {
            iconWrapper.setVisibility(GONE);
        }

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
                    TransitionManager.beginDelayedTransition(ToolbarSearchWidget.this);
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
            searchString();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_action_icon) {
            switch (currentMode) {
                case MODE_EXPAND_COLLAPSE:
                    if (!isSearchViewExpanded()) {
                        ViewUtils.focusEditText(editText);
                    } else {
                        searchString();
                    }
                    break;
                case MODE_STRICTLY_EXPANDED:
                    if (toolbarWidgetListener != null) {
                        toolbarWidgetListener.onMenuIconClick(v.getId());
                    }
                    break;
            }

        } else if (toolbarWidgetListener != null) {
            toolbarWidgetListener.onMenuIconClick(v.getId());
        }
    }

    public void searchString() {
        if (toolbarWidgetListener != null && !TextUtils.isEmpty(editText.getText())) {
            toolbarWidgetListener.onSearchString(editText.getText().toString());
            if (currentMode == MODE_STRICTLY_EXPANDED) {
                editText.clearFocus();
                ViewUtils.closeKeyboard(editText);
            }
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

        void onSearchStringChange(String query);
    }
}
