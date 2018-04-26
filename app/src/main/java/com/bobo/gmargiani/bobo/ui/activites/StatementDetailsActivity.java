package com.bobo.gmargiani.bobo.ui.activites;

import android.os.Bundle;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.ui.views.FullScreenGalleryView;
import com.bobo.gmargiani.bobo.utils.ModelGenerator;

import butterknife.BindView;

public class StatementDetailsActivity extends RootDetailedActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_statement_details;
    }

    @Override
    public boolean needEventBus() {
        return false;
    }

    @Override
    protected String getHeaderText() {
        return getString(R.string.activity_name_statement_details);
    }
}
