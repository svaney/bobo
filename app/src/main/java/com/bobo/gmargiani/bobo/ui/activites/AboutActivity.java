package com.bobo.gmargiani.bobo.ui.activites;

import com.bobo.gmargiani.bobo.R;

public class AboutActivity extends RootDetailedActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public boolean needEventBus() {
        return false;
    }

    @Override
    protected int getHeaderText() {
        return R.string.activity_name_about;
    }
}
