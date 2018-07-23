package com.bobo.gmargiani.bobo.ui.views;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.utils.AppUtils;
import com.bobo.gmargiani.bobo.utils.ImageLoader;

import org.w3c.dom.Text;

/**
 * Created by gmarg on 4/19/2018.
 */

public class FilterTextView extends LinearLayout {
    private View itemWrapper;
    private TextView textView;
    private ImageView imageView;

    private GradientDrawable bg;

    public FilterTextView(Context context) {
        this(context, null);
    }

    public FilterTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.component_filter_text_view, this);
        findViews();

        bg = new GradientDrawable();
        bg.setColor(ContextCompat.getColor(getContext(), R.color.floating_button_background_color));
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadius(AppUtils.getDimen(R.dimen.filter_text_view_radius) / 2);

    }

    private void findViews() {
        itemWrapper = findViewById(R.id.item_wrapper);
        textView = findViewById(R.id.text_view);
        imageView = findViewById(R.id.close_icon);

        ImageLoader.load(imageView)
                .setRes(R.drawable.ic_close_white)
                .build();
    }

    public void showCloseButton(boolean show) {
        imageView.setVisibility(show ? VISIBLE : GONE);
        itemWrapper.setBackground(show ? bg : null);
        textView.setTextColor(ContextCompat.getColor(getContext(), show ? R.color.color_white : R.color.color_black));
    }

    public void showBg(boolean show){
        itemWrapper.setBackground(show ? bg : null);
    }

    public void setText(String text) {
        textView.setText(text);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener listener) {
        itemWrapper.setOnClickListener(listener);
    }

    public void setOnCloseClickListener(OnClickListener onClickListener) {
        imageView.setOnClickListener(onClickListener);
    }
}
