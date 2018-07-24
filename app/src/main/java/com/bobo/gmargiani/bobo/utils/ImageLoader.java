package com.bobo.gmargiani.bobo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.app.App;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;

public class ImageLoader {

    public static ImageLoader.I load(ImageView imageView) {
        return new ImageLoader.I(imageView);
    }

    public static class I {
        private ImageView imageView;

        private int resId;
        private int placeHolderId;
        private int errorId;
        private int tintColorId;
        private boolean asBitmap;

        private Bitmap bitmap;
        private String imageUrl;
        private Uri uri;
        private File file;

        private boolean isOval;
        private boolean applyTint;
        private boolean isBitmap;
        private boolean isUrl;
        private boolean isUri;
        private boolean isFile;
        private boolean hasPlaceHolder;
        private SimpleTarget<Bitmap> target;

        public I(ImageView imageView) {
            this.imageView = imageView;
            this.resId = R.drawable.transparent_placeholder;
            this.errorId = R.drawable.transparent_placeholder;
            this.placeHolderId = R.drawable.transparent_placeholder;
        }

        public ImageLoader.I setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
            this.isBitmap = true;
            return this;
        }

        public ImageLoader.I setUrl(String url) {
            this.imageUrl = url;
            this.isUrl = true;
            return this;
        }

        public ImageLoader.I setUrl(String url, boolean asBitmap) {
            this.imageUrl = url;
            this.isUrl = true;
            this.asBitmap = asBitmap;
            return this;
        }

        public ImageLoader.I setUri(Uri uri) {
            this.uri = uri;
            this.isUri = true;
            return this;
        }

        public ImageLoader.I setFile(File file) {
            this.file = file;
            this.isFile = true;
            return this;
        }

        public ImageLoader.I setOval(boolean oval) {
            this.isOval = oval;
            return this;
        }

        public ImageLoader.I applyTint(boolean apply) {
            this.applyTint = apply;
            this.tintColorId = R.color.colorAccent;
            return this;
        }

        public ImageLoader.I applyTint(@ColorRes int colorId) {
            this.tintColorId = colorId;
            this.applyTint = true;
            return this;
        }

        public ImageLoader.I setRes(@DrawableRes int resId) {
            this.resId = resId;
            return this;
        }

        public ImageLoader.I setPlaceHolderId(@DrawableRes int resId) {
            this.placeHolderId = resId;
            this.hasPlaceHolder = true;
            return this;
        }

        public ImageLoader.I setTarget(SimpleTarget<Bitmap> target) {
            this.target = target;
            return this;
        }

        public ImageLoader.I setErroPlaceHolder(@DrawableRes int resId) {
            this.errorId = resId;
            return this;
        }


        public void build() {
            try {

                Context context = imageView.getContext();

                if (asBitmap) {
                    RequestOptions options = new RequestOptions().override(400, 400);
                    Glide.with(context).asBitmap().load(imageUrl).apply(options).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            imageView.setImageBitmap(resource);
                            if (target != null) {
                                target.onResourceReady(resource, transition);
                            }
                        }
                    });
                    return;
                }

                RequestManager requestManager = Glide.with(context);
                RequestBuilder<Drawable> drawableTypeRequest;

                if (isUrl) {
                    drawableTypeRequest = requestManager.load(imageUrl);
                } else if (isUri) {
                    drawableTypeRequest = requestManager.load(uri);
                } else if (isFile) {
                    drawableTypeRequest = requestManager.load(file);
                } else if (isBitmap) {
                    drawableTypeRequest = requestManager.load(bitmap);
                } else {
                    drawableTypeRequest = requestManager.load(resId);
                }

                RequestOptions options = new RequestOptions();
                options.error(errorId);

                if (hasPlaceHolder) {
                    options.placeholder(placeHolderId);
                }


                if (isOval) {
                    if (!applyTint) {
                        options.circleCrop();
                    } else {
                        options.transforms(new ColorFilterTransformation(ContextCompat.getColor(context, tintColorId)), new CircleCrop());
                    }
                } else {
                    if (applyTint) {
                        options.transform(new ColorFilterTransformation(ContextCompat.getColor(context, tintColorId)));
                    }
                }

                drawableTypeRequest.apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);

            } catch (Exception ignored) {
            }
        }


    }
}
