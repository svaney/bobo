package com.bobo.gmargiani.bobo.ui.activites;

import com.bobo.gmargiani.bobo.R;

public class FavoritesActivity extends AuthorizedActivity {

    @Override
    public void userIsAuthorized() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_favorites;
    }

    @Override
    protected String getHeaderText() {
        return getString(R.string.activity_name_favorites);
    }
}
