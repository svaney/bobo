package com.bobo.gmargiani.bobo.ui.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.bobo.gmargiani.bobo.R;

public class TopShadow extends FrameLayout {
    public TopShadow(Context context) {
        this(context, null);
    }

    public TopShadow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setUpView(context);
    }

    private void setUpView(Context context) {
        inflate(context, R.layout.component_top_shadow, this);
    }
}
