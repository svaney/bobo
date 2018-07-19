package com.bobo.gmargiani.bobo.ui.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.app.App;
import com.bobo.gmargiani.bobo.rest.ApiResponse;
import com.bobo.gmargiani.bobo.rest.RestCallback;
import com.bobo.gmargiani.bobo.utils.AlertManager;
import com.bobo.gmargiani.bobo.utils.AppConsts;
import com.bobo.gmargiani.bobo.utils.ImageLoader;
import com.bobo.gmargiani.bobo.utils.Utils;
import com.bobo.gmargiani.bobo.utils.ViewUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class RegistrationActivity extends RootDetailedActivity {

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

    private ArrayList<EditText> inputs = new ArrayList<>();

    public static void start(Activity context) {
        if (context != null) {
            context.startActivityForResult(new Intent(context, RegistrationActivity.class), AppConsts.RC_REGISTER);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.OVAL);
        bg.setColor(ContextCompat.getColor(this, R.color.color_accent_light));

        userAvatarWrapper.setBackground(bg);
        ImageLoader.load(userAvatar)
                .setRes(R.drawable.ic_avatar_default)
                .setOval(true)
                .build();


        inputs.add(userName);
        inputs.add(userLastName);
        inputs.add(userMail);
        inputs.add(userPhone);
        inputs.add(userPassword);
        inputs.add(userPasswordSecond);


        companyName.setVisibility(View.GONE);

        isCompany.setChecked(false);

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


    }

    @OnClick(R.id.register_button)
    public void onRegisterClick() {
        if (validateInputs()) {
            showFullLoading();
            App.getNetApi().registerUser(isCompany.isChecked(), userName.getText().toString(), userLastName.getText().toString(),
                    companyName.getText().toString(), userPassword.getText().toString(),  userMail.getText().toString(), userPhone.getText().toString(),
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
                                AlertManager.showError(RegistrationActivity.this, getString(R.string.register_error_duplicate_user));
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
                        AlertManager.showError(RegistrationActivity.this, "გთხოვთ შეავსოთ ყველა ველი");
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
                    AlertManager.showError(RegistrationActivity.this, "პაროლები არ ემთხვევა ერთმანეთს");
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
                    AlertManager.showError(RegistrationActivity.this, "გთხოვთ შეიყვანოთ სწორი ელ. ფოსტის მისამართი");
                    ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(RegistrationActivity.this, R.color.error_red_color));
                    ViewCompat.setBackgroundTintList(userMail, colorStateList);
                    ViewUtils.shakeView(userMail);
                }
            });
            return false;
        }

        if (userPassword.getText().length() < 6) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(0, scrollView.getBottom());
                    AlertManager.showError(RegistrationActivity.this, "პაროლის სიგრძე უნდა აღემატებოდეს 6 სიმბოლოს");
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
        return getString(R.string.activity_name_registration);
    }
}
