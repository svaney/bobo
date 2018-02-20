package com.bobo.gmargiani.bobo.ui.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobo.gmargiani.bobo.R;

/**
 * Created by gmargiani on 2/6/2018.
 */

public class AmountIncrementView extends LinearLayout {
    private TextView textView;
    private View increment;
    private View decrement;
    private View title;
    private AmountViewListener listener;
    private boolean showTitle = true;
    private int currAmount = 1;

    public AmountIncrementView(Context context) {
        super(context);
        setUpView(context);
    }

    public AmountIncrementView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUpView(context);
    }

    public void showTitle(boolean showTitle) {
        this.showTitle = showTitle;
        setTitleVisibility();
    }

    public void setAmount(int amount) {
        if (amount > 0 && amount < 1000) {
            currAmount = amount;
            setCurrAmount();
        }
    }

    public void setAmountListener(AmountViewListener listener) {
        this.listener = listener;
    }

    private void setUpView(Context context) {
        inflate(context, R.layout.component_amount_increment_view, this);

        textView = findViewById(R.id.amount_et);
        increment = findViewById(R.id.increment);
        decrement = findViewById(R.id.decrement);
        title = findViewById(R.id.title);

        increment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int value = Integer.parseInt(textView.getText().toString());
                    if (value < 999) {
                        textView.setText(String.valueOf(value + 1));
                    }
                } catch (Exception e) {
                    textView.setText(String.valueOf(1));
                }

                if (listener != null) {
                    listener.onAmountChange();
                }
            }
        });

        decrement.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int value = Integer.parseInt(textView.getText().toString());
                    if (value > 1) {
                        textView.setText(String.valueOf(value - 1));
                    }
                } catch (Exception e) {
                    textView.setText(String.valueOf(1));
                }

                if (listener != null) {
                    listener.onAmountChange();
                }
            }
        });

        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    textView.setText(String.valueOf(1));
                } else {
                    currAmount = Integer.parseInt(s.toString());
                }
            }
        });

        setTitleVisibility();
        setCurrAmount();
    }

    public int getAmount() {
        return currAmount;
    }

    private void setCurrAmount() {
        if (textView != null) {
            textView.setText(String.valueOf(currAmount));
        }
    }

    private void setTitleVisibility() {
        if (title != null) {
            title.setVisibility(showTitle ? VISIBLE : GONE);
        }
    }

    public interface AmountViewListener {
        void onAmountChange();
    }
}
