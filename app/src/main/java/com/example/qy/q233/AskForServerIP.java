package com.example.qy.q233;

import android.app.Activity;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

/**
 * Created by Qi Yao on 17-4-11.
 */

public class AskForServerIP extends PopupWindow {
    Activity mActivity;
    View contentView;
    PopupWindow popupWindow;
    AskForServerIP(Activity outActivity) {
        mActivity = outActivity;
        contentView = mActivity.getLayoutInflater().inflate(R.layout.server_ip, null);

        Button button = (Button) contentView.findViewById(R.id.bt1);
        contentView.setBackgroundColor(Color.WHITE);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //contentView.setBackgroundColor(Color.RED);
                MyApp myApp = (MyApp) mActivity.getApplication();
                EditText editText = (EditText) contentView.findViewById(R.id.ip);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                String url = editText.getText().toString();
                myApp.setUrl("http://" + url);
                popupWindow.dismiss();
            }
        });



        popupWindow = new PopupWindow(contentView,
                1000, 400, true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
    }

    void show() {
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
    }
}
