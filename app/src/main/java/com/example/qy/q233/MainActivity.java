package com.example.qy.q233;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Qi Yao on 17-3-15.
 */

public class MainActivity extends AppCompatActivity {

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.button_jump1);
        mButton.setOnClickListener(new ButtonListener());
    }

    class ButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            //setClass函数的第一个参数是一个Context对象
            //Context是一个类,Activity是Context类的子类,也就是说,所有的Activity对象都可以向上转型为Context对象
            //setClass函数的第二个参数是Class对象,在当前场景下,应该传入需要被启动的Activity的class对象
            intent.setClass(MainActivity.this, AccelerometerActivity.class);
            startActivity(intent);
        }
    }
}
