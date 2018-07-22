package com.bobo.gmargiani.bobo.ui.adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.model.StatementItem;
import com.bobo.gmargiani.bobo.model.UserInfo;
import com.bobo.gmargiani.bobo.utils.DummyRecyclerViewHolder;
import com.bobo.gmargiani.bobo.utils.ImageLoader;
import com.bobo.gmargiani.bobo.utils.Utils;

import java.util.ArrayList;

public class StatementRecyclerAdapter extends InfinityAdapter {

    public static final int ADAPTER_TYPE_LIST = 10;
    public static final int ADAPTER_TYPE_GRID = 20;
    public static final int ADAPTER_TYPE_SIMILAR = 30;

    private UserInfo userInfo;

    private StatementItemClickListener clickListener;

    private int adapterType;

    public boolean isGrid() {
        return adapterType == ADAPTER_TYPE_GRID;
    }

    public StatementRecyclerAdapter(Context context, int adapterType, StatementItemClickListener clickListener, LazyLoaderListener listener, UserInfo userInfo) {
        this.context = context;
        this.adapterType = adapterType;
        this.lazyLoaderListener = listener;
        this.clickListener = clickListener;
        this.userInfo = userInfo;
    }


    @Override
    RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HOLDER_TYPE_ITEM) {
            int resId = R.layout.recycler_item_statement;
            switch (adapterType) {
                case ADAPTER_TYPE_GRID:
                    resId = R.layout.recycler_item_statement_grid;
                    break;
                case ADAPTER_TYPE_LIST:
                    resId = R.layout.recycler_item_statement;
                    break;
                case ADAPTER_TYPE_SIMILAR:
                    resId = R.layout.recycler_item_similar_statement;
                    break;
            }
            View view = LayoutInflater.from(context).inflate(resId, parent, false);
            return new StatementHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        if (h.getItemViewType() == HOLDER_TYPE_ITEM) {
            StatementHolder holder = (StatementHolder) h;
            StatementItem item = (StatementItem) items.get(position);
            holder.title.setText(item.getTitle());
            holder.price.setText(Utils.getAmountWithGel(item.getPrice()));

            ImageLoader.load(holder.image)
                    .setUrl(item.getMainImage())
                    .setErroPlaceHolder(R.drawable.statement_image_place_holder)
                    .build();


            if (holder.favoritesIcFilled != null && holder.favoritesIc != null) {
                if (userInfo.isUsersItem(item.getOwnerId())) {
                    holder.favoritesIcFilled.setVisibility(View.VISIBLE);
                    holder.favoritesIc.setVisibility(View.GONE);
                    ImageLoader.load(holder.favoritesIcFilled)
                            .setRes(R.drawable.ic_settings)
                            .applyTint(true)
                            .build();
                } else if (userInfo.isStatementFavorite(item.getStatementId())) {
                    holder.favoritesIcFilled.setVisibility(View.VISIBLE);
                    holder.favoritesIc.setVisibility(View.GONE);
                } else {
                    holder.favoritesIc.setVisibility(View.VISIBLE);
                    holder.favoritesIcFilled.setVisibility(View.GONE);
                }
            }

        }
    }


    class StatementHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView price;
        ImageView image;
        ImageView favoritesIc;
        ImageView favoritesIcFilled;

        public StatementHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.statement_title);
            price = itemView.findViewById(R.id.statement_price);
            image = itemView.findViewById(R.id.statement_image);
            favoritesIc = itemView.findViewById(R.id.item_favorites_ic);
            favoritesIcFilled = itemView.findViewById(R.id.item_favorites_ic_filled);

            if (favoritesIc != null) {
                ImageLoader.load(favoritesIc)
                        .setRes(R.drawable.ic_favorite)
                        .applyTint(adapterType == ADAPTER_TYPE_GRID ? R.color.color_white : R.color.ic_grey_color)
                        .build();

                favoritesIc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (clickListener != null) {
                            clickListener.onFavoritesClick(getAdapterPosition());
                        }
                    }
                });
            }


            if (favoritesIcFilled != null) {
                ImageLoader.load(favoritesIcFilled)
                        .setRes(R.drawable.ic_favorite_filled)
                        .applyTint(true)
                        .build();

                favoritesIcFilled.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (clickListener != null) {
                            clickListener.onFavoritesClick(getAdapterPosition());
                        }
                    }
                });
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.onItemClick(getAdapterPosition());
                    }
                }
            });


        }
    }

    public interface StatementItemClickListener {
        void onItemClick(int position);

        void onFavoritesClick(int position);
    }
}
