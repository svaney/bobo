package com.bobo.gmargiani.bobo.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bobo.gmargiani.bobo.R;

/**
 * Created by gmargiani on 3/27/2018.
 */

public class AuthorizationDialog extends BaseDialog implements View.OnClickListener {
    private View closeButton;

    public AuthorizationDialog(Context context) {
        super(context);
        findViews();
    }

    private void findViews() {
        closeButton = findViewById(R.id.close_button);

        closeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == closeButton) {
            cancel();
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.dialog_authorization;
    }

}
