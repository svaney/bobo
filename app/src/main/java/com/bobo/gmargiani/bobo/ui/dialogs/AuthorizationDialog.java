package com.bobo.gmargiani.bobo.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.bobo.gmargiani.bobo.R;

/**
 * Created by gmargiani on 3/27/2018.
 */

public class AuthorizationDialog extends BaseDialog {
    public AuthorizationDialog(Context context) {
        super(context);
    }

    @Override
    protected int layoutId() {
        return R.layout.dialog_authorization;
    }
}
