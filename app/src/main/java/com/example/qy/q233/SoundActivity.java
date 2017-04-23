package com.example.qy.q233;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by Zhang Xiaorui on 2017/4/14.
 */

public class SoundActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int START_RECORD = 0;

    private Button mBtnStart, mBtnStop;

    private File mFileRec;
    private Thread mThread;
    private MediaRecorder mMediaRecorder;
    private MyHandle mHandle;

    public boolean isRecording = false;
    private boolean isListener = false;
    private boolean isThreading = true;

    private float volume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound);
        mHandle = new MyHandle();
        initView();
    }

    private void initView() {
        mBtnStart = (Button) findViewById(R.id.btn_start);
        mBtnStop = (Button) findViewById(R.id.btn_stop);

        mBtnStart.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                isListener = true;
                isRecording = true;
                isThreading = true;
                Message msg = new Message();
                msg.what = START_RECORD;
                mHandle.sendMessage(msg);
                break;
            case R.id.btn_stop:
                isListener = false;
                isThreading = false;
                isRecording = false;
//                SoundCalculator.dblast = 0;
//                SoundCalculator.dbstart = 0;
                mMediaRecorder.reset();
                mFileRec.delete();
//                SoundCalculator.dbstart = 0;
                break;

        }
    }

    private void beginstart() {
        isListener = true;
        mFileRec = SoundFileUtil.createFile("test.amr");

    }


    private void startRecord(File mFileRec) {
        try {
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setOutputFile(mFileRec.getAbsolutePath());
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            isRecording = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                SoundCalculator.dbstart = 0;
                while (isThreading) {
                    try {
                        if (isListener) {
                            volume = mMediaRecorder.getMaxAmplitude();
                            if (volume > 0 && volume < 1000000) {
                                SoundCalculator.setDbCount(20 * (float) (Math.log10(volume)));
                            }
                        }
                        Log.v("activity", "db = " + SoundCalculator.dbstart);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        isListener = false;
                    }
                }
                while(SoundCalculator.dbstart > 0) {
                    try {
                        SoundCalculator.setDbCount(0);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mThread.start();


    }


    class MyHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case START_RECORD:
                    beginstart();
                    if (mFileRec != null) {

                        startRecord(mFileRec);
                    } else {
                        Toast.makeText(getApplicationContext(), "创建文件失败", Toast.LENGTH_LONG).show();
                    }

                    break;
            }

            super.handleMessage(msg);
        }
    }

}