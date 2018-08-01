package com.bobo.gmargiani.bobo.ui.activites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.app.App;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.ActivityResultEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.DeniedPermissionsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.GrantedPermissionsEvent;
import com.bobo.gmargiani.bobo.model.OwnerDetails;
import com.bobo.gmargiani.bobo.rest.ApiResponse;
import com.bobo.gmargiani.bobo.rest.RestCallback;
import com.bobo.gmargiani.bobo.ui.views.NewImageView;
import com.bobo.gmargiani.bobo.utils.AlertManager;
import com.bobo.gmargiani.bobo.utils.AppConsts;
import com.bobo.gmargiani.bobo.utils.AppUtils;
import com.bobo.gmargiani.bobo.utils.ImageLoader;
import com.bobo.gmargiani.bobo.utils.ImagePicker;
import com.bobo.gmargiani.bobo.utils.PreferencesApiManager;
import com.bobo.gmargiani.bobo.utils.Utils;
import com.bobo.gmargiani.bobo.utils.ViewUtils;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class RegistrationActivity extends RootDetailedActivity implements NewImageView.NewImageListener {

    @BindView(R.id.user_profile_picture)
    ImageView userAvatar;

    @BindView(R.id.company_check)
    CheckBox isCompany;

    @BindView(R.id.use_profile_picture_wrapper)
    View userAvatarWrapper;

    @BindView(R.id.user_name)
    EditText userName;

    @BindView(R.id.user_last_name)
    EditText userLastName;

    @BindView(R.id.company_name)
    EditText companyName;

    @BindView(R.id.user_mail)
    EditText userMail;

    @BindView(R.id.user_phone)
    EditText userPhone;

    @BindView(R.id.user_password)
    EditText userPassword;

    @BindView(R.id.user_pass_wrapper)
    TextInputLayout userpasswrapper;

    @BindView(R.id.user_password_second)
    EditText userPasswordSecond;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.user_name_wrapper)
    View userNameWrapper;

    @BindView(R.id.user_last_name_wrapper)
    View lastNameWrapper;

    @BindView(R.id.company_wrapper)
    View companyWrapper;

    @BindView(R.id.register_button)
    Button registerButton;


    private OwnerDetails ownerDetails;

    private ArrayList<EditText> inputs = new ArrayList<>();

    public static void start(Activity context, OwnerDetails ownerDetails) {
        if (context != null) {
            Intent intent = new Intent(context, RegistrationActivity.class);
            if (ownerDetails != null) {
                intent.putExtra(AppConsts.PARAM_OWNER, Parcels.wrap(ownerDetails));
                context.startActivity(intent);
            } else {
                context.startActivityForResult(intent, AppConsts.RC_REGISTER);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ownerDetails = Parcels.unwrap(getIntent().getParcelableExtra(AppConsts.PARAM_OWNER));

        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.OVAL);
        bg.setColor(ContextCompat.getColor(this, R.color.color_accent_light));

        userAvatarWrapper.setBackground(bg);

        isCompany.setEnabled(ownerDetails != null ? false : true);
        userMail.setEnabled(ownerDetails != null ? false : true);

        if (ownerDetails != null && !TextUtils.isEmpty(ownerDetails.getAvatar())) {
            ImageLoader.load(userAvatar)
                    .setUrl(ownerDetails.getAvatar(), true)
                    .setOval(true)
                    .setTarget(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                            try {
                                File f = new File(RegistrationActivity.this.getCacheDir(), "imageFile");
                                f.createNewFile();

                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                                byte[] bitmapdata = bos.toByteArray();


                                FileOutputStream fos = new FileOutputStream(f);
                                fos.write(bitmapdata);
                                fos.flush();
                                fos.close();

                                imageFile = f;

                                ImageLoader.load(userAvatar)
                                        .setUrl(ownerDetails.getAvatar())
                                        .setOval(true)
                                        .build();
                            } catch (Exception ignored) {
                                ignored.printStackTrace();
                            }
                        }
                    })
                    .build();

        } else {
            ImageLoader.load(userAvatar)
                    .setRes(R.drawable.ic_avatar_default)
                    .setOval(true)
                    .build();
        }


        if (ownerDetails != null) {
            userName.setText(ownerDetails.getOwnerName());
            userLastName.setText(ownerDetails.getOwnerSecondName());
            userMail.setText(ownerDetails.getEmail());
            userPhone.setText(ownerDetails.getPhone());
            companyName.setText(ownerDetails.getCompanyName());
            //  userPassword.setHint(getString(R.string.new_password));
            userpasswrapper.setHint(getString(R.string.new_password));
            registerButton.setText(getString(R.string.save));
        } else {
            isCompany.setChecked(false);
        }
        inputs.add(userName);
        inputs.add(userLastName);
        inputs.add(userMail);
        inputs.add(userPhone);
        inputs.add(userPassword);
        inputs.add(userPasswordSecond);

        companyName.setVisibility(View.GONE);


        isCompany.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                companyName.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                userName.setVisibility(isChecked ? View.GONE : View.VISIBLE);
                userLastName.setVisibility(isChecked ? View.GONE : View.VISIBLE);

                companyWrapper.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                userNameWrapper.setVisibility(isChecked ? View.GONE : View.VISIBLE);
                lastNameWrapper.setVisibility(isChecked ? View.GONE : View.VISIBLE);

                if (isChecked) {
                    inputs.remove(userLastName);
                    inputs.remove(userName);
                    inputs.add(0, companyName);
                } else {
                    inputs.add(0, userLastName);
                    inputs.add(0, userName);
                    inputs.remove(companyName);
                }
            }
        });
        if (ownerDetails != null) {
            isCompany.setChecked(ownerDetails.isCompany());
            inputs.remove(userMail);
            inputs.remove(userPassword);
            inputs.remove(userPasswordSecond);
        }


        userAvatarWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImageClick(0);
            }
        });
        refreshHeaderText();
    }


    Bitmap bitmap;
    File imageFile;

    @Subscribe
    public void onActivityResultEvent(ActivityResultEvent event) {
        if (ImagePicker.isImagePickedSuccessfully(event.getRequestCode(), event.getResultCode())) {
            ImagePicker.beginCrop(this, event.getResultCode(), event.getData(), 0);
        } else if (ImagePicker.isImageCroppedSuccessfully(event.getRequestCode(), event.getResultCode())) {
            bitmap = ImagePicker.getCroppedImage(this, event.getResultCode(), event.getData());
            imageFile = ImagePicker.getCroppedImageFile(event.getResultCode(), event.getData());
            ImageLoader.load(userAvatar)
                    .setBitmap(bitmap)
                    .setOval(true)
                    .build();
        }
    }

    @Override
    public void onCloseImageClick(int position) {

    }

    @SuppressLint("NewApi")
    @Override
    public void onImageClick(int pos) {
        if (!AppUtils.hasPermission(this, Manifest.permission.CAMERA) && !permissionRequested) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, AppConsts.PERMISSION_CAMERA);
        } else {
            permissionRequested = false;
            ImagePicker.pickImageUsingChooser(RegistrationActivity.this, R.string.common_text_avatar, R.string.common_text_avatar);
        }
    }

    private boolean permissionRequested;

    @Subscribe
    public void onGrantedPermissionEvent(GrantedPermissionsEvent event) {
        if (event.getGrantedPermissions().size() > 0) {
            if (event.getRequestCode() == AppConsts.PERMISSION_CAMERA) {
                onImageClick(0);
            }
        }
    }

    @Subscribe
    public void onDeniedPermissionEvent(DeniedPermissionsEvent event) {
        if (event.getDeniedPermissions().size() > 0) {
            if (event.getRequestCode() == AppConsts.PERMISSION_CAMERA) {
                permissionRequested = true;
                onImageClick(0);
                AlertManager.showError(this, getString(R.string.no_image_permission), AlertManager.VERY_LONG);
            }
        }
    }


    @OnClick(R.id.register_button)
    public void onRegisterClick() {
        if (validateInputs()) {
            showFullLoading();
            if (ownerDetails != null) {

                App.getNetApi().updateUser(!TextUtils.isEmpty(companyName.getText().toString()) ? companyName.getText().toString() : null,
                        !TextUtils.isEmpty(userName.getText().toString()) ? userName.getText().toString() : null,
                        !TextUtils.isEmpty(userLastName.getText().toString()) ? userLastName.getText().toString() : null,
                        !TextUtils.isEmpty(userPhone.getText().toString()) ? userPhone.getText().toString() : null,
                        !TextUtils.isEmpty(userPassword.getText().toString()) ? userLastName.getText().toString() : null,
                        imageFile,
                        new RestCallback<ApiResponse<OwnerDetails>>() {
                            @Override
                            public void onResponse(ApiResponse<OwnerDetails> response) {
                                super.onResponse(response);

                                if (response.isSuccess()) {
                                    AlertManager.showInfo(RegistrationActivity.this, RegistrationActivity.this.getString(R.string.operation_succsessful));
                                    userInfo.onAuthorizeByTokenEvent(response, PreferencesApiManager.getInstance().getToken());
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    }, 2000);
                                } else {
                                    showContent();

                                    AlertManager.showError(RegistrationActivity.this, RegistrationActivity.this.getString(R.string.common_text_error));
                                }

                            }

                            @Override
                            public void onFailure(Throwable t) {
                                super.onFailure(t);
                                showContent();
                                AlertManager.showError(RegistrationActivity.this, RegistrationActivity.this.getString(R.string.common_text_error));
                            }
                        });
            } else {
                App.getNetApi().registerUser(isCompany.isChecked(), userName.getText().toString(), userLastName.getText().toString(),
                        companyName.getText().toString(), userPassword.getText().toString(), userMail.getText().toString(), userPhone.getText().toString(), imageFile,
                        new RestCallback<ApiResponse<Object>>() {
                            @Override
                            public void onResponse(ApiResponse<Object> response) {
                                super.onResponse(response);
                                showContent();
                                if (response.isSuccess()) {
                                    Intent intent = new Intent();
                                    intent.putExtra(AppConsts.PARAM_EMAIL, userMail.getText().toString());

                                    setResult(RESULT_OK, new Intent(intent));
                                    finish();

                                } else if (!TextUtils.isEmpty(response.getMessage()) && response.getMessage().contains("duplicate")) {
                                    AlertManager.showError(RegistrationActivity.this, RegistrationActivity.this.getString(R.string.register_error_duplicate_user));
                                } else {
                                    AlertManager.showError(RegistrationActivity.this, getString(R.string.common_text_error));
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                super.onFailure(t);
                                showContent();
                                AlertManager.showError(RegistrationActivity.this, getString(R.string.common_text_error));
                            }
                        });
            }
        }
    }

    private boolean validateInputs() {
        if (inputs == null) {
            return false;
        }
        for (final EditText e : inputs) {
            if (TextUtils.isEmpty(e.getText())) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0, e.getBottom());
                        AlertManager.showError(RegistrationActivity.this, getString(R.string.fill_all_values));
                        ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(RegistrationActivity.this, R.color.error_red_color));
                        ViewCompat.setBackgroundTintList(e, colorStateList);
                        ViewUtils.shakeView(e);
                    }
                });

                return false;
            }
        }

        if (!TextUtils.equals(userPassword.getText(), userPasswordSecond.getText())) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(0, scrollView.getBottom());
                    AlertManager.showError(RegistrationActivity.this, RegistrationActivity.this.getString(R.string.password_mismatch));
                    ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(RegistrationActivity.this, R.color.error_red_color));
                    ViewCompat.setBackgroundTintList(userPasswordSecond, colorStateList);
                    ViewUtils.shakeView(userPasswordSecond);
                }
            });
            return false;
        }

        if (TextUtils.isEmpty(userMail.getText()) || !android.util.Patterns.EMAIL_ADDRESS.matcher(userMail.getText()).matches()) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(0, userMail.getBottom());
                    AlertManager.showError(RegistrationActivity.this, getString(R.string.error_wrong_email));
                    ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(RegistrationActivity.this, R.color.error_red_color));
                    ViewCompat.setBackgroundTintList(userMail, colorStateList);
                    ViewUtils.shakeView(userMail);
                }
            });
            return false;
        }

        if ((ownerDetails == null && userPassword.getText().length() < 6) || (ownerDetails != null && !TextUtils.isEmpty(userPassword.getText().toString()))) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(0, scrollView.getBottom());
                    AlertManager.showError(RegistrationActivity.this, getString(R.string.error_short_password));
                    ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(RegistrationActivity.this, R.color.error_red_color));
                    ViewCompat.setBackgroundTintList(userPassword, colorStateList);
                    ViewUtils.shakeView(userPassword);
                }
            });
            return false;
        }

        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_registration;
    }

    @Override
    public boolean needEventBus() {
        return true;
    }

    @Override
    protected String getHeaderText() {
        if (ownerDetails != null) {
            return getString(R.string.activity_name_edit_profile);
        }
        return getString(R.string.activity_name_registration);
    }
}
