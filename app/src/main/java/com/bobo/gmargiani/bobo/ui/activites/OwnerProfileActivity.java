package com.bobo.gmargiani.bobo.ui.activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.GrantedPermissionsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.CategoriesEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.LocationsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.LogInEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.OwnerDetailsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.OwnerStatementsEvent;
import com.bobo.gmargiani.bobo.model.OwnerDetails;
import com.bobo.gmargiani.bobo.rest.ApiResponse;
import com.bobo.gmargiani.bobo.rest.RestCallback;
import com.bobo.gmargiani.bobo.ui.fragments.StatementListFragment;
import com.bobo.gmargiani.bobo.utils.AppConsts;
import com.bobo.gmargiani.bobo.utils.AppUtils;
import com.bobo.gmargiani.bobo.utils.ImageLoader;
import com.bobo.gmargiani.bobo.utils.Utils;

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

    @BindView(R.id.app_bar)
    View appBar;

    @BindView(R.id.subscribe_button)
    TextView subscribeButton;


    private LocationsEvent locationsEvent;

    private OwnerDetailsEvent ownerDetailsEvent;
    private OwnerStatementsEvent ownerStatementsEvent;
    private String ownerId;

    public static void start(Context context, String ownerId) {
        if (context != null) {
            Intent intent = new Intent(context, OwnerProfileActivity.class);
            intent.putExtra(AppConsts.PARAM_OWNER_ID, ownerId);
            context.startActivity(intent);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationsEvent = userInfo.getLocationsEvent();

        ownerId = getIntent().getStringExtra(AppConsts.PARAM_OWNER_ID);

        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!userInfo.isAuthorized()) {
                    showAuthorizationDialog(null);
                } else if (ownerDetailsEvent != null && ownerDetailsEvent.getState() == RootEvent.STATE_SUCCESS
                        && ownerDetailsEvent.getOwnerDetails() != null) {
                    changeUserSubscribtion(ownerDetailsEvent.getOwnerDetails().getOwnerId(), new RestCallback<ApiResponse<Object>>() {
                        @Override
                        public void onResponse(ApiResponse<Object> response) {
                            super.onResponse(response);
                            refreshInfo();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            super.onFailure(t);
                            refreshInfo();
                        }
                    });
                    refreshInfo();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        userInfo.requestLogInEvent();
        if (!TextUtils.isEmpty(ownerId)) {
            userInfo.requestOwnerDetails(ownerId);
        }
    }

    private LogInEvent logInEvent;

    @Subscribe
    public void onLoginEvent(LogInEvent event) {
        if (logInEvent != event) {
            logInEvent = event;
            if (userInfo.isAuthorized()) {
                closeAuthorizeDialog();
                refreshInfo();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshInfo();
    }

    private void refreshInfo() {
        if (ownerDetailsEvent != null && ownerDetailsEvent.getState() == RootEvent.STATE_SUCCESS
                && ownerDetailsEvent.getOwnerDetails() != null) {
            subscribeButton.setText(userInfo.isUserSubscribed(ownerDetailsEvent.getOwnerDetails().getOwnerId()) ? getString(R.string.common_text_unsubscribe)
                    : getString(R.string.common_text_subscribe));
            if (viewPager != null && viewPager.getAdapter() != null){
                ((PagerAdapter)viewPager.getAdapter()).refresh();
            }
        } else {
            subscribeButton.setText(getString(R.string.common_text_subscribe));
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
        refreshInfo();
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
                    setUpOwnerDetails();
                    showContent();
                    break;
            }
        }
        refreshHeaderText();
    }

    private void setUpOwnerDetails() {
        if (ownerDetailsEvent != null && ownerDetailsEvent.getOwnerDetails() != null) {
            if (!TextUtils.isEmpty(ownerDetailsEvent.getOwnerDetails().getAvatar())) {
                ImageLoader.load(ownerPic)
                        .setUrl(ownerDetailsEvent.getOwnerDetails().getAvatar())
                        .setOval(true)
                        .build();
            } else {
                ImageLoader.load(ownerPic)
                        .setRes(R.drawable.ic_avatar_default)
                        .setOval(true)
                        .build();
            }

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

            icOwnerLocation.setVisibility(TextUtils.isEmpty(ownerDetailsEvent.getOwnerDetails().getLocation()) ? View.GONE : View.VISIBLE);

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
        if (!TextUtils.isEmpty(ownerId)) {
            userInfo.requestOwnerDetails(ownerId);
        }
    }

    @OnClick(R.id.owner_tel)
    public void onCallOwnerClick() {
        if (ownerDetailsEvent != null && ownerDetailsEvent.getOwnerDetails() != null)
            AppUtils.callNumber(this, ownerDetailsEvent.getOwnerDetails().getPhone());
    }

    @Subscribe
    public void onPermissionGranted(GrantedPermissionsEvent event) {
        if (event.getRequestCode() == AppConsts.PERMISSION_PHONE) {
            onCallOwnerClick();
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
    protected void showFullLoading() {
        super.showFullLoading();
        appBar.setVisibility(View.GONE);
    }

    @Override
    protected void showFullError() {
        super.showFullError();
        appBar.setVisibility(View.GONE);
    }

    @Override
    protected void showContent() {
        super.showContent();
        appBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String getHeaderText() {
        return "";
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
            b.putBoolean(AppConsts.PARAM_ARCHIVED, false);

            activeStatements.setArguments(b);

            Bundle b2 = new Bundle();
            b2.putString(AppConsts.PARAM_OWNER_ID, ownerId);
            b2.putBoolean(AppConsts.PARAM_ARCHIVED, true);

            archivedStatements.setArguments(b2);
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

        public void refresh(){
            if (activeStatements != null){
                activeStatements.refreshInfo();
            }

            if (archivedStatements != null){
                archivedStatements.refreshInfo();
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
