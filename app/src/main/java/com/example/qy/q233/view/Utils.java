package com.example.qy.q233.view;

import android.content.res.Resources;

/**
 * Created by Xu Yining on 2017/5/5.
 */

public final class Utils {
    
    private Utils() {
    }
    
    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp){
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}
