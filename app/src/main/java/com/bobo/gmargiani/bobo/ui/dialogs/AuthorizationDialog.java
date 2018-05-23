package com.bobo.gmargiani.bobo.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.ui.activites.RegistrationActivity;

/**
 * Created by gmargiani on 3/27/2018.
 */

public class AuthorizationDialog extends BaseDialog implements View.OnClickListener {
    private View closeButton;
    private Button registerButton;

    public AuthorizationDialog(Context context) {
        super(context);
        findViews();
    }

    private void findViews() {
        closeButton = findViewById(R.id.close_button);
        registerButton = findViewById(R.id.register_button);

        closeButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == closeButton) {
            dismiss();
        } else if (v == registerButton) {
            dismiss();
            RegistrationActivity.start(getContext());
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.dialog_authorization;
    }

}
