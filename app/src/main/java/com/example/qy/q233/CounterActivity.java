package com.example.qy.q233;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.qy.q233.lib.Counter;
import com.example.qy.q233.view.ArcProgress;
//import com.example.qy.q233.lib.OnTextCenter;

import java.util.Timer;

/**
 * Created by Xu Yining on 2017/4/22.
 */

public class CounterActivity extends AppCompatActivity {
    public TextView textView;
    public boolean isRun = false;
    public Button button;
    public ArcProgress mProgress;
    private Timer BarTimer = new Timer();
    private Timer CountTimer = new Timer();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
            textView.setText(String.valueOf(Counter.couter).toString());
            mProgress.setProgress(Counter.couter);

//                default:
//                    ArcProgress progressBar = (ArcProgress) msg.obj;
//                    progressBar.setProgress(msg.what);
//            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        textView = (TextView) findViewById(R.id.textview);
        button = (Button) findViewById(R.id.button1);

        mProgress = (ArcProgress) findViewById(R.id.myprogress);
        mProgress.setUnfinishedStrokeColor(Color.rgb(66, 145, 241));
        mProgress.setFinishedStrokeColor(Color.rgb(0, 0, 241));
//        mProgress.setOnCenterDraw(new OnTextCenter());
//        addProrgress(mProgress);

        isRun = true;
        button.setText("stop");
        startService(new Intent(this, StepService.class));



        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (button.getText()=="start"){
                    button.setText("stop");
                    isRun = true;
                }else {
                    button.setText("start");
                    isRun = false;
                }
            }
        });

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
//                        MyTimerTask CountTimerTask = new MyTimerTask(handler);
//                        CountTimerTask.setMsg(0);
//                        CountTimer.schedule(CountTimerTask, 1, 300);
                        handler.sendEmptyMessage(199);
                    }
                }
            }
        }.start();
    }

//    public void addProrgress(ArcProgress progressBar) {
//        Thread thread = new Thread(new ProgressThread(progressBar));
//        thread.start();
//    }
//
//    class ProgressThread implements Runnable{
//        int i= 0;
//        private ArcProgress progressBar;
//        public ProgressThread(ArcProgress progressBar) {
//            this.progressBar = progressBar;
//        }
//        @Override
//        public void run() {
//            for(;i<=100;i++){
//                if(isFinishing()){
//                    break;
//                }
//                Message msg = new Message();
//                msg.what = i;
//                Log.e("DEMO","i == "+i);
//                msg.obj = progressBar;
//                SystemClock.sleep(100);
//                MyTimerTask BarTimerTask = new MyTimerTask(handler);
//                BarTimerTask.setMsg(msg.what);
//                BarTimer.schedule(BarTimerTask, 1, 5);
//            }
//        }
//    }

    @Override
    protected void onResume(){
        super.onResume();
        isRun = true;
    }

    @Override
    protected void onPause(){
        super.onPause();
        isRun = false;
    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        isCounter = false;
//        accelerometer.pause();
//    }

}
