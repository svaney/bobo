package com.bobo.gmargiani.bobo.ui.dialogs;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.ui.adapters.DialogListAdapter;

import java.util.ArrayList;

public class ListDialog extends BaseDialog {
    public static final int DIALOG_LIST_TYPE_SINGLE = 10;
    public static final int DIALOG_LIST_TYPE_MULTIPLE = 20;

    private int listType;

    private View buttonContainer;
    private Button okButton;
    private Button cancelButton;
    private RecyclerView recyclerView;

    private DialogListAdapter adapter;
    private ArrayList<Pair<String, Boolean>> data;

    public ListDialog(Context context) {
        this(context, DIALOG_LIST_TYPE_SINGLE);
    }

    public ListDialog(Context context, int type) {
        super(context);
        listType = type;
        findViews();

        adapter = new DialogListAdapter(getContext(), data, type);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void findViews() {
        buttonContainer = findViewById(R.id.button_wrapper);
        okButton = findViewById(R.id.ok_button);
        cancelButton = findViewById(R.id.cancel_button);
        recyclerView = findViewById(R.id.recycler_view);

        buttonContainer.setVisibility(listType == DIALOG_LIST_TYPE_MULTIPLE ? View.VISIBLE : View.GONE);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void setList(ArrayList<Pair<String, Boolean>> data) {
        this.data = data;
        if (adapter != null) {
            adapter.setData(data);
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.dialog_list;
    }
}
