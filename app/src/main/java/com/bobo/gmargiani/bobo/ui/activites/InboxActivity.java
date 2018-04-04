package com.bobo.gmargiani.bobo.ui.activites;

import com.bobo.gmargiani.bobo.R;

public class InboxActivity extends AuthorizedActivity {

    @Override
    public void userIsAuthorized() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_inbox;
    }

    @Override
    protected int getHeaderText() {
        return R.string.activity_name_inbox;
    }
}
