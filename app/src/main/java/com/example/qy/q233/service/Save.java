package com.example.qy.q233.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.example.qy.q233.FileManager;
import com.example.qy.q233.MyTimerTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

/**
 * Created by Qi Yao on 17-5-17.
 */

public class Save extends Service {
    static final int SAVE = 0;
    private String fileName;
    private FileManager mFileManager;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss.SSS");
    private String cache = "";
    private Timer mTimer;
    MyHandler mHandler;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFileManager = new FileManager(getApplicationContext());
        fileName = "1";
        mHandler = new MyHandler();
        MyTimerTask mTimerTask = new MyTimerTask(mHandler);
        mTimerTask.setMsg(SAVE);
        mTimer = new Timer();
        mTimer.schedule(mTimerTask, 1, 5);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        try {
            mFileManager.save(fileName, cache, true);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        cache = "";
    }

    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SAVE:
                    cache += formatter.format(new Date(System.currentTimeMillis())) + "," + Accelerometer.x + "," + Accelerometer.y + "," +
                            Accelerometer.z + "," + BDMapService.latitude + "," + BDMapService.longitude + "\n";
                    if (cache.length() > 1024) {
                        try {
                            mFileManager.save(fileName, cache, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                        cache = "";
                    }
                    break;
                default:
                    break;
            }
        }
    }
}

