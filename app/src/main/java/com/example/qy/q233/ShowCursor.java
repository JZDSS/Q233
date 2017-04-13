package com.example.qy.q233;

import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Qi Yao on 17-04-13.
 */

public class ShowCursor implements View.OnTouchListener {

    boolean show;
    ShowCursor(boolean show) {
        this.show = show;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event){
        ((EditText) v).setCursorVisible(true);
        return false;
    }
}
