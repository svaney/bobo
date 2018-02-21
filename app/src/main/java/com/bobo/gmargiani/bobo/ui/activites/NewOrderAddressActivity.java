package com.bobo.gmargiani.bobo.ui.activites;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.RootEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AuthorizedEvent;
import com.bobo.gmargiani.bobo.model.datamodels.Order;
import com.bobo.gmargiani.bobo.model.datamodels.OrderAddress;
import com.bobo.gmargiani.bobo.utils.AlertManager;
import com.bobo.gmargiani.bobo.utils.ViewUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;

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

    @BindView(R.id.delivery_date)
    TextInputEditText deliveryDateET;

    @BindView(R.id.delivery_time)
    TextInputEditText deliveryTimeET;

    @BindView(R.id.tel)
    TextInputEditText phoneET;

    @BindView(R.id.amount)
    TextInputEditText amountET;

    @BindView(R.id.amount_title)
    TextInputLayout amountETTitle;

    @BindView(R.id.delivery_amount)
    TextInputEditText deliveryAmountET;

    @BindView(R.id.amount_type_radio_group)
    RadioGroup amountTypeRadioGroup;

    @BindView(R.id.delivery_time_radio_group)
    RadioGroup deliveryTimeRadioGroup;

    @BindView(R.id.radio_full_amount)
    RadioButton fullamountRadio;

    @BindView(R.id.radio_amount_by_check)
    RadioButton deliveryAmountRadio;

    @BindView(R.id.radio_fast)
    RadioButton fastDeliveryRadio;

    @BindView(R.id.radio_pick_time)
    RadioButton timeRadio;

    @BindView(R.id.deliverty_time_container)
    View deliveryTimeContainer;

    @BindView(R.id.scroll_view)
    ScrollView scrollView;

    private OrderAddress addressInfo = new OrderAddress();

    private Order currentOrder;

    private AuthorizedEvent authorizedEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentOrder = userInfo.getCurrentOrder();
        addressInfo = currentOrder.getOrderAddress();

        setUpClickListeners();
        setDataFromOrderItem();
    }

    private void setDataFromOrderItem() {
        fastDeliveryRadio.setChecked(currentOrder.isAsap());
        timeRadio.setChecked(!currentOrder.isAsap());
        fullamountRadio.setChecked(currentOrder.isFullAmount());
        deliveryAmountRadio.setChecked(!currentOrder.isFullAmount());

        if (currentOrder.getTotalAmount() != null) {
            amountET.setText(String.valueOf(currentOrder.getTotalAmount()));
        }

        if (!currentOrder.isFullAmount() && currentOrder.getDeliveryAmount() != null) {
            deliveryAmountET.setText(String.valueOf(currentOrder.getDeliveryAmount()));
        }

        if (currentOrder.getOrderAddress().getAddress() != null) {
            addressET.setText(currentOrder.getOrderAddress().getAddress());
        }

        if (currentOrder.getOrderAddress().getAddressAdditionalInfo() != null) {
            addressInfoET.setText(currentOrder.getOrderAddress().getAddressAdditionalInfo());
        }

        if (currentOrder.getPhone() != null) {
            phoneET.setText(currentOrder.getPhone());
        }
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
        amountET.addTextChangedListener(amountWatcher);
        deliveryAmountET.addTextChangedListener(deliveryAmountWatcher);
        addressET.addTextChangedListener(addressTextWatcher);
        addressInfoET.addTextChangedListener(addressInfoTextWatcher);
        phoneET.addTextChangedListener(phoneTextWatcher);

        amountTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_full_amount:
                        deliveryAmountET.setText("");
                        deliveryAmountET.setVisibility(View.GONE);
                        currentOrder.setFullAmount(true);
                        amountETTitle.post(new Runnable() {
                            @Override
                            public void run() {
                                amountETTitle.setHint(getString(R.string.address_title_amount));
                            }
                        });
                        break;
                    case R.id.radio_amount_by_check:
                        deliveryAmountET.setVisibility(View.VISIBLE);
                        currentOrder.setFullAmount(false);
                        amountETTitle.post(new Runnable() {
                            @Override
                            public void run() {
                                amountETTitle.setHint(getString(R.string.address_title_product_estimate_amount));
                            }
                        });
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
                        currentOrder.setAsap(true);

                        break;
                    case R.id.radio_pick_time:
                        deliveryTimeContainer.setVisibility(View.VISIBLE);
                        currentOrder.setAsap(false);
                        currentOrder.setReceiveDate(0);
                        break;
                }
            }
        });
    }

    @OnClick(R.id.done)
    protected void onDoneClick() {
        if (!checkReady()) {
            AlertManager.showError(this, getString(R.string.error_text_fill_all_input));
        } else {

        }
    }

    private boolean checkReady() {
        if (!ViewUtils.validateEditText(amountET, this)) {
            return false;
        }

        if (deliveryAmountRadio.isChecked()) {
            if (!ViewUtils.validateEditText(deliveryAmountET, this)) {
                return false;
            }
        }

        if (timeRadio.isChecked()) {
            if (!ViewUtils.validateEditText(deliveryDateET, this)) {
                return false;
            }
            if (!ViewUtils.validateEditText(deliveryTimeET, this)) {
                return false;
            }
        }

        if (TextUtils.isEmpty(addressET.getText())) {
            if (!ViewUtils.validateEditText(addressET, this)) {
                return false;
            }
            return false;
        }

        if (TextUtils.isEmpty(phoneET.getText())) {
            if (!ViewUtils.validateEditText(phoneET, this)) {
                return false;
            }
        }

        return true;
    }

    @OnClick(R.id.address_on_map)
    protected void onMapClick() {

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


    private TextWatcher addressTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            currentOrder.getOrderAddress().setAddress(String.valueOf(charSequence));
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    private TextWatcher addressInfoTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            currentOrder.getOrderAddress().setAddressAdditionalInfo(String.valueOf(charSequence));
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    private TextWatcher phoneTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            currentOrder.setPhone(String.valueOf(charSequence));
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    private TextWatcher amountWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (TextUtils.isEmpty(charSequence)) {
                currentOrder.setTotalAmount(null);
            } else {
                currentOrder.setTotalAmount(new BigDecimal(String.valueOf(charSequence)));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    private TextWatcher deliveryAmountWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (TextUtils.isEmpty(charSequence)) {
                currentOrder.setDeliveryAmount(null);
            } else {
                currentOrder.setDeliveryAmount(new BigDecimal(String.valueOf(charSequence)));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };


}
