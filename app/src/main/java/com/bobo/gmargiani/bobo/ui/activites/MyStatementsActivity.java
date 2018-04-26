package com.bobo.gmargiani.bobo.ui.activites;

import com.bobo.gmargiani.bobo.R;

public class MyStatementsActivity extends AuthorizedActivity {
    @Override
    public void userIsAuthorized() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_statements;
    }

    @Override
    protected String getHeaderText() {
        return getString(R.string.activity_name_my_statements);
    }
}
