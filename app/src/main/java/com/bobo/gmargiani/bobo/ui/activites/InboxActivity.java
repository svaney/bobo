package com.bobo.gmargiani.bobo.ui.activites;

import android.content.Context;
import android.content.Intent;

import com.bobo.gmargiani.bobo.R;

public class InboxActivity extends AuthorizedActivity {

    public static void start(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, InboxActivity.class));
        }
    }

    @Override
    public void userIsLoggedIn() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_inbox;
    }

    @Override
    protected String getHeaderText() {
        return getString(R.string.activity_name_inbox);
    }
}
