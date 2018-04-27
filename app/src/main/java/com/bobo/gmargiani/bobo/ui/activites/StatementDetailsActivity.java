package com.bobo.gmargiani.bobo.ui.activites;

import android.os.Bundle;
import android.support.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.OwnerDetailsEvent;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.ui.adapters.RecyclerItemClickListener;
import com.bobo.gmargiani.bobo.ui.views.FullScreenGalleryView;
import com.bobo.gmargiani.bobo.ui.views.GalleryFooter;
import com.bobo.gmargiani.bobo.utils.AppConsts;
import com.bobo.gmargiani.bobo.utils.ImageLoader;
import com.bobo.gmargiani.bobo.utils.Utils;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class StatementDetailsActivity extends RootDetailedActivity {
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

    private StatementItem statementItem;
    private OwnerDetailsEvent ownerDetailsEvent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long id = getIntent().getLongExtra(AppConsts.PARAM_STATEMENT_ID, -1);

        if (id != -1) {
            statementItem = userInfo.getStatementItemById(id);

            if (statementItem != null) {
                setUpGallery();
                setUpValues();
                refreshHeaderText();
            }
        }
    }

    private void setUpGallery() {
        gallery.setImages(statementItem.getImages());
        gallery.setImageTouchable(false);
        gallery.setOnImageClickListener(new RecyclerItemClickListener() {
            @Override
            public void onRecyclerItemClick(int pos) {
                showFullScreenGallery(true, pos);
            }
        });

        galleryFooter.showFilledFavorite(false);
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

    private void showFullScreenGallery(boolean show) {
        showFullScreenGallery(show, 0);
    }

    private void showFullScreenGallery(boolean show, int pos) {
        fullGalleryWrapper.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) {
            fullGallery.setImages(statementItem.getImages());
            fullGallery.setImageTouchable(true);
            fullGallery.addOnPageChangeListener(fullGalleryFooter);

            fullGalleryFooter.showFilledFavorite(false);
            fullGalleryFooter.setDataSize(statementItem.getImages() == null ? 0 : statementItem.getImages().size());
            fullGalleryFooter.setGallery(fullGallery);
            fullGalleryFooter.setSelectedPos(pos);
        }
    }

    private void setUpValues() {
        statementTitle.setText(statementItem.getTitle());
        statementPrice.setText(Utils.getAmountWithGel(statementItem.getPrice()));

        statementViews.setText(String.valueOf(statementItem.getTotalViews()));
        statementFavorites.setText(String.valueOf(statementItem.getTotalFavorites()));

        statementDate.setText(Utils.getFullDate(statementItem.getCreateDate(), this));

        ImageLoader.load(icFavorites)
                .setRes(R.drawable.ic_favorite)
                .applyTint(R.color.ic_grey_color)
                .build();

        ImageLoader.load(icViews)
                .setRes(R.drawable.ic_views)
                .applyTint(R.color.ic_grey_color)
                .build();
    }

    private void setUpDetails() {
        if (statementItem != null && statementItem.getOwnerDetails() != null) {

            ImageLoader.load(ownerProfPic)
                    .setUrl(statementItem.getOwnerDetails().getAvatar())
                    .setOval(true)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestDetails();
    }

    private void requestDetails() {
        if (statementItem != null && statementItem.getOwnerDetails() == null) {
            userInfo.requestOwnerDetails(statementItem.getStatementId(), statementItem.getOwnerId());
        } else if (statementItem != null) {
            setUpDetails();
        }
    }

    @Subscribe
    public void onOwnerDetailsEvent(OwnerDetailsEvent event) {
        if (ownerDetailsEvent != event && statementItem != null && statementItem.getOwnerId() == event.getOwnerId()) {
            ownerDetailsEvent = event;
            switch (event.getState()) {
                case RootEvent.STATE_LOADING:
                    showFullLoading();
                    break;
                case RootEvent.STATE_ERROR:
                    showFullError();
                    break;
                case RootEvent.STATE_SUCCESS:
                    showContent();
                    setUpDetails();
                    break;
            }
        }
    }

    @OnClick(R.id.full_retry_button)
    public void onRetryClick() {
        requestDetails();
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
}
