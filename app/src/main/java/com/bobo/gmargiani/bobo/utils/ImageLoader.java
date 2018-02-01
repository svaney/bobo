package com.bobo.gmargiani.bobo.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bobo.gmargiani.bobo.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

/**
 * Created by gmargiani on 2/1/2018.
 */

public class ImageLoader {

    public static void loadImage(ImageView imageView, @DrawableRes int resId) {
        loadImage(imageView, resId, false);
    }

    public static void loadImage(ImageView imageView, String imageUrl) {
        loadImage(imageView, imageUrl, false);
    }

    public static void loadImage(ImageView imageView, File file) {
        loadImage(imageView, file, false);
    }

    public static void loadImage(ImageView imageView, Uri uri) {
        loadImage(imageView,  uri, false);
    }

    public static void loadImage(ImageView imageView, String imageUrl, boolean isOval) {
        loadImage(imageView, imageUrl, null, null,  R.drawable.transparent_placeholder,  R.drawable.transparent_placeholder,  R.drawable.transparent_placeholder, isOval);
    }

    public static void loadImage(ImageView imageView, File file, boolean isOval) {
        loadImage(imageView, null, null, file,  R.drawable.transparent_placeholder,  R.drawable.transparent_placeholder,  R.drawable.transparent_placeholder, isOval);
    }

    public static void loadImage(ImageView imageView, Uri uri, boolean isOval) {
        loadImage(imageView, null, uri, null, R.drawable.transparent_placeholder,  R.drawable.transparent_placeholder,  R.drawable.transparent_placeholder, isOval);
    }

    public static void loadImage(ImageView imageView, @DrawableRes int resId, boolean isOval) {
        loadImage(imageView, resId, R.drawable.transparent_placeholder, isOval);
    }

    public static void loadImage(ImageView imageView, @DrawableRes int resId, @DrawableRes int placeHolderId, boolean isOval) {
        loadImage(imageView, resId, placeHolderId, placeHolderId, isOval);
    }

    public static void loadImage(ImageView imageView, @DrawableRes int resId, @DrawableRes int placeHolderId, @DrawableRes int errorId, boolean isOval) {
        loadImage(imageView, null, null, null, resId, placeHolderId, errorId, isOval);
    }

    public static void loadImage(ImageView imageView, String imageUrl, Uri uri, File file, @DrawableRes int resId, @DrawableRes int placeHolderId, @DrawableRes int errorId, boolean isOval) {
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
                options.circleCrop();
            }

            drawableTypeRequest.apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
        } catch (Exception ignored) {
        }
    }
}
