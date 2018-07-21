package com.bobo.gmargiani.bobo.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.app.App;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.ActivityResultEvent;
import com.bobo.gmargiani.bobo.model.LogInData;
import com.bobo.gmargiani.bobo.model.OwnerDetails;
import com.bobo.gmargiani.bobo.model.Token;
import com.bobo.gmargiani.bobo.rest.ApiResponse;
import com.bobo.gmargiani.bobo.rest.RestCallback;
import com.bobo.gmargiani.bobo.ui.activites.RegistrationActivity;
import com.bobo.gmargiani.bobo.utils.AppConsts;
import com.bobo.gmargiani.bobo.utils.PreferencesApiManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by gmargiani on 3/27/2018.
 */

public class AuthorizationDialog extends BaseDialog implements View.OnClickListener {
    private View closeButton;
    private Button registerButton;
    private EditText passwordView;
    private EditText emailView;
    private String mail;
    private Activity activity;
    private View logInbutton;
    private View loader;
    private TextView errorText;

    public AuthorizationDialog(Activity activity, String mail) {
        super(activity);
        this.mail = mail;
        this.activity = activity;
        findViews();

    }

    private void findViews() {
        closeButton = findViewById(R.id.close_button);
        registerButton = findViewById(R.id.register_button);

        closeButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        passwordView = findViewById(R.id.password);
        emailView = findViewById(R.id.email);
        logInbutton = findViewById(R.id.log_in_button);
        loader = findViewById(R.id.full_loader);
        errorText = findViewById(R.id.error_text);

        logInbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.setVisibility(View.VISIBLE);
                App.getNetApi().logIn(emailView.getText().toString(), passwordView.getText().toString(),
                        new RestCallback<ApiResponse<LogInData>>() {
                            @Override
                            public void onResponse(ApiResponse<LogInData> response) {
                                super.onResponse(response);
                                if (response.isSuccess() && response.getResult() != null && response.getResult().getUserDetails() != null &&
                                        !TextUtils.isEmpty(response.getResult().getToken())) {
                                    ApiResponse<OwnerDetails> resp = new ApiResponse<>();
                                    resp.setResult(response.getResult().getUserDetails());
                                    resp.setMessage("OK");
                                    resp.setCode("0");
                                    App.getInstance().getUserInfo().onAuthorizeByTokenEvent(resp, response.getResult().getToken());
                                } else if (response.getMessage() != null && response.getMessage().contains("Incorrect")) {
                                    loader.setVisibility(View.GONE);
                                    errorText.setText(R.string.error_pass_mail);
                                    errorText.setTextColor(ContextCompat.getColor(getContext(), R.color.error_red_color));
                                } else {
                                    loader.setVisibility(View.GONE);
                                    errorText.setText(getContext().getString(R.string.common_text_error));
                                    errorText.setTextColor(ContextCompat.getColor(getContext(), R.color.error_red_color));
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                super.onFailure(t);
                                loader.setVisibility(View.GONE);
                                errorText.setText(getContext().getString(R.string.common_text_error));
                                errorText.setTextColor(ContextCompat.getColor(getContext(), R.color.error_red_color));
                            }
                        });
            }
        });

        if (!TextUtils.isEmpty(mail)) {
            emailView.setText(mail);
            errorText.setText(getContext().getString(R.string.common_text_registration_succesful));
            errorText.setVisibility(View.VISIBLE);
            errorText.setTextColor(ContextCompat.getColor(getContext(), R.color.success_green_color));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == closeButton) {
            cancel();
        } else if (v == registerButton) {
            RegistrationActivity.start(activity);
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.dialog_authorization;
    }

}
