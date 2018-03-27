package com.bobo.gmargiani.bobo.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * Created by gmargiani on 3/27/2018.
 */
public abstract class BaseDialog extends Dialog {

    public BaseDialog(Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (layoutId() > 0) {
            setContentView(layoutId());
            ButterKnife.bind(this);
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }


    protected abstract int layoutId();

}