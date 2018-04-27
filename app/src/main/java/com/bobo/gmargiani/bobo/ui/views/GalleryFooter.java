package com.bobo.gmargiani.bobo.ui.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.ui.adapters.RecyclerItemClickListener;
import com.bobo.gmargiani.bobo.utils.AppUtils;
import com.bobo.gmargiani.bobo.utils.ImageLoader;

import java.util.ArrayList;

public class GalleryFooter extends FrameLayout implements ViewPager.OnPageChangeListener {
    private LinearLayout dotWrapper;
    private ImageView icFavorite;
    private ImageView icFavoriteFilled;
    private View icShare;
    private FullScreenGalleryView galleryView;

    private int dotCount;
    private ImageView[] dotViews;


    public GalleryFooter(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUpViews();
    }

    private void setUpViews() {
        inflate(getContext(), R.layout.gallery_footer, this);

        dotWrapper = findViewById(R.id.gallery_dot_wrapper);
        icFavorite = findViewById(R.id.item_favorites_ic);
        icFavoriteFilled = findViewById(R.id.item_favorites_ic_filled);
        icShare = findViewById(R.id.ic_share);

        ImageLoader.load(icFavorite)
                .setRes(R.drawable.ic_favorite)
                .applyTint(R.color.color_white)
                .build();

        ImageLoader.load(icFavoriteFilled)
                .setRes(R.drawable.ic_favorite_filled)
                .applyTint(true)
                .build();
    }

    public void showFilledFavorite(boolean show) {
        icFavorite.setVisibility(show ? GONE : VISIBLE);
        icFavoriteFilled.setVisibility(show ? VISIBLE : GONE);
    }

    public void setDataSize(int count) {
        this.dotCount = count;
        setUpDots();
    }

    public void setGallery(FullScreenGalleryView gallery) {
        this.galleryView = gallery;
    }

    private void setUpDots() {
        if (getContext() != null) {
            this.dotViews = new ImageView[this.dotCount];
            dotWrapper.removeAllViews();

            for (int i = 0; i < this.dotCount; i++) {
                this.dotViews[i] = new ImageView(getContext());
                this.dotViews[i].setImageDrawable(AppUtils.getDrawable(R.drawable.gallery_dot_background, getContext()));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(16, 0, 16, 0);

                this.dotWrapper.addView(this.dotViews[i], params);
                final int finalPos = i;
                this.dotViews[i].setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (galleryView != null) {
                            galleryView.setCurrentItem(finalPos);
                        }
                    }
                });
            }


            if (this.dotCount > 0)
                this.dotViews[0].setImageDrawable(AppUtils.getDrawable(R.drawable.gallery_selected_dot_background, getContext()));
        }
    }


    public void setSelectedPos(int pos) {
        if (galleryView != null){
            galleryView.setCurrentItem(pos);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < this.dotCount; i++) {
            this.dotViews[i].setImageDrawable(AppUtils.getDrawable(R.drawable.gallery_dot_background, getContext()));
        }
        this.dotViews[position].setImageDrawable(AppUtils.getDrawable(R.drawable.gallery_selected_dot_background, getContext()));

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
