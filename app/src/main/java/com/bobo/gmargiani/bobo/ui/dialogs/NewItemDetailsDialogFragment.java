package com.bobo.gmargiani.bobo.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.ui.activites.RootActivity;
import com.bobo.gmargiani.bobo.ui.views.DropDownChooser;
import com.bobo.gmargiani.bobo.utils.AppUtils;

/**
 * Created by gmargiani on 1/31/2018.
 */

public class NewItemDetailsDialogFragment extends DialogFragment {
    private View dummyView;
    private RadioGroup radioGroup;
    private View addButton;
    private View cancelButton;
    private DropDownChooser dropDownChooser;
    private EditText amountInputET;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.fragment_add_new_item_details, null);

        builder.setView(v);

        radioGroup = v.findViewById(R.id.radio_group);
        dummyView = v.findViewById(R.id.dummy_view);
        addButton = v.findViewById(R.id.dismiss_button);
        cancelButton = v.findViewById(R.id.add_button);
        amountInputET = v.findViewById(R.id.amount_input_et);
        dropDownChooser = v.findViewById(R.id.drop_down);

        dropDownChooser.showTitle(false);

        amountInputET.setHint(R.string.product_dialog_radio_weight_title);

        amountInputET.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        amountInputET.setText(String.valueOf(1));

        AppUtils.closeKeyboard(dummyView);

        setUpViewClickListeners();

        ((RadioButton) radioGroup.findViewById(R.id.radio_weight)).setChecked(true);

        return builder.create();

    }

    private void setUpWeightInput() {
        dropDownChooser.setInsideText("kg.");

    }

    private void setUpVolumeInput() {

    }

    private void setUpViewClickListeners() {

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                AppUtils.closeKeyboard(dummyView);
                switch (checkedId) {
                    case R.id.radio_weight:
                        setUpWeightInput();
                        break;
                    case R.id.radio_volume:
                        setUpVolumeInput();
                        break;
                    case R.id.radio_other:
                        //     setUpOtherInput();
                        break;
                }
            }
        });
    }
}
