package com.bobo.gmargiani.bobo.ui.activites;

import android.content.Context;
import android.content.Intent;

import com.bobo.gmargiani.bobo.R;

public class NewStatementActivity extends AuthorizedActivity {

    public static void start(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, NewStatementActivity.class));
        }
    }

    @Override
    public void userIsAuthorized() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_statement;
    }

    @Override
    protected String getHeaderText() {
        return getString(R.string.activity_name_new_statement);
    }
}
