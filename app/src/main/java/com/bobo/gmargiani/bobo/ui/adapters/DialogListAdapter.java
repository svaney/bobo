package com.bobo.gmargiani.bobo.ui.adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.ui.dialogs.DialogPair;
import com.bobo.gmargiani.bobo.ui.dialogs.ListDialog;

import java.util.ArrayList;

public class DialogListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HOLDER_TYPE_SINGLE = 10;
    private static final int HOLDER_TYPE_MULTIPLE = 20;

    private int listType;
    private Context context;
    private ArrayList<DialogPair> data;
    private RecyclerItemClickListener listener;

    public DialogListAdapter(Context context, ArrayList<DialogPair> data, int type, RecyclerItemClickListener listener) {
        this.listType = type;
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    public void setData(ArrayList<DialogPair> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (listType == ListDialog.DIALOG_LIST_TYPE_MULTIPLE) {
            return HOLDER_TYPE_MULTIPLE;
        }
        return HOLDER_TYPE_SINGLE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HOLDER_TYPE_MULTIPLE) {
            View view = LayoutInflater.from(context).inflate(R.layout.recycle_item_dialog_multi, parent, false);
            return new MultiItemHolder(view);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_dialog_single, parent, false);
        return new SingleItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case HOLDER_TYPE_MULTIPLE:
                MultiItemHolder h = (MultiItemHolder) holder;
                h.itemText.setText(data.get(position).getKey());
                h.itemCheck.setChecked(data.get(position).getValue());
                break;
            default:
                SingleItemHolder h1 = (SingleItemHolder) holder;
                h1.itemRadio.setOnCheckedChangeListener(null);
                h1.itemText.setText(data.get(position).getKey());
                h1.itemRadio.setChecked(data.get(position).getValue());
                h1.itemRadio.setOnCheckedChangeListener(h1);
                break;
        }
    }

    public ArrayList<Integer> getSelectedPositions() {
        ArrayList<Integer> positions = new ArrayList<>();

        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getValue()) {
                    positions.add(new Integer(i));
                }
            }
        }

        return positions;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class MultiItemHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        TextView itemText;
        CheckBox itemCheck;
        View itemWrapper;

        public MultiItemHolder(final View itemView) {
            super(itemView);

            itemText = itemView.findViewById(R.id.item_text);
            itemCheck = itemView.findViewById(R.id.item_check);
            itemWrapper = itemView.findViewById(R.id.item_wrapper);

            itemCheck.setOnCheckedChangeListener(this);
            itemWrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemCheck.setChecked(!itemCheck.isChecked());
                }
            });
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (data != null) {
                data.get(getAdapterPosition()).setValue(b);
            }
        }
    }

    class SingleItemHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        TextView itemText;
        RadioButton itemRadio;
        View itemWrapper;

        public SingleItemHolder(View itemView) {
            super(itemView);

            itemText = itemView.findViewById(R.id.item_text);
            itemRadio = itemView.findViewById(R.id.item_radio);
            itemWrapper = itemView.findViewById(R.id.item_wrapper);

            itemRadio.setOnCheckedChangeListener(this);
            itemWrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemRadio.setChecked(!itemRadio.isChecked());
                }
            });
        }


        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (data != null) {
                for (DialogPair p : data) {
                    p.setValue(false);
                }
                data.get(getAdapterPosition()).setValue(b);

                if (listener != null){
                    listener.onRecyclerItemClick(getAdapterPosition());
                }
            }
        }
    }
}
