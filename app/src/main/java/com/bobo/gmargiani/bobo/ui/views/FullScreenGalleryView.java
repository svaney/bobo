package com.bobo.gmargiani.bobo.ui.views;

import android.content.Context;
import android.os.Parcel;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ImageViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.ui.adapters.RecyclerItemClickListener;
import com.bobo.gmargiani.bobo.utils.ImageLoader;

import java.util.ArrayList;

public class FullScreenGalleryView extends ViewPager {

    private ArrayList<String> imageLinks;
    private FullScreenGalleryViewAdapter adapter;
    private RecyclerItemClickListener imageClickListener;
    private boolean isImageTouchable;

    public FullScreenGalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public FullScreenGalleryViewAdapter getAdapter() {
        return adapter;
    }

    public void setImages(ArrayList<String> imageLinks) {
        this.imageLinks = imageLinks;
        adapter = new FullScreenGalleryViewAdapter();

        setAdapter(adapter);
    }

    public void setOnImageClickListener(RecyclerItemClickListener listener) {
        this.imageClickListener = listener;
    }

    public void setImageTouchable(boolean imageTouchable) {
        isImageTouchable = imageTouchable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof TouchImageView) {
            return ((TouchImageView) v).canScrollHorizontallyFroyo(-dx);
        } else {
            return super.canScroll(v, checkV, dx, x, y);
        }
    }

    public class FullScreenGalleryViewAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageLinks == null || imageLinks.size() == 0 ? 1 : imageLinks.size();
        }

        private FrameLayout createView() {
            FrameLayout frameLayout = new FrameLayout(getContext());
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            if (isImageTouchable) {
                TouchImageView imageView = new TouchImageView(getContext());
                frameLayout.addView(imageView);
            } else {
                ImageView imageView = new ImageView(getContext());
                frameLayout.addView(imageView);
            }

            return frameLayout;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            FrameLayout recycledView = createView();

            ImageView imageView = (ImageView) recycledView.getChildAt(0);

            if (imageClickListener != null) {
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageClickListener.onRecyclerItemClick(position);
                    }
                });

            }

            if (imageLinks == null || imageLinks.size() == 0){
                ImageLoader.load(imageView)
                        .setRes(R.drawable.statement_image_place_holder)
                        .build();
            } else {
                ImageLoader.load(imageView)
                        .setUrl(imageLinks.get(position), true)
                        .build();
            }

            container.addView(recycledView);

            return recycledView;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            FullScreenGalleryView galleryView = (FullScreenGalleryView) container;
            View recycleView = (View) object;

            galleryView.removeView(recycleView);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
