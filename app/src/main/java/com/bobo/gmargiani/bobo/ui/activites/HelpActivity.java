package com.bobo.gmargiani.bobo.ui.activites;

import android.content.Context;
import android.content.Intent;

import com.bobo.gmargiani.bobo.R;

public class HelpActivity extends RootDetailedActivity {

    public static void start(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, HelpActivity.class));
        }
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    public boolean needEventBus() {
        return false;
    }

    @Override
    protected String getHeaderText() {
        return getString(R.string.activity_name_help);
    }
}
