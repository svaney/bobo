package com.bobo.gmargiani.bobo.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.model.OwnerDetails;
import com.bobo.gmargiani.bobo.utils.ImageLoader;

public class OwnerAdapter extends InfinityAdapter {
    private StatementRecyclerAdapter.StatementItemClickListener clickListener;

    public OwnerAdapter(Context context, StatementRecyclerAdapter.StatementItemClickListener clickListener, LazyLoaderListener listener) {
        this.context = context;
        this.lazyLoaderListener = listener;
        this.clickListener = clickListener;
    }

    @Override
    RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HOLDER_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_owner, parent, false);
            return new OwnerHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        if (h.getItemViewType() == HOLDER_TYPE_ITEM) {
            OwnerHolder holder = (OwnerHolder) h;
            OwnerDetails item = (OwnerDetails) items.get(position);

            holder.ownerMail.setText(item.getPhone());
            holder.ownerName.setText(item.getDisplayName());

            if (!TextUtils.isEmpty(item.getAvatar())) {
                ImageLoader.load(holder.ownerPic)
                        .setUrl(item.getAvatar())
                        .setOval(true)
                        .build();
            } else {
                ImageLoader.load(holder.ownerPic)
                        .setRes(R.drawable.ic_avatar_default)
                        .setOval(true)
                        .build();
            }

            ImageLoader.load(holder.subscribeImage)
                    .setRes(R.drawable.ic_subscribe_empty)
                    .applyTint(false)
                    .build();

        }
    }

    class OwnerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ownerPic;
        View subscribeButton;
        ImageView subscribeImage;
        TextView ownerName;
        TextView ownerMail;
        TextView subscribeText;

        public OwnerHolder(View itemView) {
            super(itemView);
            ownerPic = itemView.findViewById(R.id.owner_image);
            subscribeButton = itemView.findViewById(R.id.subscribe_container);
            subscribeImage = itemView.findViewById(R.id.ic_subscribe);
            ownerName = itemView.findViewById(R.id.owner_name);
            ownerMail = itemView.findViewById(R.id.owner_mail);
            subscribeText = itemView.findViewById(R.id.subscribe_text);
            itemView.setOnClickListener(this);
            subscribeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.onFavoritesClick(getAdapterPosition());
                    }
                }
            });

            subscribeText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.onFavoritesClick(getAdapterPosition());
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}
