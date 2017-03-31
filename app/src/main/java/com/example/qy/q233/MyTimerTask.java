package com.example.qy.q233;

import android.os.Handler;
import android.os.Message;

import java.util.TimerTask;

/**
 * Created by Qi Yao on 17-3-31.
 */

public class MyTimerTask extends TimerTask {
    Handler mHandler;
    MyTimerTask (Handler mHandler)
    {
        this.mHandler = mHandler;
    }
    public void sendMessage(Handler mHandler, int id) {
        if (mHandler != null) {
            Message message = Message.obtain(mHandler, id);
            mHandler.sendMessage(message);
        }
    }
    @Override
    public void run() {
        sendMessage(mHandler, 0);
    }
}
