package com.bobo.gmargiani.bobo.ui.activites;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.TokenAuthorizationEvent;
import com.bobo.gmargiani.bobo.ui.adapters.StatementRecyclerAdapter;
import com.bobo.gmargiani.bobo.ui.views.ToolbarWidget;
import com.bobo.gmargiani.bobo.utils.AppUtils;
import com.bobo.gmargiani.bobo.utils.ImageLoader;
import com.bobo.gmargiani.bobo.utils.LocaleHelper;
import com.bobo.gmargiani.bobo.utils.PreferencesApiManager;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends RootActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ToolbarWidget.ToolbarWidgetListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawer;

    @BindView(R.id.toolbar_widget)
    protected ToolbarWidget toolbarWidget;

    @BindView(R.id.search_background)
    protected View searchBackground;

    @BindView(R.id.nav_view)
    protected NavigationView navView;

    @BindView(R.id.recycler_view)
    protected RecyclerView recyclerView;

    protected ImageView userAvatar;
    protected ImageView languageImage;
    protected View userAvatarWrapper;
    protected View languageChangebtn;
    protected View authorizeText;

    private ActionBarDrawerToggle drawerToggle;

    private boolean drawerListenerAdded;

    private TokenAuthorizationEvent tokenAuthorizationEvent;

    private StatementRecyclerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpDrawer();

        toolbarWidget.setOnViewClickListener(this);
        userAvatarWrapper.setOnClickListener(this);
        languageChangebtn.setOnClickListener(this);
        authorizeText.setOnClickListener(this);
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
        authorizeText = navView.getHeaderView(0).findViewById(R.id.nav_login_register);

        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.OVAL);
        bg.setColor(ContextCompat.getColor(this, R.color.color_accent_light));

        userAvatarWrapper.setBackground(bg);

        ImageLoader.load(userAvatar)
                .setRes(R.drawable.ic_avatar_default)
                .setOval(true)
                .build();

    }

    @Override
    protected void onStart() {
        super.onStart();
        userInfo.requestTokenAuthorizationEvent();
    }

    @Subscribe
    public void onTokenAuthorizationEvent(TokenAuthorizationEvent event) {
        if (tokenAuthorizationEvent != event) {
            tokenAuthorizationEvent = event;

            switch (event.getState()) {
                case RootEvent.STATE_LOADING:
                    showFullLoading();
                    break;
                case RootEvent.STATE_SUCCESS:
                    showContent();
                    setUpRecyclerView();
                    break;
                case RootEvent.STATE_ERROR:
                    showFullError();
                    break;
            }
        }
    }

    private void setUpRecyclerView() {
        boolean isGrid = PreferencesApiManager.getInstance().listIsGrid();
        if (isGrid) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, AppUtils.calculateNoOfColumns(this, R.dimen.statement_item_grid_width)));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }

        adapter = new StatementRecyclerAdapter(this, isGrid);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            if (v == userAvatarWrapper || v == authorizeText) {
                if (userInfo.isAuthorized()) {

                } else {
                    showAuthorizationDialog();
                }
                drawer.closeDrawer(GravityCompat.START);
            } else if (v == languageChangebtn) {
                LocaleHelper.changeLanguage(MainActivity.this);
                restartApp();
            }
        }
    }

    private void showBurgerMenuIcon(boolean show) {
        if (show) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            drawerToggle.setDrawerIndicatorEnabled(true);
            drawerToggle.setToolbarNavigationClickListener(null);
            drawerListenerAdded = false;
            searchBackground.setVisibility(View.GONE);
        } else {
            searchBackground.setVisibility(View.VISIBLE);
            drawerToggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (!drawerListenerAdded) {
                drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toolbarWidget.collapseSearch();
                    }
                });
                drawerListenerAdded = true;
            }
        }
    }

    @Override
    public void onMenuIconClick(int iconResId) {
        switch (iconResId) {
            case R.id.toolbar_favorite:
                startActivity(new Intent(this, FavoritesActivity.class));
                break;
            case R.id.toolbar_filter:
                PreferencesApiManager.getInstance().setListGrid(!PreferencesApiManager.getInstance().listIsGrid());
                setUpRecyclerView();
                break;
            case R.id.toolbar_inbox:
                break;
        }
    }

    @Override
    public void onSearchString(String query) {

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
    public void onSearchViewExpand() {
        showBurgerMenuIcon(false);
    }

    @Override
    public void onSearchViewCollapse() {
        showBurgerMenuIcon(true);
    }

    @OnClick(R.id.full_retry_button)
    public void onFullRetryClick() {
        userInfo.requestTokenAuthorizationEvent();
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
        if (toolbarWidget != null && toolbarWidget.isSearchViewExpanded()) {
            toolbarWidget.collapseSearch();
        } else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
