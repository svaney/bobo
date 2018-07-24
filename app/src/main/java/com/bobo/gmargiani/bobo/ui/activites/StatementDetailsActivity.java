package com.bobo.gmargiani.bobo.ui.activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.GrantedPermissionsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.CategoriesEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.LocationsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.LogInEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.OwnerDetailsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.SimilarStatementsEvent;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.model.UserInfo;
import com.bobo.gmargiani.bobo.rest.ApiResponse;
import com.bobo.gmargiani.bobo.rest.RestCallback;
import com.bobo.gmargiani.bobo.ui.adapters.RecyclerItemClickListener;
import com.bobo.gmargiani.bobo.ui.adapters.StatementRecyclerAdapter;
import com.bobo.gmargiani.bobo.ui.views.FullScreenGalleryView;
import com.bobo.gmargiani.bobo.ui.views.GalleryFooter;
import com.bobo.gmargiani.bobo.ui.views.ReadMoreText;
import com.bobo.gmargiani.bobo.utils.AppConsts;
import com.bobo.gmargiani.bobo.utils.AppUtils;
import com.bobo.gmargiani.bobo.utils.ImageLoader;
import com.bobo.gmargiani.bobo.utils.Utils;

import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.OnClick;

public class StatementDetailsActivity extends RootDetailedActivity implements StatementRecyclerAdapter.StatementItemClickListener {
    @BindView(R.id.gallery_footer)
    GalleryFooter galleryFooter;

    @BindView(R.id.gallery)
    FullScreenGalleryView gallery;

    @BindView(R.id.full_gallery_footer)
    GalleryFooter fullGalleryFooter;

    @BindView(R.id.full_gallery)
    FullScreenGalleryView fullGallery;

    @BindView(R.id.full_screen_gallery_wrapper)
    ViewGroup fullGalleryWrapper;

    @BindView(R.id.close_full_gallery)
    View closeFullGalleryButton;

    @BindView(R.id.statement_price)
    TextView statementPrice;

    @BindView(R.id.similar_title)
    View similarsTitle;

    @BindView(R.id.statement_title)
    TextView statementTitle;

    @BindView(R.id.statement_views)
    TextView statementViews;

    @BindView(R.id.statement_total_favorites)
    TextView statementFavorites;

    @BindView(R.id.statement_date)
    TextView statementDate;

    @BindView(R.id.ic_statistics_views)
    ImageView icViews;

    @BindView(R.id.ic_statistics_favorite)
    ImageView icFavorites;

    @BindView(R.id.owner_pic)
    ImageView ownerProfPic;

    @BindView(R.id.owner_title)
    TextView ownerTitle;

    @BindView(R.id.owner_location)
    TextView ownerLocation;

    @BindView(R.id.statement_details)
    ReadMoreText statementDetails;

    @BindView(R.id.ic_owner_tel)
    ImageView icOwnerTel;

    @BindView(R.id.owner_tel)
    TextView ownerTel;

    @BindView(R.id.ic_owner_location)
    ImageView icOwnerLocation;

    @BindView(R.id.statement_type)
    TextView statementType;

    @BindView(R.id.show_on_map)
    View mapButton;

    @BindView(R.id.ic_statement_category)
    ImageView icStatementCategory;

    @BindView(R.id.statement_category)
    TextView statementCategory;

    @BindView(R.id.ic_statement_location)
    ImageView icStatementLocation;

    @BindView(R.id.statement_location)
    TextView statementLocation;

    @BindView(R.id.owner_content)
    View ownerContent;

    @BindView(R.id.owner_loader)
    View ownerLoader;

    @BindView(R.id.owner_retry)
    View ownerRetry;

    @BindView(R.id.similar_loader)
    View similarLoader;

    @BindView(R.id.similar_retry)
    View similarRetry;

    @BindView(R.id.similar_recycler)
    RecyclerView similarRecycler;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    private StatementItem statementItem;
    private OwnerDetailsEvent ownerDetailsEvent;
    private SimilarStatementsEvent similarStatementsEvent;


    private LocationsEvent locationsEvent;
    private CategoriesEvent categoriesEvent;

