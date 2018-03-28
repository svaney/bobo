package com.bobo.gmargiani.bobo.ui.activites;

import com.bobo.gmargiani.bobo.R;

public class FavoritesActivity extends AuthorizedActivity {

    @Override
    public void userIsAuthorized() {

    }

    @Override
    public void onAuthorizationDialogCancel() {
        finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_favorites;
    }

    @Override
    protected int getHeaderText() {
        return R.string.activity_name_favorites;
    }
}
