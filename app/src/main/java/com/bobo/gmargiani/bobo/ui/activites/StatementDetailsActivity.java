package com.bobo.gmargiani.bobo.ui.activites;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.ui.adapters.RecyclerItemClickListener;
import com.bobo.gmargiani.bobo.ui.views.FullScreenGalleryView;
import com.bobo.gmargiani.bobo.ui.views.GalleryFooter;
import com.bobo.gmargiani.bobo.utils.AppConsts;

import butterknife.BindView;

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

    private StatementItem statementItem;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long id = getIntent().getLongExtra(AppConsts.PARAM_STATEMENT_ID, -1);

        if (id != -1) {
            statementItem = userInfo.getStatementItemById(id);

            if (statementItem != null) {
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

                refreshHeaderText();
            }
        }
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

    @Override
    public void onBackPressed() {
        if (fullGallery.getVisibility() == View.VISIBLE) {
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
        return false;
    }

    @Override
    protected String getHeaderText() {
        if (statementItem != null) {
            return statementItem.getTitle();
        }
        return getString(R.string.activity_name_statement_details);
    }
}
