package com.example.qy.q233;

import android.graphics.ColorMatrixColorFilter;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Qi Yao on 17-4-12.
 */

public class TouchDark implements View.OnTouchListener {

    private final float[] BT_SELECTED = new float[] {1,0,0,0,-50,0,1,0,0,-50,0,0,1,0,-50,0,0,0,1,0};
    private final float[] BT_NOT_SELECTED = new float[] {1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0};

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            v.getBackground().setColorFilter(
                    new ColorMatrixColorFilter(BT_SELECTED));
            v.setBackgroundDrawable(v.getBackground());
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            v.getBackground().setColorFilter(
                    new ColorMatrixColorFilter(BT_NOT_SELECTED));
            v.setBackgroundDrawable(v.getBackground());
        }
        return false;
    }
}
