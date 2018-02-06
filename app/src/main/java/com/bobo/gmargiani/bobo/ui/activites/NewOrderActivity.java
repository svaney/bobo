package com.bobo.gmargiani.bobo.ui.activites;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.model.ProductItem;
import com.bobo.gmargiani.bobo.ui.adapters.NewProductRecyclerAdapter;
import com.bobo.gmargiani.bobo.ui.adapters.interfaces.NewProductAdapterListener;
import com.bobo.gmargiani.bobo.ui.dialogs.NewProductDialogFragment;
import com.bobo.gmargiani.bobo.utils.AlertManager;
import com.bobo.gmargiani.bobo.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

/**
 * Created by gmargiani on 1/31/2018.
 */

public class NewOrderActivity extends RootDetailedActivity implements NewProductAdapterListener {


    @BindView(R.id.products_recycler)
    RecyclerView productsRecycler;

    @BindView(R.id.new_product_button)
    View newProductButton;

    @BindView(R.id.next_step)
    View nextStepButton;

    @BindView(R.id.order_description_et)
    EditText orderDescriptionET;


    private ArrayList<ProductItem> orderItems = new ArrayList<>();
    private NewProductRecyclerAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productsRecycler.setLayoutManager(new LinearLayoutManager(productsRecycler.getContext()));
        adapter = new NewProductRecyclerAdapter(orderItems, this, this);
        productsRecycler.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

       // showTutorialIfNeeded();
    }

    private void showTutorialIfNeeded() {
        ShowcaseConfig config = new ShowcaseConfig();

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, new Date().toString());

        sequence.setConfig(config);

        sequence.addSequenceItem(newProductButton,
                getString(R.string.tip_text_new_order_add_product), getString(R.string.tip_button_text_next));

        sequence.addSequenceItem(orderDescriptionET,
                getString(R.string.tip_text_new_order_order_description), getString(R.string.tip_button_text_next));

        sequence.addSequenceItem(nextStepButton,
                getString(R.string.tip_text_new_order_finish_product), getString(R.string.tip_button_text_close));


        sequence.start();
    }

    @OnClick(R.id.new_product_button)
    protected void onNewProductButtonClick() {
        NewProductDialogFragment fr = new NewProductDialogFragment();
        fr.show(getSupportFragmentManager(), "DIALOG");
    }


    @OnClick(R.id.next_step)
    protected void onNextStepClick() {
        if (orderItems.size() == 0) {
            AlertManager.showError(this, getString(R.string.error_order_without_items));
            ViewUtils.shakeView(newProductButton);
        }
    }

    public void addNewProduct(ProductItem productItem) {
        orderItems.add(0, productItem);
        adapter.notifyItemInserted(0);
        productsRecycler.smoothScrollToPosition(0);
    }


    @Override
    public void onItemClick(int position) {
        NewProductDialogFragment fr = new NewProductDialogFragment();
        fr.setEditItem(orderItems.get(position));
        fr.show(getSupportFragmentManager(), "DIALOG");
    }

    @Override
    public void onItemDelete(int position) {
        orderItems.remove(position);
        adapter.notifyItemRemoved(position);
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
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
