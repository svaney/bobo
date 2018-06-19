package com.bobo.gmargiani.bobo.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.utils.AppUtils;
import com.bobo.gmargiani.bobo.utils.ImageLoader;

public class NewImageView extends FrameLayout {
    private View backgroundView;
    private View addButton;
    private View closeButtonWrapper;
    private ImageView userImage;
    private ImageView closeIcon;
    private NewImageListener listener;

    private Bitmap bitmap;
    private int position;

    public NewImageView(@NonNull Context context) {
        this(context, null);
    }

    public NewImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setClipChildren(false);
        inflate(context, R.layout.component_new_picture, this);

        findViews();
    }

    private void findViews() {
        backgroundView = findViewById(R.id.background);
        userImage = findViewById(R.id.user_image);
        addButton = findViewById(R.id.add_button);
        closeIcon = findViewById(R.id.close_button);
        closeButtonWrapper = findViewById(R.id.close_button_wrapper);

        ImageLoader.load(closeIcon)
                .setRes(R.drawable.ic_close_white)
                .build();

        GradientDrawable gr = new GradientDrawable();
        gr.setShape(GradientDrawable.RECTANGLE);
        gr.setCornerRadius(AppUtils.getDimen(R.dimen.picture_place_holder_radius));
        gr.setColor(ContextCompat.getColor(getContext(), R.color.new_picture_background));

        backgroundView.setBackground(gr);

        GradientDrawable br = new GradientDrawable();
        br.setShape(GradientDrawable.OVAL);
        br.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        closeButtonWrapper.setBackground(br);
    }

    public void clearImage() {
        bitmap = null;
        addButton.setVisibility(VISIBLE);
        userImage.setVisibility(GONE);
        closeButtonWrapper.setVisibility(GONE);
    }

    public void setBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            clearImage();
        } else {
            addButton.setVisibility(GONE);
            closeButtonWrapper.setVisibility(VISIBLE);
            userImage.setVisibility(VISIBLE);
            userImage.setImageBitmap(bitmap);
        }
    }

    public void setListener(final NewImageListener listener, int pos) {

        this.position = pos;
        this.listener = listener;

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onImageClick(position);
                }
            }
        });

        userImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onImageClick(position);
                }
            }
        });

        closeButtonWrapper.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCloseImageClick(position);
                }
            }
        });
    }

    public interface NewImageListener {
        void onCloseImageClick(int position);

        void onImageClick(int position);
    }
}
