package com.bobo.gmargiani.bobo.ui.activites;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.bobo.gmargiani.bobo.R;

/**
 * Created by gmargiani on 2/1/2018.
 */

public abstract class RootDetailedActivity extends RootActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackButton();
    }

    protected void initBackButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(getHeaderText());
        }
    }

    protected void refreshHeaderText() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getHeaderText());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
