package com.example.qy.q233;

import android.app.Activity;

import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by Qi Yao on 17/05/15.
 */

public class FileContent extends PopupWindow {
    private Activity mActivity;
    private View contentView;
    private PopupWindow popupWindow;

    FileContent(Activity outActivity, String s) {
        mActivity = outActivity;

        contentView = mActivity.getLayoutInflater().inflate(R.layout.file_content, null);
        //contentView.setBackgroundColor(Color.WHITE);
        ((TextView) contentView.findViewById(R.id.content)).setText(s);
        popupWindow = new PopupWindow(contentView, 800, 1300, true);
    }
    void show() {
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
    }
}
