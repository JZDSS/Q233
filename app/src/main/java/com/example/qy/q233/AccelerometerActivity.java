package com.example.qy.q233;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;

import java.io.IOException;
import java.util.Timer;

/**
 * Created by Xu Yining on 2017/4/1.
 */

public class AccelerometerActivity extends AppCompatActivity {
    private static final int UPDATE_TEXTVIWE = 0;
    private boolean sensorOn;
    private boolean exporting;
    private Accelerometer mAccelerometer;
    FileManager mFileManager;
    private Timer mTimer = new Timer();
    String cache = "";
    private Handler mHandler = new MyHandler();
    SDKReceiver mReceiver;
    BarView xBarView, yBarView, zBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acceleration);

        mAccelerometer = new Accelerometer(this);

        xBarView = (BarView) findViewById(R.id.sv1);
        yBarView = (BarView) findViewById(R.id.sv2);
        zBarView = (BarView) findViewById(R.id.sv3);

        //mFileManager = new FileManager(this);
        mFileManager = new FileManager(getApplicationContext());

        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);

        MyTimerTask mTimerTask = new MyTimerTask(mHandler);
        mTimerTask.setMsg(UPDATE_TEXTVIWE);
        mTimer.schedule(mTimerTask, 1, 5);

    }

    @Override
    protected void onResume() {


        xBarView.closed = false;


        yBarView.closed = false;

        zBarView.closed = false;
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

    public void read(View view){
        //mFileManager.setFileName("a.txt");
        //String content = mFileManager.read();
        try
        {
            String content = mFileManager.read("a.txt");
            Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
        }
        catch(IOException e){e.printStackTrace();}



    }
    public void sensorControl(View view) {
        if (sensorOn) {
            onPause();
            if (exporting)
            {
                exporting = false;
                ((Button)findViewById(R.id.export)).setText(R.string.export);
                //mFileManager.save(cache);
                try{
                    mFileManager.save("a.txt", cache, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                cache = "";
            }
        } else {
            onResume();
        }
    }

    private void refresh(int id, String s) {
        TextView mTextView = (TextView) findViewById(id);
        mTextView.setText(s);
    }

//    public void sendMessage(int id) {
//        if (mHandler != null) {
//            Message message = Message.obtain(mHandler, id);
//            mHandler.sendMessage(message);
//        }
//    }

    public void exportControl(View view) {
        if (exporting){

            exporting = false;
            ((Button)findViewById(R.id.export)).setText(R.string.export);
            //mFileManager.save(cache);
            try{
                mFileManager.save("a.txt", cache, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            cache = "";

        }else{
            exporting = true;
            if (!sensorOn){
                sensorOn = true;
                onResume();
            }
            ((Button)findViewById(R.id.export)).setText(R.string.stop);
            String fileName = "a.txt";
            try{
                mFileManager.save(fileName, "", false);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public void jump(View view){
        xBarView.closed = true;
        yBarView.closed = true;
        zBarView.closed = true;

        Intent intent=new Intent();
        //setClass函数的第一个参数是一个Context对象
        //Context是一个类,Activity是Context类的子类,也就是说,所有的Activity对象都可以向上转型为Context对象
        //setClass函数的第二个参数是Class对象,在当前场景下,应该传入需要被启动的Activity的class对象
        intent.setClass(AccelerometerActivity.this, BDMapActivity.class);
        startActivity(intent);
    }

//    public void jump2(View view){
//        Intent intent=new Intent();
//        //setClass函数的第一个参数是一个Context对象
//        //Context是一个类,Activity是Context类的子类,也就是说,所有的Activity对象都可以向上转型为Context对象
//        //setClass函数的第二个参数是Class对象,在当前场景下,应该传入需要被启动的Activity的class对象
//        intent.setClass(MainActivity.this, AccelerometerActivity.class);
//        startActivity(intent);
//    }
//    private class MyTimerTask extends TimerTask {
//        @Override
//        public void run() {
//            sendMessage(UPDATE_TEXTVIWE);
//        }
//    }


    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXTVIWE:
                    refresh(R.id.val_x, String.format("%.3f", mAccelerometer.x));
                    refresh(R.id.val_y, String.format("%.3f", mAccelerometer.y));
                    refresh(R.id.val_z, String.format("%.3f", mAccelerometer.z));
                    if (mAccelerometer.x - xBarView.value > 0.1)
                    {
                        xBarView.value += 0.1;
                    }else if (mAccelerometer.x - xBarView.value < -0.1)
                    {
                        xBarView.value -= 0.1;
                    }else {
                        xBarView.value = mAccelerometer.x;
                    }
                    if (mAccelerometer.y - yBarView.value > 0.1)
                    {
                        yBarView.value += 0.1;
                    }else if (mAccelerometer.y - yBarView.value < -0.1)
                    {
                        yBarView.value -= 0.1;
                    }else {
                        yBarView.value = mAccelerometer.y;
                    }
                    if (mAccelerometer.z - zBarView.value > 0.1)
                    {
                        zBarView.value += 0.1;
                    }else if (mAccelerometer.z - zBarView.value < -0.1)
                    {
                        zBarView.value -= 0.1;
                    }else {
                        zBarView.value = mAccelerometer.z;
                    }

                    //refresh(R.id.val_norm, String.valueOf(mAccelerometer.norm));
                    if (exporting){
                        cache += mAccelerometer.x + "," + mAccelerometer.y + "," +
                                mAccelerometer.z + ";";
                        if (cache.length()>1024){
                            try {
                                mFileManager.save("a.txt", cache, true);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
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
