package com.bobo.gmargiani.bobo.ui.activites;

import android.content.Context;
import android.content.Intent;

import com.bobo.gmargiani.bobo.R;

public class ManageSubscriptionsActivity extends AuthorizedActivity {

    public static void start(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, ManageSubscriptionsActivity.class));
        }
    }

    @Override
    public void userIsLoggedIn() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_manage_subscriptions;
    }

    @Override
    protected String getHeaderText() {
        return getString(R.string.activity_name_manage_subscriptions);
    }
}
