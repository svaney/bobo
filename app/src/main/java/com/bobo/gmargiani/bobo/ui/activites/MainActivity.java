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
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.ActivityResultEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.StatementsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.TokenAuthorizationEvent;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.ui.adapters.StatementRecyclerAdapter;
import com.bobo.gmargiani.bobo.ui.views.ToolbarWidget;
import com.bobo.gmargiani.bobo.utils.AppConsts;
import com.bobo.gmargiani.bobo.utils.AppUtils;
import com.bobo.gmargiani.bobo.utils.ImageLoader;
import com.bobo.gmargiani.bobo.utils.LocaleHelper;
import com.bobo.gmargiani.bobo.utils.PreferencesApiManager;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends RootActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        ToolbarWidget.ToolbarWidgetListener, StatementRecyclerAdapter.LazyLoaderListener {

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

    @BindView(R.id.floating_component)
    protected ViewGroup floatingButton;

    protected ImageView userAvatar;
    protected ImageView languageImage;
    protected View userAvatarWrapper;
    protected View languageChangebtn;
    protected View authorizeText;

    private ActionBarDrawerToggle drawerToggle;

    private boolean drawerListenerAdded;

    private TokenAuthorizationEvent tokenAuthorizationEvent;
    private StatementsEvent statementsEvent;

    private StatementRecyclerAdapter adapter;

    private int floatingButtonLocation;
    private boolean floatingButtonIsMoving;

    private RecyclerView.OnScrollListener floatingButtonListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0) {
                if (!floatingButtonIsMoving) {
                    floatingButtonIsMoving = true;
                    floatingButton.animate().y(AppUtils.getDisplayHeight(MainActivity.this))
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    floatingButtonIsMoving = false;
                                }
                            });
                }
            } else if (dy < 0) {
                if (!floatingButtonIsMoving) {
                    floatingButtonIsMoving = true;
                    floatingButton.animate().y(floatingButtonLocation)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    floatingButtonIsMoving = false;
                                }
                            });
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpDrawer();
        setUpViews();

        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        try {
                            if (!isOpen) {
                                toolbarWidget.collapseSearch();
                            }
                        } catch (Exception e) {
                        }
                    }
                });

    }

    private void setUpViews() {
        toolbarWidget.setOnViewClickListener(this);
        userAvatarWrapper.setOnClickListener(this);
        languageChangebtn.setOnClickListener(this);
        authorizeText.setOnClickListener(this);

        ImageLoader.load((ImageView) floatingButton.findViewById(R.id.icon))
                .setRes(R.drawable.ic_camera)
                .applyTint(R.color.color_white)
                .build();

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(ContextCompat.getColor(this, R.color.floating_button_background_color));
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadius(AppUtils.getDimen(R.dimen.floating_button_height) / 2);

        floatingButton.setBackground(bg);

        floatingButton.post(new Runnable() {
            @Override
            public void run() {
                floatingButtonLocation = (int) floatingButton.getY();
                recyclerView.addOnScrollListener(floatingButtonListener);
            }
        });

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NewStatementActivity.class));
            }
        });
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
        try {
            if (PreferencesApiManager.getInstance().listIsGrid() != adapter.isGrid()){
                setUpRecyclerView();
            }
            adapter.checkLoader(recyclerView);
        } catch (Exception ignored) {
        }
    }

    private void setUpRecyclerView() {
        boolean isGrid = PreferencesApiManager.getInstance().listIsGrid();
        if (isGrid) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (recyclerView.getAdapter().getItemViewType(position) == StatementRecyclerAdapter.HOLDER_TYPE_ITEM) {
                        return 1;
                    }
                    return 2;
                }
            });
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }

        adapter = new StatementRecyclerAdapter(this, isGrid, this);
        adapter.setIsLoading(true);
        statementsEvent = null;
        adapter.setData(new ArrayList<StatementItem>());
        recyclerView.setAdapter(adapter);
    }

    private void showBurgerMenuIcon(boolean show) {
        if (show) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            drawerToggle.setDrawerIndicatorEnabled(true);
            drawerToggle.setToolbarNavigationClickListener(null);
            drawerListenerAdded = false;
        } else {
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
    public void onLastItemIsVisible() {
        userInfo.requestStatements(adapter.getItemCount() - 1, false);
    }

    @Override
    public void onLazyLoaderErrorClick() {
        userInfo.requestStatements(adapter.getItemCount() - 1, false);
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

    @Subscribe
    public void onStatementItems(final StatementsEvent event) {
        if (statementsEvent != event) {
            statementsEvent = event;

            switch (event.getState()) {
                case RootEvent.STATE_LOADING:
                    adapter.setIsLoading(true);
                    break;
                case RootEvent.STATE_ERROR:
                    adapter.setError();
                    break;
                case RootEvent.STATE_SUCCESS:
                    adapter.setData(event.getStatements());
                    adapter.setIsLoading(event.canLoadMore());
                    break;
            }
        }
    }

    @Override
    public void onSearchString(String query) {

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

    @Override
    public void onActivityResultEvent(ActivityResultEvent event) {
        if (event.getResultCode() == RESULT_OK) {
            switch (event.getRequestCode()) {
                case AppConsts.RC_FILTER:

                    break;
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
                startActivityForResult(new Intent(this, FilterActivity.class), AppConsts.RC_FILTER);
                break;
            case R.id.toolbar_inbox:
                startActivity(new Intent(this, InboxActivity.class));
                break;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if (id == R.id.nav_filter) {
            startActivityForResult(new Intent(this, FilterActivity.class), AppConsts.RC_FILTER);
        } else if (id == R.id.nav_favorites) {
            startActivity(new Intent(this, FavoritesActivity.class));
        } else if (id == R.id.nav_subscriptions) {
            startActivity(new Intent(this, ManageSubscriptionsActivity.class));
        } else if (id == R.id.nav_my_statements) {
            startActivity(new Intent(this, MyStatementsActivity.class));
        } else if (id == R.id.nav_inbox) {
            startActivity(new Intent(this, InboxActivity.class));
        } else if (id == R.id.nav_new_statement) {
            startActivity(new Intent(this, NewStatementActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_terms_and_conditions) {
            startActivity(new Intent(this, TermsActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (id == R.id.nav_help) {
            startActivity(new Intent(this, HelpActivity.class));
        }

        return true;
    }

    @Override
    public void onSearchViewExpand() {
        showBurgerMenuIcon(false);
        floatingButton.setVisibility(View.GONE);
        searchBackground.setVisibility(View.VISIBLE);
        try {
            searchBackground.animate().alpha(1);
        } catch (Exception e) {
            searchBackground.setAlpha(1);
        }

    }

    @Override
    public void onSearchViewCollapse() {
        showBurgerMenuIcon(true);
        floatingButton.setVisibility(View.VISIBLE);
        try {
            searchBackground.animate().alpha(0).withEndAction(new Runnable() {
                @Override
                public void run() {
                    searchBackground.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            searchBackground.setAlpha(0);
            searchBackground.setVisibility(View.GONE);
        }
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
