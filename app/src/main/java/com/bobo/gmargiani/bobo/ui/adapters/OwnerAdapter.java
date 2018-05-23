package com.bobo.gmargiani.bobo.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.model.OwnerDetails;
import com.bobo.gmargiani.bobo.utils.ImageLoader;

public class OwnerAdapter extends InfinityAdapter {
    private RecyclerItemClickListener clickListener;

    public OwnerAdapter(Context context, RecyclerItemClickListener clickListener, LazyLoaderListener listener) {
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

            ImageLoader.load(holder.ownerPic)
                    .setUrl(item.getAvatar())
                    .setOval(true)
                    .build();

            ImageLoader.load(holder.subscribeImage)
                    .setRes(R.drawable.ic_subscribe_full)
                    .applyTint(true)
                    .build();

        }
    }

    class OwnerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ownerPic;
        View subscribeButton;
        ImageView subscribeImage;
        TextView ownerName;
        TextView ownerMail;

        public OwnerHolder(View itemView) {
            super(itemView);
            ownerPic = itemView.findViewById(R.id.owner_image);
            subscribeButton = itemView.findViewById(R.id.subscribe_container);
            subscribeImage = itemView.findViewById(R.id.ic_subscribe);
            ownerName = itemView.findViewById(R.id.owner_name);
            ownerMail = itemView.findViewById(R.id.owner_mail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onRecyclerItemClick(getAdapterPosition());
            }
        }
    }
}
