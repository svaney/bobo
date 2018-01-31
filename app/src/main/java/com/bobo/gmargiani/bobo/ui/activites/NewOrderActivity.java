package com.bobo.gmargiani.bobo.ui.activites;

import android.os.Bundle;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.events.AuthorizedEvent;
import com.bobo.gmargiani.bobo.model.NewOrderItem;
import com.bobo.gmargiani.bobo.utils.AlertManager;

import java.util.ArrayList;

import butterknife.OnClick;

/**
 * Created by gmargiani on 1/31/2018.
 */

public class NewOrderActivity extends ProfileBarActivity {
    private ArrayList<NewOrderItem> orderItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onAuthorizedSuccessEvent(AuthorizedEvent event) {

    }

    @OnClick(R.id.add_product_button)
    protected void onNewProductButtonClick() {
        showConditionalDialog();
    }

    @OnClick(R.id.next_step)
    protected void onNextStepClick() {
        if (orderItems.size() == 0 ){
            AlertManager.showError(this, getString(R.string.new_order_error_now_items));
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_order;
    }

}
