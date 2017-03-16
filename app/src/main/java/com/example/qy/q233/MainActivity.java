package com.example.qy.q233;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private boolean sensorOn;
    private Accelerometer mAccelerometer;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAccelerometer = new Accelerometer(this);
        mTimer = new Timer();
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                String x = String.valueOf(mAccelerometer.x);
                refresh(R.id.val_x, x);
            }
        };
        mTimer.schedule(mTimerTask,1000,5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((Button) findViewById(R.id.control)).setText(R.string.stop);
        sensorOn = true;
        mAccelerometer.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((Button) findViewById(R.id.control)).setText(R.string.start);
        sensorOn = false;
        mAccelerometer.pause();
    }

    @Override
    protected void onDestroy () {
        if (mTimer != null) {
            mTimer.cancel( );
            mTimer = null;
        }
        super.onDestroy();
    }

    public void clickButton(View view){
        if(sensorOn){
            onPause();
        }
        else {
            onResume();
        }
    }

    private void refresh(int id, String s){
        TextView mTextView = (TextView) findViewById(id);
        mTextView.setText(s);
    }
//    private float norm(float x, float y, float z){
//        return (float)Math.sqrt(x * x + y * y + z * z);
//    }

}