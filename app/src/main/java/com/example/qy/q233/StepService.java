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
    public float norm = 9.8f;

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(" StepService", "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(" StepService", "onCreate");

        accelerometer = new Accelerometer(this);
        accelerometer.resume();

        Flag = true;
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    if (Flag) {
                        norm = accelerometer.norm;
                        counter.RefreshNorm(accelerometer.norm);
                        try {
                            Thread.sleep(300);
                            counter.Counter();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

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
        Log.i(" StepService","onDestroy");
    }
}