    public static void start(Context context, StatementItem item, UserInfo userInfo) {
        if (context != null) {
            if (userInfo.isUsersItem(item.getOwnerId())) {
                NewStatementActivity.start(context, item);
            } else {
                Intent intent = new Intent(context, StatementDetailsActivity.class);
                if (item != null) {
                    intent.putExtra(AppConsts.PARAM_STATEMENT, Parcels.wrap(item));
                }
                context.startActivity(intent);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoriesEvent = userInfo.getCategoriesEvent();
        locationsEvent = userInfo.getLocationsEvent();

        statementItem = Parcels.unwrap(getIntent().getParcelableExtra(AppConsts.PARAM_STATEMENT));

        if (statementItem != null) {
            setUpGallery();
            setUpValues();
            refreshHeaderText();
        }
    }

    private void setUpGallery() {
        if (statementItem != null) {
            gallery.setImages(statementItem.getImages());
            gallery.setImageTouchable(false);
            gallery.setOnImageClickListener(new RecyclerItemClickListener() {
                @Override
                public void onRecyclerItemClick(int pos) {
                    showFullScreenGallery(true, pos);
                }
            });

            galleryFooter.showFilledFavorite(userInfo.isStatementFavorite(statementItem.getStatementId()));
            galleryFooter.setOnFavoriteClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onFavoriteClick();
                }
            });
            galleryFooter.setDataSize(statementItem.getImages() == null ? 0 : statementItem.getImages().size());
            galleryFooter.setGallery(gallery);
            gallery.addOnPageChangeListener(galleryFooter);

            showFullScreenGallery(false);

            closeFullGalleryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFullScreenGallery(false);
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshInfo();
    }

    private void refreshInfo() {
        if (galleryFooter != null) {
            galleryFooter.showFilledFavorite(userInfo.isStatementFavorite(statementItem.getStatementId()));
        }
        if (fullGalleryFooter != null) {
            fullGalleryFooter.showFilledFavorite(userInfo.isStatementFavorite(statementItem.getStatementId()));
        }
    }

    private void onFavoriteClick() {
        if (!userInfo.isAuthorized()) {
            showAuthorizationDialog(null);
        } else if (statementItem != null) {
            if (userInfo.isUsersItem(statementItem.getOwnerId())) {
                NewStatementActivity.start(this, statementItem);
            } else {
                changeItemFavorite(statementItem.getStatementId(), new RestCallback<ApiResponse<Object>>() {
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

    private void showFullScreenGallery(boolean show) {
        showFullScreenGallery(show, 0);
    }

    private void showFullScreenGallery(boolean show, int pos) {

        if (show && statementItem != null && statementItem.getImages() != null && statementItem.getImages().size() != 0) {
            fullGalleryWrapper.setVisibility(show ? View.VISIBLE : View.GONE);
            fullGallery.setImages(statementItem.getImages());
            fullGallery.setImageTouchable(true);
            fullGallery.addOnPageChangeListener(fullGalleryFooter);

            fullGalleryFooter.showFilledFavorite(userInfo.isStatementFavorite(statementItem.getStatementId()));
            fullGalleryFooter.setDataSize(statementItem.getImages() == null ? 0 : statementItem.getImages().size());
            fullGalleryFooter.setGallery(fullGallery);
            fullGalleryFooter.setSelectedPos(pos);

            fullGalleryFooter.setOnFavoriteClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onFavoriteClick();
                }
            });
        } else {
            fullGalleryWrapper.setVisibility(show && statementItem.getImages() != null && statementItem.getImages().size() != 0 ? View.VISIBLE : View.GONE);
        }
    }

    private void setUpValues() {
        if (statementItem != null) {
            statementTitle.setText(statementItem.getTitle());
            statementPrice.setText(Utils.getAmountWithGel(statementItem.getPrice()));

            statementViews.setText(String.valueOf(statementItem.getTotalViews()));
            statementFavorites.setText(String.valueOf(statementItem.getTotalFavorites()));

            //   statementDate.setText(Utils.getFullDate(statementItem.getCreateDate(), this));

            statementDate.setText(statementItem.getCreateDate());
            statementDetails.setText(statementItem.getDescription());

            statementLocation.setText(locationsEvent.getValueByKey(statementItem.getLocation()));
            statementCategory.setText(categoriesEvent.getValueByKey(statementItem.getCategory()));
            statementType.setText(statementItem.isSelling() ? getString(R.string.common_text_selling) :
                    (statementItem.isRenting() ? getString(R.string.common_text_renting) : ""));

            if (statementItem.getLat() != null && statementItem.getLon() != null){
                mapButton.setVisibility(View.VISIBLE);
                mapButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MapsActivity.start(StatementDetailsActivity.this, MapsActivity.TYPE_SHOW_LOCATION, statementItem.getLat(), statementItem.getLon());
                    }
                });
            }
        }


