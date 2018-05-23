package com.bobo.gmargiani.bobo.ui.activites;

import android.content.Context;
import android.content.Intent;

import com.bobo.gmargiani.bobo.R;

public class RegistrationActivity extends RootDetailedActivity {

    public static void start(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, RegistrationActivity.class));
        }
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
