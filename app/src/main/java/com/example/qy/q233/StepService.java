package com.example.qy.q233;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.qy.q233.lib.Accelerometer;
import com.example.qy.q233.lib.Counter;

/**
 * Created by Xu Yining on 2017/5/2.
 */

public class StepService extends Service {
    private static boolean Flag = false;
    public Accelerometer accelerometer;
    public Counter counter = new Counter();
    public int StepCounter = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(" StepCounterService", "onCreate");
        accelerometer = new Accelerometer(this);
        Flag = true;
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    if (Flag) {
                        counter.RefreshNorm(accelerometer.norm);
                        counter.Counter();
                    }
                }
            }
        }.start();
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Flag = false;
        Log.i(" StepCounterService","onDestroy");
    }
}
