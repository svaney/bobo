package com.bobo.gmargiani.bobo.utils;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.app.App;
import com.bobo.gmargiani.bobo.model.ProductItem;
import com.bobo.gmargiani.bobo.utils.consts.ModelConsts;

/**
 * Created by gmarg on 2/1/2018.
 */

public class ModelUtils {
    public static String getNewProductDescription(ProductItem item) {
        String desc = "";

        if (item != null) {
            switch (item.getUnitType()) {
                case ModelConsts.PRODUCT_UNIT_TYPE_OTHER:
                    desc = item.getComment();
                    break;
                case ModelConsts.PRODUCT_UNIT_TYPE_WEIGHT:
                    desc = getFormattedWeightText(item);
                    break;
                case ModelConsts.PRODUCT_UNIT_TYPE_VOLUME:
                    desc = getFormattedVolumeText(item);
                    break;
            }
        }

        return desc;
    }

    public static String getFormattedWeightText(ProductItem item) {
        String ans = item.getAmount() + " ";

        if (item != null && item.getUnitType() == ModelConsts.PRODUCT_UNIT_TYPE_WEIGHT) {

            switch (item.getMeasureUnitType()) {
                case ModelConsts.WEIGHT_UNIT_MEASURE_TYPE_KILOGRAM:
                    ans += App.getInstance().getString(R.string.weight_kg_long);
                    break;
                case ModelConsts.WEIGHT_UNIT_MEASURE_TYPE_GRAM:
                    ans += App.getInstance().getString(R.string.weight_gram_long);
                    break;
            }
        }
        return ans;
    }

    public static String getFormattedVolumeText(ProductItem item) {
        String ans = item.getAmount() + " ";

        if (item != null && item.getUnitType() == ModelConsts.PRODUCT_UNIT_TYPE_VOLUME) {

            switch (item.getMeasureUnitType()) {
                case ModelConsts.VOLUME_UNIT_MEASURE_TYPE_LITRE:
                    ans += App.getInstance().getString(R.string.volume_litre_long);
                    break;
                case ModelConsts.VOLUME_UNIT_MEASURE_TYPE_MILLILITRE:
                    ans += App.getInstance().getString(R.string.volume_millilitre_long);
                    break;
            }
        }
        return ans;
    }
}
