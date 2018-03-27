package com.bobo.gmargiani.bobo.ui.activites;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.TokenAuthorizationEvent;
import com.bobo.gmargiani.bobo.ui.views.ToolbarWidget;
import com.bobo.gmargiani.bobo.utils.AlertManager;
import com.bobo.gmargiani.bobo.utils.ImageLoader;
import com.bobo.gmargiani.bobo.utils.LocaleHelper;
import com.bobo.gmargiani.bobo.utils.PreferencesApiManager;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

public class MainActivity extends RootActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawer;

    @BindView(R.id.toolbar_widget)
    protected ToolbarWidget toolbarWidget;

    @BindView(R.id.content_wrapper)
    protected View contentWrapper;

    @BindView(R.id.nav_view)
    protected NavigationView navView;

    protected ImageView userAvatar;
    protected ImageView languageImage;
    protected View userAvatarWrapper;
    protected View languageChangebtn;

    private ActionBarDrawerToggle drawerToggle;

    private boolean searchExpanded;


    private TokenAuthorizationEvent tokenAuthEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpDrawer();
        setUpClickListeners();
    }

    private void setUpDrawer() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navView.setNavigationItemSelectedListener(this);

        userAvatar = navView.getHeaderView(0).findViewById(R.id.user_profile_picture);
        userAvatarWrapper = navView.getHeaderView(0).findViewById(R.id.use_profile_picture_wrapper);
        languageChangebtn = navView.getHeaderView(0).findViewById(R.id.language_change);
        languageImage = navView.getHeaderView(0).findViewById(R.id.language_image);

        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.OVAL);
        bg.setColor(ContextCompat.getColor(this, R.color.color_accent_light));

        userAvatarWrapper.setBackground(bg);
    }

    private void setUpClickListeners() {

        toolbarWidget.setOnViewClickListener(this);
        userAvatarWrapper.setOnClickListener(this);
        languageChangebtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == null) {
            showBurgerMenuIcon(false);
        } else {

            switch (v.getId()) {

                case R.id.use_profile_picture_wrapper:
                    if (isAuthorizationLoaded()) {
                        if (userInfo.isAuthorized()) {

                        } else {
                            drawer.closeDrawer(GravityCompat.START);
                            showAuthorizationDialog();
                        }
                    } else {
                        AlertManager.showInfo(this, getString(R.string.alert_info_loading_data));
                    }
                    break;


                case R.id.language_change:
                    LocaleHelper.changeLanguage(MainActivity.this);
                    restartApp();
                    break;

                case R.id.toolbar_inbox:
                    if (isAuthorizationLoaded()) {
                        if (userInfo.isAuthorized()) {

                        } else {
                            showAuthorizationDialog();
                        }
                    } else {
                        AlertManager.showInfo(this, getString(R.string.alert_info_loading_data));
                    }
                    break;

                case R.id.toolbar_favorite:
                    if (isAuthorizationLoaded()) {
                        if (userInfo.isAuthorized()) {

                        } else {
                            showAuthorizationDialog();
                        }
                    } else {
                        AlertManager.showInfo(this, getString(R.string.alert_info_loading_data));
                    }
                    break;

                case R.id.toolbar_filter:
                    break;
            }
        }
    }

    private void showBurgerMenuIcon(boolean show) {
        if (show) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            drawerToggle.setDrawerIndicatorEnabled(true);
            drawerToggle.setToolbarNavigationClickListener(null);
            searchExpanded = false;
            contentWrapper.setBackgroundColor(ContextCompat.getColor(this, R.color.color_transparent));
        } else {
            contentWrapper.setBackgroundColor(ContextCompat.getColor(this, R.color.content_search_background));
            drawerToggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (!searchExpanded) {
                drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toolbarWidget.closeSearch();
                        showBurgerMenuIcon(true);
                    }
                });
                searchExpanded = true;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        userInfo.requestTokenAuthorizationEvent(PreferencesApiManager.getInstance().getToken());
    }

    @Subscribe
    public void onTokenAuthorizationEvent(TokenAuthorizationEvent event) {
        if (tokenAuthEvent != event) {
            this.tokenAuthEvent = event;
            switch (event.getState()) {
                case RootEvent.STATE_LOADING:
                    showFullLoading();
                    break;
                case RootEvent.STATE_ERROR:
                case RootEvent.STATE_SUCCESS:
                    showContent();
                    setUpScreenData();
                    break;
            }
        }
    }

    private void setUpScreenData() {
        if (userInfo.isAuthorized()) {

        } else {
            ImageLoader.loadImage(userAvatar, R.drawable.ic_avatar_default, true, false);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_filter) {
            // Handle the camera action
        } else if (id == R.id.nav_favorites) {

        } else if (id == R.id.nav_subscriptions) {

        } else if (id == R.id.nav_my_statements) {

        } else if (id == R.id.nav_inbox) {

        } else if (id == R.id.nav_new_statement) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_terms_and_conditions) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_help) {

        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean needEventBus() {
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (searchExpanded) {
            toolbarWidget.closeSearch();
            showBurgerMenuIcon(true);
        } else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private boolean isAuthorizationLoaded() {
        return !(tokenAuthEvent == null || tokenAuthEvent.getState() == RootEvent.STATE_LOADING);
    }
}
