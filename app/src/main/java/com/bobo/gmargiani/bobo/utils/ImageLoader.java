package com.bobo.gmargiani.bobo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.app.App;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.security.MessageDigest;

/**
 * Created by gmargiani on 2/1/2018.
 */

public class ImageLoader {

    public static void loadImage(ImageView imageView, @DrawableRes int resId) {
        loadImage(imageView, resId, false, false);
    }

    public static void loadImage(ImageView imageView, Bitmap bitmap) {
        loadImage(imageView, bitmap, null, null, null, R.drawable.transparent_placeholder, R.drawable.transparent_placeholder, R.drawable.transparent_placeholder, false, false);
    }

    public static void loadImage(ImageView imageView, String imageUrl) {
        loadImage(imageView, imageUrl, false);
    }

    public static void loadImage(ImageView imageView, File file) {
        loadImage(imageView, file, false);
    }

    public static void loadImage(ImageView imageView, Uri uri) {
        loadImage(imageView, uri, false);
    }

    public static void loadImage(ImageView imageView, String imageUrl, boolean isOval) {
        loadImage(imageView, imageUrl, null, null, R.drawable.transparent_placeholder, R.drawable.transparent_placeholder, R.drawable.transparent_placeholder, isOval, false);
    }

    public static void loadImage(ImageView imageView, File file, boolean isOval) {
        loadImage(imageView, null, null, file, R.drawable.transparent_placeholder, R.drawable.transparent_placeholder, R.drawable.transparent_placeholder, isOval, false);
    }

    public static void loadImage(ImageView imageView, Uri uri, boolean isOval) {
        loadImage(imageView, null, uri, null, R.drawable.transparent_placeholder, R.drawable.transparent_placeholder, R.drawable.transparent_placeholder, isOval, false);
    }

    public static void loadImage(ImageView imageView, @DrawableRes int resId, boolean isOval, boolean applyTint) {
        loadImage(imageView, resId, R.drawable.transparent_placeholder, isOval, applyTint);
    }

    public static void loadImage(ImageView imageView, @DrawableRes int resId, @DrawableRes int placeHolderId, boolean isOval, boolean applyTint) {
        loadImage(imageView, resId, placeHolderId, placeHolderId, isOval, applyTint);
    }

    public static void loadImage(ImageView imageView, @DrawableRes int resId, @DrawableRes int placeHolderId, @DrawableRes int errorId, boolean isOval, boolean applyTint) {
        loadImage(imageView, null, null, null, resId, placeHolderId, errorId, isOval, applyTint);
    }

    public static void loadImage(ImageView imageView, String imageUrl, Uri uri, File file, @DrawableRes int resId, @DrawableRes int placeHolderId,
                                 @DrawableRes int errorId, boolean isOval, boolean applyTint) {
        loadImage(imageView, null, imageUrl, uri, file, resId, placeHolderId, errorId, isOval, applyTint);
    }

    public static void loadImage(ImageView imageView, Bitmap bitmap, String imageUrl, Uri uri, File file, @DrawableRes int resId,
                                 @DrawableRes int placeHolderId, @DrawableRes int errorId, boolean isOval, boolean applyTint) {
        try {
            Context context = imageView.getContext();

            RequestManager requestManager = Glide.with(context);

            RequestBuilder<Drawable> drawableTypeRequest;
            if (imageUrl != null) {
                drawableTypeRequest = requestManager.load(imageUrl);
            } else if (uri != null) {
                drawableTypeRequest = requestManager.load(uri);
            } else if (file != null) {
                drawableTypeRequest = requestManager.load(file);
            } else if (bitmap != null) {
                drawableTypeRequest = requestManager.load(bitmap);
            } else {
                if (resId == -1) {
                    resId = placeHolderId;
                }
                drawableTypeRequest = requestManager.load(resId);
            }

            RequestOptions options = new RequestOptions();
            if (placeHolderId > -1)
                options.placeholder(placeHolderId);
            if (errorId > -1)
                options.error(errorId);

            if (isOval) {
                if (!applyTint) {
                    options.circleCrop();
                } else {
                    options.transforms(new ColorFilterTransformation(ViewUtils.getAccentColor(App.getInstance())), new CircleCrop());
                }
            } else {
                if (applyTint) {
                    options.transform(new ColorFilterTransformation(ViewUtils.getAccentColor(context)));
                }
            }

            drawableTypeRequest.apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
        } catch (Exception ignored) {
        }
    }


}
