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
    private Activity mActivity;
    private View contentView;
    private PopupWindow popupWindow;
    AskForServerIP(Activity outActivity) {
        mActivity = outActivity;
        contentView = mActivity.getLayoutInflater().inflate(R.layout.server_ip, null);

        EditText editText1 = (EditText) contentView.findViewById(R.id.ip1);
        EditText editText2 = (EditText) contentView.findViewById(R.id.ip2);
        EditText editText3 = (EditText) contentView.findViewById(R.id.ip3);
        EditText editText4 = (EditText) contentView.findViewById(R.id.ip4);
        editText1.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText2.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText3.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText4.setInputType(InputType.TYPE_CLASS_NUMBER);

        Button button = (Button) contentView.findViewById(R.id.bt1);
        contentView.setBackgroundColor(Color.WHITE);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //contentView.setBackgroundColor(Color.RED);
                MyApp myApp = (MyApp) mActivity.getApplication();
                EditText editText1 = (EditText) contentView.findViewById(R.id.ip1);
                EditText editText2 = (EditText) contentView.findViewById(R.id.ip2);
                EditText editText3 = (EditText) contentView.findViewById(R.id.ip3);
                EditText editText4 = (EditText) contentView.findViewById(R.id.ip4);


                String url = editText1.getText().toString() + "." +
                        editText2.getText().toString() + "." +
                        editText3.getText().toString() + "." +
                        editText4.getText().toString();
                if (!url.equals("...")) {
                    myApp.setUrl(url);
                }
                popupWindow.dismiss();
            }
        });



        popupWindow = new PopupWindow(contentView,
                1000, 400, true);
//        popupWindow.setFocusable(true);
//        popupWindow.setOutsideTouchable(false);
    }

    void show() {
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
    }
}
