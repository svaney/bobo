package com.bobo.gmargiani.bobo.ui.activites;

import com.bobo.gmargiani.bobo.R;

public class NewStatementActivity extends AuthorizedActivity {
    @Override
    public void userIsAuthorized() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_statement;
    }

    @Override
    protected int getHeaderText() {
        return R.string.activity_name_new_statement;
    }
}
