package com.bobo.gmargiani.bobo.ui.activites;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AuthorizedEvent;
import com.bobo.gmargiani.bobo.utils.ViewUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gmargiani on 2/7/2018.
 */

public class NewOrderAddressActivity extends RootDetailedActivity {
    @BindView(R.id.address)
    TextInputEditText addressET;

    @BindView(R.id.address_info)
    TextInputEditText addressInfoET;

    @BindView(R.id.tel)
    TextInputEditText telNumberET;

    @BindView(R.id.amount)
    TextInputEditText amountET;

    @BindView(R.id.delivery_amount)
    TextInputEditText deliveryAmountET;

    @BindView(R.id.amount_type_radio_group)
    RadioGroup amountTypeRadioGroup;

    @BindView(R.id.delivery_time_radio_group)
    RadioGroup deliveryTimeRadioGroup;

    @BindView(R.id.radio_full_amount)
    RadioButton fullamountRadio;

    @BindView(R.id.radio_amount_by_check)
    RadioButton checkAmountRadio;

    @BindView(R.id.radio_fast)
    RadioButton fastDeliveryRadio;

    @BindView(R.id.radio_pick_time)
    RadioButton timeRadio;

    @BindView(R.id.deliverty_time_container)
    View deliveryTimeContainer;

    @BindView(R.id.scroll_view)
    ScrollView scrollView;


    private AuthorizedEvent authorizedEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fullamountRadio.setChecked(true);
        fastDeliveryRadio.setChecked(true);

        setUpClickListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();

        userInfo.requestAuthorizedEvent();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAuthorizedEvent(AuthorizedEvent event) {
        if (event != authorizedEvent) {
            authorizedEvent = event;
            switch (event.getState()) {
                case RootEvent.STATE_SUCCESS:
                    break;
            }
        }
    }

    private void setUpClickListeners() {
        amountTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_full_amount:
                        deliveryAmountET.setVisibility(View.GONE);
                        break;
                    case R.id.radio_amount_by_check:
                        deliveryAmountET.setVisibility(View.VISIBLE);
                        break;
                }

            }
        });

        deliveryTimeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_fast:
                        deliveryTimeContainer.setVisibility(View.GONE);
                        break;
                    case R.id.radio_pick_time:
                        deliveryTimeContainer.setVisibility(View.VISIBLE);
                        break;
                }

                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });
    }

    @OnClick(R.id.address_on_map)
    protected void onMapClick() {

    }

    @OnClick(R.id.done)
    protected void onDoneClick() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_order_address;
    }

    @Override
    public boolean needEventBus() {
        return true;
    }

    protected int getHeaderText() {
        return R.string.activity_name_new_order_address;
    }

}
