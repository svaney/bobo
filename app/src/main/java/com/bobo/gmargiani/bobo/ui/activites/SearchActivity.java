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
import android.widget.ImageView;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.events.LogInEvent;
import com.bobo.gmargiani.bobo.ui.fragments.SearchListFragment;
import com.bobo.gmargiani.bobo.ui.views.ToolbarSearchWidget;
import com.bobo.gmargiani.bobo.utils.AppConsts;
import com.bobo.gmargiani.bobo.utils.ImageLoader;
import com.bobo.gmargiani.bobo.utils.ViewUtils;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends RootDetailedActivity implements ToolbarSearchWidget.ToolbarWidgetListener, TabLayout.OnTabSelectedListener {
    @BindView(R.id.toolbar_widget)
    ToolbarSearchWidget searchWidget;

    @BindView(R.id.search_helper)
    View searchHelperContainer;

    @BindView(R.id.search_helper_tv)
    TextView searchHelper;

    @BindView(R.id.search_helper_icon)
    ImageView searchHelperIcon;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.pager)
    ViewPager viewPager;


    private String searchQuery;
    private boolean activityStarted;

    public static void start(Context context, String query) {
        if (context != null) {
            Intent intent = new Intent(context, SearchActivity.class);
            intent.putExtra(AppConsts.PARAM_SEARCH_QUERY, query);
            context.startActivity(intent);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchQuery = getIntent().getStringExtra(AppConsts.PARAM_SEARCH_QUERY);

        searchWidget.setMode(ToolbarSearchWidget.MODE_STRICTLY_EXPANDED);
        searchWidget.setOnViewClickListener(this);
        searchWidget.setSearchText(searchQuery);

        ImageLoader.load(searchHelperIcon)
                .setRes(R.drawable.ic_search)
                .applyTint(true)
                .build();

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.search_tab_name_statements)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.search_tab_name_owners)));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addOnTabSelectedListener(this);

        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        ViewUtils.collapse(searchHelperContainer);
    }


    @Override
    public void onSearchString(String query) {
        if (!TextUtils.isEmpty(query)) {
            try {
                ViewUtils.collapse(searchHelperContainer);
                searchQuery = query;
                ((PagerAdapter) viewPager.getAdapter()).refreshSearchQuery();
            } catch (Exception ignored) {
            }
        }
    }

    @OnClick({R.id.search_helper_icon, R.id.search_helper})
    public void onSearchHelperClick() {
        searchWidget.setSearchText(searchHelper.getText().toString());
        searchWidget.searchString();
    }

    @Override
    public void onMenuIconClick(int iconResId) {
        switch (iconResId) {
            case R.id.toolbar_action_icon:
                searchWidget.setSearchText("");
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        activityStarted = true;

        userInfo.requestLogInEvent();
    }

    private LogInEvent logInEvent;
    @Subscribe
    public void onLoginEvent(LogInEvent event) {
        if (logInEvent != event) {
            logInEvent = event;
            if (userInfo.isAuthorized()) {
                closeAuthorizeDialog();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        activityStarted = false;
    }

    @Override
    public void onSearchStringChange(String query) {
        if (activityStarted) {
            if (!TextUtils.isEmpty(query)) {
                if (searchHelperContainer.getVisibility() != View.VISIBLE) {
                    ViewUtils.expand(searchHelperContainer);
                }
                searchHelper.setText(query);
            } else {
                ViewUtils.collapse(searchHelperContainer);
                searchHelper.setText("");
            }
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onSearchViewExpand() {

    }

    @Override
    public void onSearchViewCollapse() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public boolean needEventBus() {
        return false;
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;
        SearchListFragment ownerFragment;
        SearchListFragment statementFragment;


        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
            ownerFragment = new SearchListFragment();
            statementFragment = new SearchListFragment();

            Bundle b = new Bundle();
            b.putString(AppConsts.PARAM_SEARCH_QUERY, searchQuery);
            b.putInt(AppConsts.PARAM_LIST_TYPE, SearchListFragment.LIST_TYPE_STATEMENT);

            statementFragment.setArguments(b);

            Bundle b2 = new Bundle();
            b2.putString(AppConsts.PARAM_SEARCH_QUERY, searchQuery);
            b2.putInt(AppConsts.PARAM_LIST_TYPE, SearchListFragment.LIST_TYPE_OWNER);
            ownerFragment.setArguments(b2);

        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return statementFragment;
                case 1:
                    return ownerFragment;
                default:
                    return null;
            }
        }

        public void refreshSearchQuery() {
            if (statementFragment != null) {
                statementFragment.setSearchQuery(searchQuery);
            }

            if (ownerFragment != null) {
                ownerFragment.setSearchQuery(searchQuery);
            }

        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
