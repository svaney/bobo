package com.bobo.gmargiani.bobo.ui.activites;

import com.bobo.gmargiani.bobo.R;

public class HelpActivity extends RootDetailedActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    public boolean needEventBus() {
        return false;
    }

    @Override
    protected String getHeaderText() {
        return getString(R.string.activity_name_help);
    }
}
