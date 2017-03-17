package com.example.qy.q233;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Qi Yao on 17-3-15.
 */

public class MainActivity extends AppCompatActivity {

    private static final int UPDATE_TEXTVIWE = 0;

    private boolean sensorOn;
    private boolean exporting;
    private Accelerometer mAccelerometer;
    FileManager mFileManager;
    private Timer mTimer = new Timer();
    String cache = "";
    private Handler mHandler = new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAccelerometer = new Accelerometer(this);

        mFileManager = new FileManager(this);

        TimerTask mTimerTask = new MyTimerTask();
        mTimer.schedule(mTimerTask, 1, 5);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((Button) findViewById(R.id.sensor_control)).setText(R.string.stop);
        sensorOn = true;
        mAccelerometer.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((Button) findViewById(R.id.sensor_control)).setText(R.string.start);
        sensorOn = false;
        mAccelerometer.pause();
    }

    @Override
    protected void onDestroy() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        super.onDestroy();
    }

    public void sensorControl(View view) {
        if (sensorOn) {
            onPause();
        } else {
            onResume();
        }
    }

    private void refresh(int id, String s) {
        TextView mTextView = (TextView) findViewById(id);
        mTextView.setText(s);
    }

    public void sendMessage(int id) {
        if (mHandler != null) {
            Message message = Message.obtain(mHandler, id);
            mHandler.sendMessage(message);
        }
    }

    public void exportControl(View view) {
        if (exporting){

            exporting = false;
            ((Button)findViewById(R.id.export)).setText(R.string.export);
            mFileManager.save(cache, R.string.SD_card, true);
            cache = "";

        }else{
            exporting = true;
            if (!sensorOn){
                sensorOn = true;
                onResume();
            }
            ((Button)findViewById(R.id.export)).setText(R.string.stop);
            String fileName = "a.txt";
            mFileManager.setFileName(fileName);

        }
    }

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            sendMessage(UPDATE_TEXTVIWE);
        }
    }


    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXTVIWE:
                    refresh(R.id.val_x, String.valueOf(mAccelerometer.x));
                    refresh(R.id.val_y, String.valueOf(mAccelerometer.y));
                    refresh(R.id.val_z, String.valueOf(mAccelerometer.z));
                    refresh(R.id.val_norm, String.valueOf(mAccelerometer.norm));
                    if (exporting){
                        cache += mAccelerometer.x + "," + mAccelerometer.y + "," +
                                mAccelerometer.z + ";";
                        if (cache.length()>1024){
                            mFileManager.save(cache,R.string.SD_card,true);
                            cache = "";
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
