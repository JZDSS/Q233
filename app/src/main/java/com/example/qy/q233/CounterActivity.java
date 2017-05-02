package com.example.qy.q233;

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
    public Accelerometer accelerometer;
    public boolean isopened;
    private MyHandler mHandler = new MyHandler();
    private Timer counterTimer = new Timer();
    public Counter mCounter = new Counter();
    public float new_norm = 9.8f;
    public float old_norm = 9.8f;
    public float norm_r = 9.8f;
    public TextView textView;
    public int StepCounter = 0;
    public Button button;
    public boolean isCounter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        textView = (TextView) findViewById(R.id.textview);
        button = (Button) findViewById(R.id.button1);
        isCounter = true;

        accelerometer = new Accelerometer(this);
        accelerometer.resume();

        MyTimerTask counterTask = new MyTimerTask(mHandler);
        counterTask.setMsg(1);
        counterTimer.schedule(counterTask, 1, 500);

    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if(isCounter) {
                        new_norm = accelerometer.norm;
                        mCounter.DetectorPeak(new_norm, old_norm);
                        mCounter.Counter();
                        StepCounter = mCounter.couter;
                        if ((new_norm != old_norm) && (new_norm != norm_r)) {
                            norm_r = old_norm;
                            mCounter.norm_r = old_norm;
                            old_norm = accelerometer.norm;
                        }
                        textView.setText(String.valueOf(StepCounter).toString());
                    }
                default:
                    break;
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isCounter = false;
        accelerometer.pause();
    }
}
