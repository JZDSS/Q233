package com.example.qy.q233;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Qi Yao on 17-04-13.
 */

public class EditTextViewBackgroundSwitcher implements View.OnFocusChangeListener {
    int id1;
    int id2;
    Activity activity;
    EditTextViewBackgroundSwitcher(Activity activity, int id1, int id2)
    {
        this.activity = activity;
        this.id1 = id1;
        this.id2 = id2;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        ((EditText) v).setCursorVisible(true);
        if (hasFocus) {
            ((EditText) v).setBackground(activity.getDrawable(id1));
        } else {
            if (((EditText) v).getText().toString().isEmpty()){
                ((EditText)v).setBackground(activity.getDrawable(id2));
            }
        }
    }
}
