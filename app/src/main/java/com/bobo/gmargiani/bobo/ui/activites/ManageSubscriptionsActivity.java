package com.bobo.gmargiani.bobo.ui.activites;

import com.bobo.gmargiani.bobo.R;

public class ManageSubscriptionsActivity extends AuthorizedActivity {
    @Override
    public void userIsAuthorized() {

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
