package com.example.qy.q233;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by Xu Yining on 2017/4/1.
 */

public class AccelerometerActivity extends AppCompatActivity {

    private String fileName;
    private static final int UPDATE_BAR_AND_TEXTVIWE = 0;
    private static final int UPDATE_CHART = 1;
    private boolean sensorOn;
    private boolean exporting;
    private Accelerometer mAccelerometer;
    private FileManager mFileManager;
    private Timer barTimer = new Timer();
    private Timer chartTimer = new Timer();
    private String cache = "";
    private Handler mHandler = new MyHandler();
    private BarView xBarView, yBarView, zBarView;
    private boolean storageAllowed = true;
    LineChart mLinechart;
    DrawLinechart mDrawLineChart;
    public boolean isChart = false;
    public static float savedTime;
    ArrayList<Entry> yVals = new ArrayList<>();
    public Switch mSwitch;
    public MessageToServer messageToServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acceleration);
        messageToServer = new MessageToServer();

        if (SplashActivity.apiVersion >=23) {
            requestPermissions(Permission.allPermissions, 0);
        }
        mAccelerometer = new Accelerometer(this);

        xBarView = (BarView) findViewById(R.id.sv1);
        yBarView = (BarView) findViewById(R.id.sv2);
        zBarView = (BarView) findViewById(R.id.sv3);
        mDrawLineChart = new DrawLinechart();
        mLinechart = (LineChart) findViewById(R.id.linechart);
        yVals.add(new Entry(0,0));
        if (!isChart){
            mDrawLineChart.initChart(mLinechart, savedTime, yVals);
            isChart = true;
        }
        mFileManager = new FileManager(getApplicationContext());

        mSwitch = (Switch) findViewById(R.id.sensor_switch);
        mSwitch.toggle();
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (!isChecked){
//                    mSwitch.setText(getString(R.string.text_off));
//                }else {
//                    mSwitch.setText(getString(R.string.text_on));
//                }
            }
        });

        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        SDKReceiver mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);

        MyTimerTask barTimerTask = new MyTimerTask(mHandler);
        barTimerTask.setMsg(UPDATE_BAR_AND_TEXTVIWE);
        barTimer.schedule(barTimerTask, 1, 5);

        MyTimerTask chartTask = new MyTimerTask(mHandler);
        chartTask.setMsg(UPDATE_CHART);
        chartTimer.schedule(chartTask, 1, 125);
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onResume() {

        super.onResume();
        xBarView.closed = false;
        yBarView.closed = false;
        zBarView.closed = false;
        isChart = true;

        sensorOn = true;
        mAccelerometer.resume();
        messageToServer.mThread.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isChart = false;

        sensorOn = false;
        if(!Debug.ENABLE)
        {
            mAccelerometer.pause();
            messageToServer.mThread.close();
        }

    }

    @Override
    protected void onDestroy() {
        if (barTimer != null) {
            barTimer.cancel();
            barTimer = null;
        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case Permission.CODE_ACCESS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getApplicationContext(), "GET LOCATION PERMISSION DENIED!", Toast.LENGTH_LONG).show();
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED);
            case Permission.CODE_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getApplicationContext(), "WRITE/READ STORAGE PERMISSION DENIED!", Toast.LENGTH_LONG).show();
                    storageAllowed = false;
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    storageAllowed = true;
            default:
                break;
        }
    }

    public void read(View view){
        if (!storageAllowed && SplashActivity.apiVersion >=23){
            requestPermissions(new String[]{Permission.allPermissions[1]}, Permission.Codes[1]);
            return;
        }
//        fileName = ((EditText) findViewById(R.id.file_name)).getText().toString();
        try
        {
            String content = mFileManager.read(fileName);
            Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
        } catch(IOException e){
            e.printStackTrace();
            if (SplashActivity.apiVersion >=23){
                requestPermissions(new String[]{Permission.allPermissions[1]}, Permission.Codes[1]);
            }
        }


    }
    public void sensorControl(View view) {
        if (sensorOn) {
            onPause();
//            ((Button) findViewById(R.id.sensor_control)).setText(R.string.start);
            if (exporting && storageAllowed)
            {
                exporting = false;
                //mFileManager.save(cache);
                try{
                    mFileManager.save(fileName, cache, true);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (SplashActivity.apiVersion >=23){
                        requestPermissions(new String[]{Permission.allPermissions[1]}, Permission.Codes[1]);
                    }
                    return;
                }
                cache = "";
//                ((Button)findViewById(R.id.export)).setText(R.string.export);
            }
        } else {
            onResume();
//            ((Button) findViewById(R.id.sensor_control)).setText(R.string.stop);

        }
    }

    private void refresh(int id, String s) {
        TextView mTextView = (TextView) findViewById(id);
        mTextView.setText(s);
    }


    public void exportControl(View view) {
        if (!storageAllowed && SplashActivity.apiVersion >=23){
            requestPermissions(new String[]{Permission.allPermissions[1]}, Permission.Codes[1]);
            return;
        }
        if (exporting){
            //mFileManager.save(cache);
            try{
                mFileManager.save(fileName, cache, true);
            } catch (Exception e) {
                e.printStackTrace();
                if (SplashActivity.apiVersion >=23) {
                    requestPermissions(new String[]{Permission.allPermissions[1]}, Permission.Codes[1]);
                }
                return;
            }
            cache = "";
//            ((Button)findViewById(R.id.export)).setText(R.string.export);
            exporting = false;
        } else {

            if (!sensorOn){

                onResume();
//                ((Button) findViewById(R.id.sensor_control)).setText(R.string.stop);
                sensorOn = true;
            }

//            fileName = ((EditText) findViewById(R.id.file_name)).getText().toString();
            try{
                mFileManager.save(fileName, "", false);
            }catch (Exception e){
                e.printStackTrace();
                if (SplashActivity.apiVersion >=23) {
                    requestPermissions(new String[]{Permission.allPermissions[1]}, Permission.Codes[1]);
                }
                return;
            }
//            ((Button)findViewById(R.id.export)).setText(R.string.stop);
            exporting = true;
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


    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_BAR_AND_TEXTVIWE:
                    refresh(R.id.val_x, String.format("%.3f", mAccelerometer.x));
                    refresh(R.id.val_y, String.format("%.3f", mAccelerometer.y));
                    refresh(R.id.val_z, String.format("%.3f", mAccelerometer.z));
                    if (mAccelerometer.x - xBarView.value > 0.1) {
                        xBarView.value += 0.1;
                    }else if (mAccelerometer.x - xBarView.value < -0.1) {
                        xBarView.value -= 0.1;
                    }else {
                        xBarView.value = mAccelerometer.x;
                    }
                    if (mAccelerometer.y - yBarView.value > 0.1) {
                        yBarView.value += 0.1;
                    }else if (mAccelerometer.y - yBarView.value < -0.1) {
                        yBarView.value -= 0.1;
                    }else {
                        yBarView.value = mAccelerometer.y;
                    }
                    if (mAccelerometer.z - zBarView.value > 0.1) {
                        zBarView.value += 0.1;
                    }else if (mAccelerometer.z - zBarView.value < -0.1) {
                        zBarView.value -= 0.1;
                    }else {
                        zBarView.value = mAccelerometer.z;
                    }

                    messageToServer.post(System.currentTimeMillis(), mAccelerometer.x, mAccelerometer.y,
                            mAccelerometer.z, mAccelerometer.norm);

                    if (exporting && storageAllowed){
                        cache += System.currentTimeMillis() + "," + mAccelerometer.x + "," + mAccelerometer.y + "," +
                                mAccelerometer.z + "\n";
                        if (cache.length()>1024){
                            try {
                                mFileManager.save(fileName, cache, true);
                            }catch (Exception e){
                                e.printStackTrace();
                                if (SplashActivity.apiVersion >=23) {
                                    requestPermissions(new String[]{Permission.allPermissions[1]}, Permission.Codes[1]);
                                }
                                return;
                            }
                            cache = "";
                        }
                    }
                    break;
                case UPDATE_CHART:
                    if (isChart) {
                        //yVals.add(new Entry(savedTime, mAccelerometer.norm));
                        savedTime += 0.125;
                        mDrawLineChart.updateData(mLinechart, mAccelerometer.norm, yVals, savedTime);
                    }
                default:
                    break;
            }
        }
    }
}
