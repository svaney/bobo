package com.bobo.gmargiani.bobo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.bobo.gmargiani.bobo.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImagePicker {
    public static final int REQUEST_PICK = 7352;
    public static final int REQUEST_CROP = UCrop.REQUEST_CROP;
    private static final String TAG = "ImagePicker";
    private static final String TEMP_IMAGE_NAME = "tempImage";
    private static final String CROPPED_IMAGE_NAME = "croppedImage";

    private static final boolean DEBUG_LOG = false;

    private static boolean isCamera;
    private static Uri selectedImage;

    public enum ResizeType {
        /**
         * Resize to have image less than max size.
         * Can cause smaller images.
         */
        MAX_SIZE,
        /**
         * Scaled to exact size.
         */
        FIXED_SIZE,
        NO_RESIZE
    }

    public static void pickImageFromCamera(Activity activity, @StringRes int noSuitableAppMessage) {
        Intent intent = getPickFromCameraIntent(activity);
        startActivityForResultFrom(activity, intent, noSuitableAppMessage);
    }

    public static void pickImageFromGallery(Activity activity, @StringRes int noSuitableAppMessage) {
        Intent intent = getPickFromGalleryIntent();
        startActivityForResultFrom(activity, intent, noSuitableAppMessage);
    }

    public static void pickImageUsingChooser(Activity activity, @StringRes int chooserTitle, @StringRes int noSuitableAppMessage) {
        Intent intent = getPickWithChooserIntent(activity, chooserTitle);
        startActivityForResultFrom(activity, intent, noSuitableAppMessage);
    }

    private static void startActivityForResultFrom(Activity activity, Intent intent, @StringRes int noSuitableAppMessage) {
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, REQUEST_PICK);
        } else {
            Toast.makeText(activity, noSuitableAppMessage, Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    private static Intent getPickWithChooserIntent(Context context, @StringRes int chooserTitle) {
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = getPickFromGalleryIntent();
        Intent takePhotoIntent = getPickFromCameraIntent(context);
        addIntentsToList(context, intentList, pickIntent);
        addIntentsToList(context, intentList, takePhotoIntent);

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(
                    intentList.remove(intentList.size() - 1),
                    context.getString(chooserTitle));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[intentList.size()]));
        }

        return chooserIntent;
    }

    @NonNull
    private static Intent getPickFromCameraIntent(Context context) {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra("return-data", true);
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(context)));
        return takePhotoIntent;
    }

    @NonNull
    private static Intent getPickFromGalleryIntent() {
        return new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    private static void addIntentsToList(Context context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
            if (DEBUG_LOG) {
                Log.d(TAG, "Intent: " + intent.getAction() + " package: " + packageName);
            }
        }
    }

    public static boolean isImageCroppedSuccessfully(int requestCode, int resultCode) {
        return requestCode == ImagePicker.REQUEST_CROP
                && resultCode == Activity.RESULT_OK;
    }

    public static boolean wasErrorWhileCropping(int requestCode, int resultCode, Intent data) {
        boolean wasError = requestCode == ImagePicker.REQUEST_CROP
                && resultCode == UCrop.RESULT_ERROR;
        if (wasError) {
            Log.w(TAG, "Was error while cropping.", UCrop.getError(data));
        }
        return wasError;
    }

    public static boolean isImagePickedSuccessfully(int requestCode, int resultCode) {
        return requestCode == ImagePicker.REQUEST_PICK
                && resultCode == Activity.RESULT_OK;
    }

    /**
     * Simpler version of {@link this#beginCrop(Activity, int, Intent, int)} with no {@code maxCroppedSize} set.
     */
    public static void beginCrop(Activity activity, int resultCode, Intent imageReturnedIntent) {
        beginCrop(activity, resultCode, imageReturnedIntent, 0);
    }

    /**
     * Start cropping library activity.
     *
     * @param maxCroppedSize to use library version of size constraining.
     *                       If you want, you can use the resizing options
     *                       of the {@link this#getCroppedImage(Context, int, Intent, ResizeType, int)} method instead.
     */
    public static void beginCrop(Activity activity, int resultCode, Intent imageReturnedIntent, int maxCroppedSize) {
        Context context = activity;
        Uri selectedImage = getImageUri(context, resultCode, imageReturnedIntent);
        if (selectedImage != null) {
            Uri destination = Uri.fromFile(new File(context.getCacheDir(), CROPPED_IMAGE_NAME));
            UCrop crop = UCrop.of(selectedImage, destination).withAspectRatio(1, 1).withOptions(createCropOptions(context));
            if (maxCroppedSize > 0) {
                crop = crop.withMaxResultSize(maxCroppedSize, maxCroppedSize);
            }
            crop.start(activity);
        }
    }

    public static UCrop.Options createCropOptions(Context context) {
        UCrop.Options options = new UCrop.Options();

        options.setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        options.setToolbarColor(ContextCompat.getColor(context, R.color.color_black));
        options.setActiveWidgetColor(ContextCompat.getColor(context, R.color.colorAccent));
        options.setToolbarTitle("სურათი");
        options.setFreeStyleCropEnabled(false);
        options.useSourceImageAspectRatio();
        options.setShowCropGrid(false);

        options.setHideBottomControls(true);
        return options;
    }

    private static Uri getImageUri(Context context, int resultCode, Intent imageReturnedIntent) {
        if (DEBUG_LOG) {
            Log.d(TAG, "getImageUri, resultCode: " + resultCode);
        }
        File imageFile = getTempFile(context);
        if (resultCode == Activity.RESULT_OK) {
            isCamera = (imageReturnedIntent == null ||
                    imageReturnedIntent.getData() == null ||
                    imageReturnedIntent.getData().equals(Uri.fromFile(imageFile)));
            if (isCamera) {     /** CAMERA **/
                selectedImage = Uri.fromFile(imageFile);
            } else {            /** ALBUM **/
                selectedImage = imageReturnedIntent.getData();
            }
            if (DEBUG_LOG) {
                Log.d(TAG, "selectedImage: " + selectedImage);
            }
        }
        return selectedImage;
    }

    /**
     * Simpler version of {@link this#getCroppedImage(Context, int, Intent, ResizeType, int)} with no resizing.
     */
    public static Bitmap getCroppedImage(Context context, int resultCode, Intent result) {
        return getCroppedImage(context, resultCode, result, ResizeType.NO_RESIZE, 0);
    }

    @Nullable
    public static Bitmap getCroppedImage(Context context, int resultCode, Intent result,
                                         ResizeType resizeType, int size) {
        Bitmap bm = null;
        if (resultCode == Activity.RESULT_OK) {
            Uri croppedImageUri = UCrop.getOutput(result);
            if (croppedImageUri != null) {
                bm = getImageResized(context, croppedImageUri, resizeType, size);
            }
        }
        return bm;
    }

    @Nullable
    public static File getCroppedImageFile(int resultCode, Intent result) {
        File file = null;
        if (resultCode == Activity.RESULT_OK) {
            Uri croppedImageUri = UCrop.getOutput(result);
            if (croppedImageUri != null) {
                file = new File(croppedImageUri.getPath());
            }
        }
        return file;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static File getTempFile(Context context) {
        File imageFile = new File(context.getExternalCacheDir(), TEMP_IMAGE_NAME);
        imageFile.getParentFile().mkdirs();
        return imageFile;
    }

    /**
     * Resize to avoid using too much memory
     **/
    private static Bitmap getImageResized(Context context, Uri selectedImageUri,
                                          ResizeType resizeType, int targetSize) {
        Bitmap bitmap = null;

        // Get file descriptor
        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(selectedImageUri, "r");
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Can't find file", e);
        }

        if (fileDescriptor != null) {

            BitmapFactory.Options options = null;

            // If max size - calculate options inSampleSize
            if (ResizeType.MAX_SIZE == resizeType) {
                // Get the dimensions of the bitmap
                options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;

                BitmapFactory.decodeFileDescriptor(
                        fileDescriptor.getFileDescriptor(), null, options);

                int imageW = options.outWidth;
                int imageH = options.outHeight;

                // Determine how much to scale down the image (less than max size)
                int scaleFactor = Math.round(Math.min((float) imageW / targetSize, (float) imageH / targetSize));

                // Decode the image file into a Bitmap scaled down
                options.inJustDecodeBounds = false;
                options.inSampleSize = scaleFactor;
            }

            bitmap = BitmapFactory.decodeFileDescriptor(
                    fileDescriptor.getFileDescriptor(), null, options);

            // If fixed - scale the bitmap
            if (ResizeType.FIXED_SIZE == resizeType) {
                bitmap = Bitmap.createScaledBitmap(
                        bitmap, targetSize, targetSize, true);
            }

            //noinspection PointlessBooleanExpression
            if (DEBUG_LOG && bitmap != null) {
                Log.d(TAG, "Image size: Width=" + bitmap.getWidth() + ", Height=" + bitmap.getHeight());
            }

            // Close descriptor
            try {
                fileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    private static int getRotation(Context context, Uri imageUri, boolean isCamera) {
        int rotation;
        if (isCamera) {
            rotation = getRotationFromCamera(context, imageUri);
        } else {
            rotation = getRotationFromGallery(context, imageUri);
        }
        if (DEBUG_LOG) {
            Log.d(TAG, "Image rotation: " + rotation);
        }
        return rotation;
    }

    private static int getRotationFromCamera(Context context, Uri imageFile) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageFile, null);
            ExifInterface exif = new ExifInterface(imageFile.getPath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static int getRotationFromGallery(Context context, Uri imageUri) {
        String[] columns = {MediaStore.Images.Media.ORIENTATION};
        Cursor cursor = context.getContentResolver().query(imageUri, columns, null, null, null);
        if (cursor == null) return 0;

        cursor.moveToFirst();

        int orientationColumnIndex = cursor.getColumnIndex(columns[0]);
        int rotation = cursor.getInt(orientationColumnIndex);
        cursor.close();
        return rotation;
    }


    private static Bitmap rotate(Bitmap bm, int rotation) {
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        }
        return bm;
    }
}
