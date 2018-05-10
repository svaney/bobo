package com.bobo.gmargiani.bobo.ui.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.CategoriesEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.LocationsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.OwnerDetailsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.OwnerStatementsEvent;
import com.bobo.gmargiani.bobo.model.OwnerDetails;
import com.bobo.gmargiani.bobo.ui.fragments.StatementListFragment;
import com.bobo.gmargiani.bobo.utils.AppConsts;
import com.bobo.gmargiani.bobo.utils.ImageLoader;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class OwnerProfileActivity extends RootDetailedActivity implements TabLayout.OnTabSelectedListener {

    @BindView(R.id.owner_pic)
    ImageView ownerPic;

    @BindView(R.id.owner_name)
    TextView ownerName;

    @BindView(R.id.owner_location)
    TextView ownerLocation;

    @BindView(R.id.ic_owner_location)
    ImageView icOwnerLocation;

    @BindView(R.id.ic_owner_tel)
    ImageView icOwnerTel;

    @BindView(R.id.owner_tel)
    TextView ownerTel;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.pager)
    ViewPager viewPager;


    private LocationsEvent locationsEvent;

    private OwnerDetailsEvent ownerDetailsEvent;
    private OwnerStatementsEvent ownerStatementsEvent;
    private long ownerId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationsEvent = userInfo.getLocationsEvent();

        ownerId = getIntent().getLongExtra(AppConsts.PARAM_OWNER_ID, -1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ownerId != -1) {
            userInfo.requestOwnerDetails(ownerId);
        }
    }

    @Subscribe
    public void onOwnerDetails(OwnerDetailsEvent event) {
        if (ownerDetailsEvent != event) {
            ownerDetailsEvent = event;
            switch (event.getState()) {
                case RootEvent.STATE_LOADING:
                    showFullLoading();
                    break;
                case RootEvent.STATE_ERROR:
                    showFullError();
                    break;
                case RootEvent.STATE_SUCCESS:
                    userInfo.requestOwnerStatements(ownerId);
                    break;
            }
        } else {
            userInfo.requestOwnerStatements(ownerId);
        }
    }

    @Subscribe
    public void onOwnerStatementsEvent(OwnerStatementsEvent event) {
        if (ownerStatementsEvent != event && event.getOwnerId() == ownerId) {
            ownerStatementsEvent = event;
            switch (event.getState()) {
                case RootEvent.STATE_LOADING:
                    showFullLoading();
                    break;
                case RootEvent.STATE_ERROR:
                    showFullError();
                    break;
                case RootEvent.STATE_SUCCESS:
                    setUpOwnerDetails();
                    showContent();
                    break;
            }
        }

        refreshHeaderText();
    }

    private void setUpOwnerDetails() {
        if (ownerDetailsEvent != null && ownerDetailsEvent.getOwnerDetails() != null) {
            ImageLoader.load(ownerPic)
                    .setUrl(ownerDetailsEvent.getOwnerDetails().getAvatar())
                    .setOval(true)
                    .build();

            ImageLoader.load(icOwnerLocation)
                    .setRes(R.drawable.ic_blue_location)
                    .applyTint(R.color.colorAccent)
                    .build();

            ImageLoader.load(icOwnerTel)
                    .setRes(R.drawable.ic_phone)
                    .applyTint(R.color.colorAccent)
                    .build();

            ownerName.setText(ownerDetailsEvent.getOwnerDetails().getDisplayName());

            ownerTel.setText(ownerDetailsEvent.getOwnerDetails().getPhone());

            ownerLocation.setText(locationsEvent.getValueByKey(ownerDetailsEvent.getOwnerDetails().getLocation()));

            setUpOwnerTabs();
        }
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
        if (ownerId != -1) {
            userInfo.requestOwnerDetails(ownerId);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_owner_profile;
    }

    @Override
    public boolean needEventBus() {
        return true;
    }

    @Override
    protected String getHeaderText() {
        if (ownerDetailsEvent != null && ownerDetailsEvent.getOwnerDetails() != null) {
            return ownerDetailsEvent.getOwnerDetails().getDisplayName();
        }
        return super.getHeaderText();
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
            b.putLong(AppConsts.PARAM_OWNER_ID, ownerId);

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

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
