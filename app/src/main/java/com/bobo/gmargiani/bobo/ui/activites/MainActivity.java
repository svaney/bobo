package com.bobo.gmargiani.bobo.ui.activites;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.opengl.Visibility;
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
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.ActivityResultEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.LogInEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.StatementsEvent;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.model.UserInfo;
import com.bobo.gmargiani.bobo.rest.ApiResponse;
import com.bobo.gmargiani.bobo.rest.RestCallback;
import com.bobo.gmargiani.bobo.ui.adapters.LazyLoaderListener;
import com.bobo.gmargiani.bobo.ui.adapters.RecyclerItemClickListener;
import com.bobo.gmargiani.bobo.ui.adapters.StatementRecyclerAdapter;
import com.bobo.gmargiani.bobo.ui.views.FilterTextView;
import com.bobo.gmargiani.bobo.ui.views.ToolbarSearchWidget;
import com.bobo.gmargiani.bobo.utils.AlertManager;
import com.bobo.gmargiani.bobo.utils.AppConsts;
import com.bobo.gmargiani.bobo.utils.AppUtils;
import com.bobo.gmargiani.bobo.utils.ImageLoader;
import com.bobo.gmargiani.bobo.utils.LocaleHelper;
import com.bobo.gmargiani.bobo.utils.PreferencesApiManager;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends RootActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        ToolbarSearchWidget.ToolbarWidgetListener, LazyLoaderListener, StatementRecyclerAdapter.StatementItemClickListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawer;

    @BindView(R.id.toolbar_widget)
    protected ToolbarSearchWidget toolbarWidget;

    @BindView(R.id.search_background)
    protected View searchBackground;

    @BindView(R.id.nav_view)
    protected NavigationView navView;

    @BindView(R.id.recycler_view)
    protected RecyclerView recyclerView;

    @BindView(R.id.filter_value_wrapper)
    protected FrameLayout filterTextViewWrapper;

    @BindView(R.id.floating_component)
    protected ViewGroup floatingButton;


    protected ImageView userAvatar;
    protected ImageView languageImage;
    protected View userAvatarWrapper;
    protected View languageChangebtn;
    protected TextView authorizeText;
    protected View logout;

    private ActionBarDrawerToggle drawerToggle;

    private boolean drawerListenerAdded;

    private StatementsEvent statementsEvent;

    private StatementRecyclerAdapter adapter;

    private int floatingButtonLocation;
    private boolean floatingButtonIsMoving;

    private ArrayList<String> filterValues;

    private LogInEvent logInEvent;

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

        filterValues = new ArrayList<>();

        for (int i = 0; i < FilterActivity.FILTER_PARAMS_SIZE; i++) {
            filterValues.add("");
        }

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
        logout.setOnClickListener(this);

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
                if (!userInfo.isAuthorized()) {
                    showAuthorizationDialog(null);
                } else {
                    NewStatementActivity.start(MainActivity.this);
                }
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
        logout = navView.getHeaderView(0).findViewById(R.id.log_out);
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
        userInfo.requestLogInEvent();
        if (adapter == null || PreferencesApiManager.getInstance().listIsGrid() != adapter.isGrid()) {
            setUpRecyclerView();
        }
        adapter.checkLoader(recyclerView);

        toolbarWidget.collapseSearch();
    }

    @Subscribe
    public void onLoginEvent(LogInEvent event) {
        if (logInEvent != event) {
            logInEvent = event;
            if (userInfo.isAuthorized()) {
                closeAuthorizeDialog();

                if (!TextUtils.isEmpty(event.getLogInData().getUserDetails().getAvatar()))
                    ImageLoader.load(userAvatar)
                            .setUrl(event.getLogInData().getUserDetails().getAvatar())
                            .setOval(true)
                            .build();

                authorizeText.setText(event.getLogInData().getUserDetails().getDisplayName());
                refreshInfo();
            }

            logout.setVisibility(userInfo.isAuthorized() ? View.VISIBLE : View.GONE);
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

        adapter = new StatementRecyclerAdapter(this, isGrid ? StatementRecyclerAdapter.ADAPTER_TYPE_GRID : StatementRecyclerAdapter.ADAPTER_TYPE_LIST,
                this, this, userInfo);
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

        boolean sell = !"N".equals(filterValues.get(FilterActivity.FILTER_PARAM_POS_SELL));
        boolean rent = !"N".equals(filterValues.get(FilterActivity.FILTER_PARAM_POS_RENT));

        BigDecimal priceFrom = TextUtils.isEmpty(filterValues.get(FilterActivity.FILTER_PARAM_POS_PRICE_FROM)) ?
                null : new BigDecimal(filterValues.get(FilterActivity.FILTER_PARAM_POS_PRICE_FROM));

        BigDecimal priceTo = TextUtils.isEmpty(filterValues.get(FilterActivity.FILTER_PARAM_POS_PRICE_TO)) ?
                null : new BigDecimal(filterValues.get(FilterActivity.FILTER_PARAM_POS_PRICE_TO));

        String category = filterValues.get(FilterActivity.FILTER_PARAM_POS_CATEGORY);
        String location = filterValues.get(FilterActivity.FILTER_PARAM_POS_LOCATION);
        String orderBy = filterValues.get(FilterActivity.FILTER_PARAM_POS_ORDER_BY);

        ArrayList locations = new ArrayList();
        locations.addAll(Arrays.asList(location.split(";")));

        ArrayList categories = new ArrayList();
        categories.addAll(Arrays.asList(category.split(";")));


        userInfo.requestStatements(adapter.getItemCount() - 1, false,
                sell, rent, categories, locations, priceFrom, priceTo, orderBy);
    }

    @Override
    public void onLazyLoaderErrorClick() {
        onLastItemIsVisible();
    }

    @Subscribe
    public void onStatementItems(final StatementsEvent event) {
        if (statementsEvent != event) {
            statementsEvent = event;

            filterTextViewWrapper.setVisibility(View.GONE);
            switch (event.getState()) {
                case RootEvent.STATE_LOADING:
                    adapter.setIsLoading(true);
                    break;
                case RootEvent.STATE_ERROR:
                    adapter.setError();
                    break;
                case RootEvent.STATE_SUCCESS:
                    setUpFilterTypeView();
                    adapter.setData(event.getStatements());
                    adapter.setIsLoading(event.canLoadMore());
                    break;
            }
        }
    }


    private void setUpFilterTypeView() {
        BigDecimal priceFrom = TextUtils.isEmpty(filterValues.get(FilterActivity.FILTER_PARAM_POS_PRICE_FROM)) ?
                null : new BigDecimal(filterValues.get(FilterActivity.FILTER_PARAM_POS_PRICE_FROM));

        BigDecimal priceTo = TextUtils.isEmpty(filterValues.get(FilterActivity.FILTER_PARAM_POS_PRICE_TO)) ?
                null : new BigDecimal(filterValues.get(FilterActivity.FILTER_PARAM_POS_PRICE_TO));

        String category = filterValues.get(FilterActivity.FILTER_PARAM_POS_CATEGORY);
        String location = filterValues.get(FilterActivity.FILTER_PARAM_POS_LOCATION);
        String orderBy = filterValues.get(FilterActivity.FILTER_PARAM_POS_ORDER_BY);

        ArrayList<String> locations = new ArrayList();
        locations.addAll(Arrays.asList(location.split(";")));

        ArrayList<String> categories = new ArrayList();
        categories.addAll(Arrays.asList(category.split(";")));

        String filterText = "";

        if (categories != null) {
            if (categories.size() > 0 && !TextUtils.isEmpty(categories.get(0))) {
                filterText += getString(R.string.filter_title_category) + ": ";
                for (String cat : categories) {
                    filterText += userInfo.getCategoriesEvent().getValueByKey(cat) + "; ";
                }
            }
        }

        if (locations != null) {
            if (locations.size() > 0 && !TextUtils.isEmpty(locations.get(0))) {
                filterText += getString(R.string.filter_title_location) + ": ";
                for (String cat : locations) {
                    filterText += userInfo.getLocationsEvent().getValueByKey(cat) + "; ";
                }
            }
        }

        if (priceFrom != null) {
            filterText += getString(R.string.filter_title_price_from) + ": " + String.valueOf(priceFrom) + "; ";
        }

        if (priceTo != null) {
            filterText += getString(R.string.filter_title_price_to) + ": " + String.valueOf(priceTo) + "; ";
        }

        if (!TextUtils.isEmpty(filterText)) {
            filterTextViewWrapper.setVisibility(View.VISIBLE);
            FilterTextView filterTextView = new FilterTextView(this);
            filterTextView.showCloseButton(true);
            filterTextView.setText(filterText);
            filterTextViewWrapper.removeAllViews();
            filterTextViewWrapper.addView(filterTextView);
            filterTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filterValues = new ArrayList<>();

                    for (int i = 0; i < FilterActivity.FILTER_PARAMS_SIZE; i++) {
                        filterValues.add("");
                    }
                    statementsEvent = new StatementsEvent();
                    setUpRecyclerView();
                }
            });
        }
    }

    @Override
    public void onSearchString(String query) {
        if (!TextUtils.isEmpty(query)) {
            SearchActivity.start(this, query);
        }
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            if (v == userAvatarWrapper || v == authorizeText) {
                if (userInfo.isAuthorized()) {
                    RegistrationActivity.start(MainActivity.this, logInEvent.getLogInData().getUserDetails());
                } else {
                    showAuthorizationDialog(null);
                }
                drawer.closeDrawer(GravityCompat.START);
            } else if (v == languageChangebtn) {
                LocaleHelper.changeLanguage(MainActivity.this);
                userInfo.clearData();
                adapter = null;
                restartApp();
            } else if (v == logout) {
                PreferencesApiManager.getInstance().saveToken("");
                userInfo.clearData();
                adapter = null;
                restartApp();
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        if (statementsEvent != null && statementsEvent.getStatements() != null && statementsEvent.getStatements().size() > position) {
            StatementItem item = statementsEvent.getStatements().get(position);
            StatementDetailsActivity.start(this, item, userInfo);
        }
    }

    @Override
    public void onFavoritesClick(int position) {
        if (!userInfo.isAuthorized()) {
            showAuthorizationDialog(null);
        } else if (statementsEvent != null && statementsEvent.getStatements() != null && statementsEvent.getStatements().size() > position) {
            if (userInfo.isUsersItem(statementsEvent.getStatements().get(position).getOwnerId())) {
                NewStatementActivity.start(this, statementsEvent.getStatements().get(position));
            } else {
                changeItemFavorite(statementsEvent.getStatements().get(position).getStatementId(), new RestCallback<ApiResponse<Object>>() {
                    @Override
                    public void onResponse(ApiResponse<Object> response) {
                        super.onResponse(response);
                        if (!response.isSuccess()) {
                            refreshInfo();
                        }
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshInfo();
    }

    private void refreshInfo() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResultEvent(ActivityResultEvent event) {
        if (event.getResultCode() == RESULT_OK) {
            switch (event.getRequestCode()) {
                case AppConsts.RC_FILTER:
                    filterValues = Parcels.unwrap(event.getData().getParcelableExtra(AppConsts.PARAM_FILTER_PARAMS));
                    statementsEvent = new StatementsEvent();
                    setUpRecyclerView();
                    break;
            }
        }

    }

    @Override
    public void onMenuIconClick(int iconResId) {
        switch (iconResId) {
            case R.id.toolbar_favorite:
                FavoritesActivity.start(this);
                break;
            case R.id.toolbar_filter:
                FilterActivity.startWithResult(this, filterValues);
                break;
            case R.id.toolbar_inbox:
                InboxActivity.start(this);
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
            FilterActivity.startWithResult(this, filterValues);
        } else if (id == R.id.nav_favorites) {
            FavoritesActivity.start(this);
        } else if (id == R.id.nav_subscriptions) {
            ManageSubscriptionsActivity.start(this);
        } else if (id == R.id.nav_my_statements) {
            MyStatementsActivity.start(this);
            //   } else if (id == R.id.nav_inbox) {
            //       InboxActivity.start(this);
        } else if (id == R.id.nav_new_statement) {
            if (!userInfo.isAuthorized()) {
                showAuthorizationDialog(null);
            } else {
                NewStatementActivity.start(MainActivity.this);
            }
            //    } else if (id == R.id.nav_settings) {
            //        SettingsActivity.start(this);
        } else if (id == R.id.nav_terms_and_conditions) {
            TermsActivity.start(this);
        } else if (id == R.id.nav_about) {
            AboutActivity.start(this);
        } else if (id == R.id.nav_help) {
            HelpActivity.start(this);
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

    @Override
    public void onSearchStringChange(String query) {

    }

    @OnClick(R.id.full_retry_button)
    public void onFullRetryClick() {

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
