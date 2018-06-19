package com.bobo.gmargiani.bobo.ui.activites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.transition.TransitionManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.ActivityResultEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.DeniedPermissionsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.AppEvents.GrantedPermissionsEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.CategoriesEvent;
import com.bobo.gmargiani.bobo.evenbuts.events.LocationsEvent;
import com.bobo.gmargiani.bobo.model.KeyValue;
import com.bobo.gmargiani.bobo.ui.dialogs.DialogPair;
import com.bobo.gmargiani.bobo.ui.dialogs.ListDialog;
import com.bobo.gmargiani.bobo.ui.views.FilterTextView;
import com.bobo.gmargiani.bobo.ui.views.NewImageView;
import com.bobo.gmargiani.bobo.utils.AlertManager;
import com.bobo.gmargiani.bobo.utils.AppConsts;
import com.bobo.gmargiani.bobo.utils.AppUtils;
import com.bobo.gmargiani.bobo.utils.ImagePicker;
import com.bobo.gmargiani.bobo.utils.ViewUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class NewStatementActivity extends RootDetailedActivity implements NewImageView.NewImageListener {
    @BindView(R.id.new_images_wrapper)
    LinearLayout newImagesWrapper;

    @BindView(R.id.category)
    View categoryValidateView;

    @BindView(R.id.location)
    View locationValidateView;

    @BindView(R.id.statement_name)
    EditText statementName;

    @BindView(R.id.statement_desc)
    EditText statementDesc;

    @BindView(R.id.category_values_wrapper)
    LinearLayout categoryValuesWrapper;

    @BindView(R.id.location_values_wrapper)
    LinearLayout locationValuesWrapper;

    @BindView(R.id.edit_text_wrapper)
    View statementNameWrapper;

    private LocationsEvent locationsEvent;
    private CategoriesEvent categoriesEvent;


    private ArrayList<Bitmap> userImages = new ArrayList<>();
    private ArrayList<String> userLocations = new ArrayList<>();
    private ArrayList<String> userCategories = new ArrayList<>();

    public static void start(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, NewStatementActivity.class));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoriesEvent = userInfo.getCategoriesEvent();
        locationsEvent = userInfo.getLocationsEvent();

        newImagesWrapper.addView(new NewImageView(this));

        setValues();
    }

    private void setValues() {
        setLocationValues();
        setCategoryValues();
        setImageValues();
    }

    @OnClick(R.id.add_statement_button)
    public void onCreateClick() {
        if (userCategories.isEmpty()) {
            AlertManager.showError(this, "გთხოვთ მიუთითოთ თუ რა კატერგორიას მიეკუთვნება პროდუქტი");
            ViewUtils.shakeView(categoryValidateView);
        } else if (userLocations.isEmpty()) {
            AlertManager.showError(this, "გთხოვთ მიუთითოთ თუ სად მდებარეობს პროდუქტი");
            ViewUtils.shakeView(locationValidateView);
        } else if (TextUtils.isEmpty(statementName.getText())) {
            AlertManager.showError(this, "გთხოვთ მიუთითოთ პროდუქტი დასახელება");
            ViewUtils.shakeView(statementNameWrapper);
        } else if (TextUtils.isEmpty(statementDesc.getText())) {
            AlertManager.showError(this, "გთხოვთ მიუთითოთ პროდუქტი აწერა");
            ViewUtils.shakeView(statementDesc);
        }
    }

    @Subscribe
    public void onActivityResultEvent(ActivityResultEvent event) {
        if (ImagePicker.isImagePickedSuccessfully(event.getRequestCode(), event.getResultCode())) {
            ImagePicker.beginCrop(this, event.getResultCode(), event.getData(), 0);

        } else if (ImagePicker.isImageCroppedSuccessfully(event.getRequestCode(), event.getResultCode())) {
            Bitmap bitmap = ImagePicker.getCroppedImage(this, event.getResultCode(), event.getData());

            userImages.add(bitmap);
            setImageValues();

        }
    }


    @OnClick(R.id.category)
    public void onCategoryClick() {
        ListDialog categoryDialog = new ListDialog(this, ListDialog.DIALOG_LIST_TYPE_MULTIPLE, new ListDialog.ListDialogItemsSelectedListener() {
            @Override
            public void onItemsSelected(ArrayList<Integer> itemPositions) {
                if (itemPositions != null) {
                    userCategories.clear();
                    for (Integer i : itemPositions) {
                        userCategories.add(categoriesEvent.getCategories().get(i).getKey());
                    }
                    setCategoryValues();
                }
            }
        });

        ArrayList<DialogPair> data = new ArrayList<>();

        for (KeyValue kv : categoriesEvent.getCategories()) {
            data.add(new DialogPair(kv.getValue(), userCategories.contains(kv.getKey())));
        }

        categoryDialog.setList(data);
        categoryDialog.show();
    }

    @OnClick(R.id.location)
    public void onLocationClick() {
        ListDialog locationDialog = new ListDialog(this, ListDialog.DIALOG_LIST_TYPE_MULTIPLE, new ListDialog.ListDialogItemsSelectedListener() {
            @Override
            public void onItemsSelected(ArrayList<Integer> itemPositions) {
                if (itemPositions != null) {
                    userLocations.clear();
                    for (Integer i : itemPositions) {
                        userLocations.add(locationsEvent.getLocations().get(i).getKey());
                    }
                    setLocationValues();
                }
            }
        });

        ArrayList<DialogPair> data = new ArrayList<>();

        for (KeyValue kv : locationsEvent.getLocations()) {
            data.add(new DialogPair(kv.getValue(), userLocations.contains(kv.getKey())));
        }

        locationDialog.setList(data);
        locationDialog.show();

    }

    @Override
    public void onCloseImageClick(int position) {
        if (position < userImages.size()) {
            userImages.remove(position);
            setImageValues();
            try {
                TransitionManager.beginDelayedTransition(newImagesWrapper);
            } catch (Exception ignored) {
            }
        }
    }


    @SuppressLint("NewApi")
    @Override
    public void onImageClick(int position) {
        lastClickedImagePosition = position;
        if (position < userImages.size()) {

        } else {
            if (!AppUtils.hasPermission(this, Manifest.permission.CAMERA) && !permissionRequested) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, AppConsts.PERMISSION_CAMERA);
            } else {
                permissionRequested = false;
                ImagePicker.pickImageUsingChooser(NewStatementActivity.this, R.string.app_name, R.string.app_name);
            }
        }
    }

    private int lastClickedImagePosition;
    private boolean permissionRequested;

    @Subscribe
    public void onGrantedPermissionEvent(GrantedPermissionsEvent event) {
        if (event.getGrantedPermissions().size() > 0) {
            if (event.getRequestCode() == AppConsts.PERMISSION_CAMERA) {
                onImageClick(lastClickedImagePosition);
            }
        }
    }

    @Subscribe
    public void onDeniedPermissionEvent(DeniedPermissionsEvent event) {
        if (event.getDeniedPermissions().size() > 0) {
            if (event.getRequestCode() == AppConsts.PERMISSION_CAMERA) {
                permissionRequested = true;
                onImageClick(lastClickedImagePosition);
                AlertManager.showError(this, "თქვენ უარი თქვით კამერის გამოყენების უფლებაზე. შესაბამისად შესაძლებელი იქნება სურათების მხოლოდ გალერეედან ატვირთვა", AlertManager.VERY_LONG);
            }
        }
    }

    private void setImageValues() {
        newImagesWrapper.removeAllViews();

        for (int i = 0; i < userImages.size(); i++) {
            NewImageView img = new NewImageView(this);
            img.setBitmap(userImages.get(i));
            img.setListener(this, i);
            newImagesWrapper.addView(img);
        }

        if (userImages.size() < 5) {
            NewImageView img = new NewImageView(this);
            img.setListener(this, userImages.size() + 1);
            img.clearImage();
            newImagesWrapper.addView(img);
        }
    }

    private void setCategoryValues() {
        categoryValuesWrapper.removeAllViews();

        if (!userCategories.isEmpty()) {
            for (int i = 0; i < userCategories.size(); i++) {
                String currKey = userCategories.get(i);
                final FilterTextView txt = new FilterTextView(this);
                txt.setText(categoriesEvent.getValueByKey(currKey));
                txt.showCloseButton(true);
                categoryValuesWrapper.addView(txt);

                txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCategoryClick();
                    }
                });

                final int finalPos = i;
                txt.setOnCloseClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userCategories.remove(finalPos);
                        setCategoryValues();

                        try {
                            TransitionManager.beginDelayedTransition(categoryValuesWrapper);
                        } catch (Exception ignored) {
                        }
                    }
                });
            }

        }
    }

    private void setLocationValues() {
        locationValuesWrapper.removeAllViews();

        if (!userLocations.isEmpty()) {
            for (int i = 0; i < userLocations.size(); i++) {
                String currKey = userLocations.get(i);
                final FilterTextView txt = new FilterTextView(this);
                txt.setText(locationsEvent.getValueByKey(currKey));
                txt.showCloseButton(true);
                locationValuesWrapper.addView(txt);

                txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLocationClick();
                    }
                });

                final int finalPos = i;
                txt.setOnCloseClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userLocations.remove(finalPos);
                        setLocationValues();

                        try {
                            TransitionManager.beginDelayedTransition(locationValuesWrapper);
                        } catch (Exception ignored) {
                        }
                    }
                });
            }

        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_new_statement;
    }

    @Override
    public boolean needEventBus() {
        return true;
    }

    @Override
    protected String getHeaderText() {
        return getString(R.string.activity_name_new_statement);
    }


}
