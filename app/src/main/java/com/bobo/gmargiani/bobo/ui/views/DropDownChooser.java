package com.bobo.gmargiani.bobo.ui.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;

/**
 * Created by gmargiani on 1/31/2018.
 */

public class DropDownChooser extends LinearLayout implements View.OnClickListener {

    private TextView textTV;
    private ImageView imageView;
    private OnClickListener listener;

    public DropDownChooser(Context context) {
        this(context, null);
    }

    public DropDownChooser(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (this.isInEditMode()) return;

        setUpView();
    }

    private void setUpView() {
        inflate(getContext(), R.layout.component_drop_down_chooser, this);

        textTV = findViewById(R.id.text_tv);
        imageView = findViewById(R.id.image);

        imageView.setOnClickListener(this);
    }

    public void setText(String text) {
        textTV.setText(text);
    }

    public void setImageId(int imageId) {
        imageView.setImageResource(imageId);
    }

    public void setImageClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v == imageView && listener != null) {
            listener.onClick(v);
        }
    }
}
