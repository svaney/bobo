package com.bobo.gmargiani.bobo.ui.activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.OwnerStatementsEvent;
import com.bobo.gmargiani.bobo.ui.fragments.StatementListFragment;
import com.bobo.gmargiani.bobo.utils.AppConsts;
import com.bobo.gmargiani.bobo.utils.Utils;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class MyStatementsActivity extends AuthorizedActivity implements TabLayout.OnTabSelectedListener {
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.pager)
    ViewPager viewPager;

    @BindView(R.id.app_bar)
    View appBar;

    private String ownerId;

    private OwnerStatementsEvent ownerStatementsEvent;

    public static void start(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, MyStatementsActivity.class));
        }
    }

    @Override
    public void userIsLoggedIn() {
        ownerId = logInEvent.getLogInData().getUserDetails().getOwnerId();
        userInfo.requestOwnerStatements(ownerId);
    }

    @Subscribe
    public void onOwnerStatementsEvent(OwnerStatementsEvent event) {
        if (ownerStatementsEvent != event && Utils.equals(event.getOwnerId(), ownerId)) {
            ownerStatementsEvent = event;
            switch (event.getState()) {
                case RootEvent.STATE_LOADING:
                    showFullLoading();
                    break;
                case RootEvent.STATE_ERROR:
                    showFullError();
                    break;
                case RootEvent.STATE_SUCCESS:
                    setUpOwnerTabs();
                    showContent();
                    break;
            }
        }
        refreshHeaderText();
    }

    private void setUpOwnerTabs() {
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.common_text_active)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.common_text_archive)));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(this);

    }

    @OnClick(R.id.full_retry_button)
    public void onOwnerErrorClick() {
        if (!TextUtils.isEmpty(ownerId)) {
            userInfo.requestOwnerDetails(ownerId);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;
        StatementListFragment activeStatements;
        StatementListFragment archivedStatements;


        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
            activeStatements = new StatementListFragment();
            archivedStatements = new StatementListFragment();

            Bundle b = new Bundle();
            b.putString(AppConsts.PARAM_OWNER_ID, ownerId);

            activeStatements.setArguments(b);
            archivedStatements.setArguments(b);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return activeStatements;
                case 1:
                    return archivedStatements;
                default:
                    return null;
            }
        }

        public void refresh() {
            if (activeStatements != null) {
                activeStatements.refreshInfo();
            }

            if (archivedStatements != null) {
                archivedStatements.refreshInfo();
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_statements;
    }

    @Override
    protected String getHeaderText() {
        return getString(R.string.activity_name_my_statements);
    }
}
