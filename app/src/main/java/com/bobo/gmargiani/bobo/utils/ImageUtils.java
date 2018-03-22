package com.bobo.gmargiani.bobo.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;

import com.bobo.gmargiani.bobo.BuildConfig;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.ActivityResultEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.GrantedPermissionsEvent;
import com.bobo.gmargiani.bobo.utils.consts.AppConsts;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by gmargiani on 2/1/2018.
 */

public class ImageUtils {

// ImageUtils.manageGallery(getActivity());
  //  ImageUtils.manageCamera(getActivity());


 /*   @Subscribe
    public void onPermissionsGranted(GrantedPermissionsEvent event) {
        if (event.getRequestCode() == AppConsts.PERMISSION_CAMERA) {
            ImageUtils.openCamera(getActivity());
        } else if (event.getRequestCode() == AppConsts.PERMISSION_EXTERNAL_STORAGE) {
            ImageUtils.openGallery(getActivity());
        }
    }

    */


  /*  @Subscribe
    public void onActivityResult(ActivityResultEvent event) {
        switch (event.getResultCode()) {
            case RESULT_OK:

                if (event.getRequestCode() == AppConsts.RC_CAMERA || event.getRequestCode() == AppConsts.RC_GALLERY) {
                    String path = null;
                    if (event.getRequestCode() == AppConsts.RC_CAMERA) {
                        path = ImageUtils.handleCameraResult(getActivity());
                    } else if (event.getRequestCode() == AppConsts.RC_GALLERY) {
                        path = ImageUtils.handleGalleryResult(event.getData(), getActivity());
                    }

                    if (!TextUtils.isEmpty(path)) {
                        File imgFile = new File(path);
                        if (imgFile.exists()) {
                            this.file = imgFile;
                            setPicture();
                        } else {
                            setPicture();
                        }
                    } else {
                        setPicture();
                    }

                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }
                break;
        }
    }

    */
    @SuppressLint("NewApi")
    public static void manageCamera(Activity activity) {
        if (AppUtils.atLeastMarshmallow()) {
            if (!AppUtils.hasPermission(activity, Manifest.permission.CAMERA)) {
                requestPermission(activity, Manifest.permission.CAMERA, AppConsts.PERMISSION_CAMERA);
            } else {
                openCamera(activity);
            }
        } else {
            openCamera(activity);
        }
    }

    @SuppressLint("NewApi")
    public static void manageGallery(Activity activity) {
        if (AppUtils.atLeastMarshmallow()) {
            if (!AppUtils.hasPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    || !AppUtils.hasPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        AppConsts.PERMISSION_EXTERNAL_STORAGE);
            } else {
                openGallery(activity);
            }
        } else {
            openGallery(activity);
        }
    }

    public static void openGallery(Activity activity) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(galleryIntent, AppConsts.RC_GALLERY);
    }

    public static void openCamera(Activity activity) {
        File temporaryFile = getTemporaryFile(activity);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);

        Uri uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", temporaryFile);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            activity.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }


        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        cameraIntent.putExtra("path", temporaryFile.getAbsolutePath());

        activity.startActivityForResult(cameraIntent, AppConsts.RC_CAMERA);
    }

    public static String handleCameraResult(Activity activity) {
        File f = getTemporaryFile(activity);
        counter++;
        try {
            f = fixImage(f, null, -1, activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f.getAbsolutePath();
    }

    public static String handleGalleryResult(Intent data, Activity activity) {
        File file = null;
        try {
            Bitmap b = getBitmapFromUriOrFile(data.getData(), null, activity);
            Cursor cursor = getOrientation(data.getData(), activity);
            if (cursor != null) {
                int orientation = cursor.getInt(0);
                File f = getTemporaryFile(activity);
                counter++;
                cursor.close();
                file = fixImage(f, b, orientation, activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file == null ? null : file.getAbsolutePath();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void requestPermission(Activity activity, String permission, int permissionCode) {
        requestPermissions(activity, new String[]{permission}, permissionCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void requestPermissions(Activity activity, String[] permission, int permissionCode) {
        activity.requestPermissions(permission, permissionCode);
    }

    private static Cursor getOrientation(Uri photoUri, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(photoUri, new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);
        if (cursor != null && cursor.getCount() != 1) {
            return null;
        }
        cursor.moveToFirst();
        return cursor;
    }

    public static Bitmap getBitmapFromUriOrFile(Uri uri, File file, Activity activity) throws Exception {
        InputStream input;

        if (uri != null)
            input = activity.getContentResolver().openInputStream(uri);
        else input = new FileInputStream(file);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;

        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        if (input != null)
            input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > AppUtils.getDisplayWidth(activity)) ? (originalSize / AppUtils.getDisplayWidth(activity)) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);

        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

        if (uri != null)
            input = activity.getContentResolver().openInputStream(uri);
        else input = new FileInputStream(file);

        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);

        if (input != null)
            input.close();

        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }

    private static int counter = 0;

    public static File getTemporaryFile(Activity activity) {
        return new File(activity.getExternalCacheDir() + "/photo" + counter + ".jpg");
    }

    //bmp is null if camera result
    private static File fixImage(File imageFile, @Nullable Bitmap bmp, int orientation, Activity activity) throws Exception {
        Bitmap bitmap;
        if (bmp == null) {
            bitmap = getBitmapFromUriOrFile(null, imageFile, activity);
            bitmap = fixExif(imageFile.getAbsolutePath(), bitmap, -1);
        } else {
            bitmap = bmp;

            saveBitmap(imageFile, bitmap, 100);

            bitmap = fixExif(imageFile.getAbsolutePath(), bitmap, orientation);
        }

        File file = saveBitmap(imageFile, bitmap);
        if (bmp != null)
            bmp.recycle();
        bitmap.recycle();

        return file;
    }

    private static Bitmap fixExif(String path, Bitmap bmp, int orientation) throws Exception {
        ExifInterface exif = new ExifInterface(path);
        int rotate = 0;

        if (orientation < 0) {
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
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
        } else {
            rotate = orientation;
        }

        if (rotate > 1) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotate);
            bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            return bmp;
        }

        return bmp;
    }

    private static File saveBitmap(File imageFile, Bitmap bmp) throws Exception {
        return saveBitmap(imageFile, bmp, 80);
    }

    private static File saveBitmap(File imageFile, Bitmap bmp, int quality) throws Exception {

        File file = new File(imageFile.getAbsolutePath());

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        FileOutputStream fOut = new FileOutputStream(imageFile, false);
        bmp.compress(Bitmap.CompressFormat.JPEG, quality, fOut);
        fOut.flush();
        fOut.close();
        return file;
    }

}
