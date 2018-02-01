package com.bobo.gmargiani.bobo.ui.activites;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.model.ProductItem;
import com.bobo.gmargiani.bobo.ui.dialogs.NewIProductDialogFragment;
import com.bobo.gmargiani.bobo.utils.AlertManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gmargiani on 1/31/2018.
 */

public class NewOrderActivity extends RootDetailedActivity {
    @BindView(R.id.products_recycler)
    RecyclerView productsRecycler;

    private ArrayList<ProductItem> orderItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }


    @OnClick(R.id.add_product_button)
    protected void onNewProductButtonClick() {
        final NewIProductDialogFragment dialog = new NewIProductDialogFragment();

        dialog.show(this.getSupportFragmentManager(), "NEW_ITEM");
        dialog.setCancelable(false);
    }


    @OnClick(R.id.next_step)
    protected void onNextStepClick() {
        if (orderItems.size() == 0) {
            AlertManager.showError(this, getString(R.string.new_order_error_now_items));
        }
    }

    public void addNewProduct(ProductItem productItem) {
        orderItems.add(productItem);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_order;
    }

    @Override
    public boolean needEventBus() {
        return false;
    }


}
