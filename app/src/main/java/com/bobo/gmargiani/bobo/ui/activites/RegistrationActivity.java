package com.bobo.gmargiani.bobo.ui.activites;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.utils.AlertManager;
import com.bobo.gmargiani.bobo.utils.ImageLoader;
import com.bobo.gmargiani.bobo.utils.Utils;
import com.bobo.gmargiani.bobo.utils.ViewUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class RegistrationActivity extends RootDetailedActivity {

    @BindView(R.id.user_profile_picture)
    ImageView userAvatar;

    @BindView(R.id.use_profile_picture_wrapper)
    View userAvatarWrapper;

    @BindView(R.id.user_name)
    EditText userName;

    @BindView(R.id.user_last_name)
    EditText userLastName;

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

    private ArrayList<EditText> inputs = new ArrayList<>();

    public static void start(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, RegistrationActivity.class));
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
    }

    @OnClick(R.id.register_button)
    public void onRegisterClick() {
        if (validateInputs()) {

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
                    AlertManager.showError(RegistrationActivity.this, "გთხოვტ შეიყვანოთ სწორი ელ. ფოსტის მისამართი");
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
        return false;
    }

    @Override
    protected String getHeaderText() {
        return getString(R.string.activity_name_registration);
    }
}
