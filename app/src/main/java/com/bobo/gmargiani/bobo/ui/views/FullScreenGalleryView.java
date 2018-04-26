package com.bobo.gmargiani.bobo.ui.views;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.utils.ImageLoader;
import java.util.ArrayList;

public class FullScreenGalleryView extends ViewPager {

    private ArrayList<String> imageLinks;
    private OnTouchListener touchListener;
    private FullScreenGalleryViewAdapter adapter;

    public void setFiles(ArrayList<String> imageLinks) {
        this.imageLinks = imageLinks;
        adapter = new FullScreenGalleryViewAdapter();

        setAdapter(adapter);
    }

    public FullScreenGalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public FullScreenGalleryViewAdapter getAdapter() {
        return adapter;
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

    public void setOnPhotoTapListener(OnTouchListener touchListener) {
        this.touchListener = touchListener;
    }


    public class FullScreenGalleryViewAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageLinks == null ? 0 : imageLinks.size();
        }

        private View createView() {
            FrameLayout frameLayout = new FrameLayout(getContext());
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            TouchImageView imageView = new TouchImageView(getContext());

            frameLayout.setTag(imageView);
            frameLayout.addView(imageView);

            return frameLayout;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View recycledView = createView();

            TouchImageView imageView = (TouchImageView) recycledView.getTag();
            imageView.setOnTouchListener(touchListener);

            ImageLoader.load(imageView)
                    .setUrl(imageLinks.get(position), true)
                    .build();

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
