package com.bobo.gmargiani.bobo.ui.activites;

import android.content.Context;
import android.content.Intent;

import com.bobo.gmargiani.bobo.R;

public class FavoritesActivity extends AuthorizedActivity {

    public static void start(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, FavoritesActivity.class));
        }
    }


    @Override
    public void userIsLoggedIn() {
        
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
