package com.example.qy.q233;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.example.qy.q233.lib.Accelerometer;
import com.example.qy.q233.lib.Counter;

import java.util.Timer;

/**
 * Created by Xu Yining on 2017/4/22.
 */

public class CounterActivity extends AppCompatActivity {
    public TextView textView;
    public boolean isRun = false;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            textView.setText(String.valueOf(Counter.couter).toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        textView = (TextView) findViewById(R.id.textview);

        isRun = true;
        startService(new Intent(this, StepService.class));

        new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (isRun) {
                        handler.sendEmptyMessage(199);
                    }
                }
            }
        }.start();

    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        isCounter = false;
//        accelerometer.pause();
//    }
}