        ImageLoader.load(icFavorites)
                .setRes(R.drawable.ic_favorite)
                .applyTint(R.color.ic_grey_color)
                .build();

        ImageLoader.load(icViews)
                .setRes(R.drawable.ic_views)
                .applyTint(R.color.ic_grey_color)
                .build();

        ImageLoader.load(icStatementCategory)
                .setRes(R.drawable.ic_blue_category)
                .applyTint(R.color.colorAccent)
                .build();

        ImageLoader.load(icStatementLocation)
                .setRes(R.drawable.ic_blue_location)
                .applyTint(R.color.colorAccent)
                .build();

        icStatementLocation.setVisibility(TextUtils.isEmpty(statementItem.getLocation()) ? View.GONE : View.VISIBLE);
        statementLocation.setVisibility(TextUtils.isEmpty(statementItem.getLocation()) ? View.GONE : View.VISIBLE);
    }

    private void setUpOwnerDetails() {
        if (ownerDetailsEvent != null && ownerDetailsEvent.getOwnerDetails() != null) {
            ImageLoader.load(ownerProfPic)
                    .setErroPlaceHolder(R.drawable.ic_avatar_default)
                    .setUrl(ownerDetailsEvent.getOwnerDetails().getAvatar())
                    .setOval(true)
                    .build();

            ownerTitle.setText(ownerDetailsEvent.getOwnerDetails().getDisplayName());
            ownerLocation.setText(locationsEvent.getValueByKey(ownerDetailsEvent.getOwnerDetails().getLocation()));
            ownerTel.setText(ownerDetailsEvent.getOwnerDetails().getPhone());

            ImageLoader.load(icOwnerTel)
                    .setRes(R.drawable.ic_phone)
                    .applyTint(R.color.colorAccent)
                    .build();

            ImageLoader.load(icOwnerLocation)
                    .setRes(R.drawable.ic_blue_location)
                    .applyTint(R.color.colorAccent)
                    .build();

            ownerLocation.setVisibility(TextUtils.isEmpty(locationsEvent.getValueByKey(ownerDetailsEvent.getOwnerDetails().getLocation())) ? View.GONE : View.VISIBLE);
            icOwnerLocation.setVisibility(TextUtils.isEmpty(locationsEvent.getValueByKey(ownerDetailsEvent.getOwnerDetails().getLocation())) ? View.GONE : View.VISIBLE);
        }
    }

    private void setUpSimilarStatementContent() {
        if (similarStatementsEvent != null && similarStatementsEvent.getState() == RootEvent.STATE_SUCCESS) {
            if (similarStatementsEvent.getSimilarStatements() != null && similarStatementsEvent.getSimilarStatements().size() > 0) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

                similarRecycler.setLayoutManager(layoutManager);
                StatementRecyclerAdapter adapter = new StatementRecyclerAdapter(this, StatementRecyclerAdapter.ADAPTER_TYPE_SIMILAR,
                        this, null, userInfo);
                adapter.setIsLoading(false);
                adapter.setData(similarStatementsEvent.getSimilarStatements());
                similarRecycler.setAdapter(adapter);
            } else {
                similarRecycler.setVisibility(View.GONE);
                similarsTitle.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        if (similarStatementsEvent != null && similarStatementsEvent.getSimilarStatements() != null && similarStatementsEvent.getSimilarStatements().size() > position) {
            StatementItem item = similarStatementsEvent.getSimilarStatements().get(position);

            StatementDetailsActivity.start(this, item, userInfo);
        }
    }

    @Override
    public void onFavoritesClick(int position) {
        if (!userInfo.isAuthorized()) {
            showAuthorizationDialog(null);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        userInfo.requestLogInEvent();
        requestDetails();
    }

    private LogInEvent logInEvent;

    @Subscribe
    public void onLoginEvent(LogInEvent event) {
        if (logInEvent != event) {
            logInEvent = event;
            if (userInfo.isAuthorized()) {
                closeAuthorizeDialog();
            }
            refreshInfo();
        }
    }

    private void requestDetails() {
        if (statementItem != null) {
            userInfo.requestOwnerDetails(statementItem.getOwnerId());
            userInfo.requestSimilarStatements(statementItem.getCategory());
        }
    }

    @Subscribe
    public void onOwnerDetailsEvent(OwnerDetailsEvent event) {
        if (ownerDetailsEvent != event && statementItem != null && Utils.equals(statementItem.getOwnerId(), event.getOwnerId())) {
            ownerDetailsEvent = event;
            switch (event.getState()) {
                case RootEvent.STATE_LOADING:
                    showOwnerLoader();
                    break;
                case RootEvent.STATE_ERROR:
                    showOwnerError();
                    break;
                case RootEvent.STATE_SUCCESS:
                    showOwnerContent();
                    setUpOwnerDetails();
                    break;
            }
        }
    }

    @Subscribe
    public void onSimilarStatements(SimilarStatementsEvent event) {
        if (similarStatementsEvent != event && statementItem != null && Utils.equals(event.getCategoryId(), statementItem.getCategory())) {
            similarStatementsEvent = event;

            similarRecycler.setVisibility(View.VISIBLE);
            similarsTitle.setVisibility(View.VISIBLE);

            switch (event.getState()) {
                case RootEvent.STATE_LOADING:
                    showSimilarStatementLoader();
                    break;
                case RootEvent.STATE_ERROR:
                    showSimilarStatementError();
                    break;
                case RootEvent.STATE_SUCCESS:
                    showSimilarStatementContent();
                    setUpSimilarStatementContent();
                    break;
            }
        }
    }

    @OnClick(R.id.call_button)
    public void onCallOwnerClick() {
        if (ownerDetailsEvent != null && ownerDetailsEvent.getOwnerDetails() != null)
            AppUtils.callNumber(this, ownerDetailsEvent.getOwnerDetails().getPhone());
    }

    @OnClick({R.id.owner_content, R.id.owner_tel})
    public void onOwnerClick() {
        if (ownerDetailsEvent != null) {
            OwnerProfileActivity.start(this, ownerDetailsEvent.getOwnerId());
        }
    }


    @Subscribe
    public void onPermissionGranted(GrantedPermissionsEvent event) {
        if (event.getRequestCode() == AppConsts.PERMISSION_PHONE) {
            onCallOwnerClick();
        }
    }

    @Override
    public void onBackPressed() {
        if (fullGalleryWrapper.getVisibility() == View.VISIBLE) {
            showFullScreenGallery(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_statement_details;
    }

    @Override
    public boolean needEventBus() {
        return true;
    }

    @Override
    protected String getHeaderText() {
        if (statementItem != null) {
            return statementItem.getTitle();
        }
        return getString(R.string.activity_name_statement_details);
    }

    private void showOwnerContent() {
        ownerContent.setVisibility(View.VISIBLE);
        ownerRetry.setVisibility(View.GONE);
        ownerLoader.setVisibility(View.GONE);
    }

    private void showOwnerLoader() {
        ownerLoader.setVisibility(View.VISIBLE);
        ownerRetry.setVisibility(View.GONE);
        ownerContent.setVisibility(View.GONE);
    }

    private void showOwnerError() {
        ownerRetry.setVisibility(View.VISIBLE);
        ownerContent.setVisibility(View.GONE);
        ownerLoader.setVisibility(View.GONE);

        ownerRetry.findViewById(R.id.full_retry_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDetails();
            }
        });
    }

    private void showSimilarStatementContent() {
        similarRecycler.setVisibility(View.VISIBLE);
        similarLoader.setVisibility(View.GONE);
        similarRetry.setVisibility(View.GONE);
    }

    private void showSimilarStatementLoader() {
        similarLoader.setVisibility(View.VISIBLE);
        similarRecycler.setVisibility(View.GONE);
        similarRetry.setVisibility(View.GONE);

    }

    private void showSimilarStatementError() {
        similarRetry.setVisibility(View.VISIBLE);
        similarRecycler.setVisibility(View.GONE);
        similarLoader.setVisibility(View.GONE);

        similarRetry.findViewById(R.id.full_retry_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDetails();
            }
        });
    }


}
