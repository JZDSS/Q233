package com.example.qy.q233;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.example.qy.q233.lib.ContextMenuDialogFragment;
import com.example.qy.q233.lib.MenuObject;
import com.example.qy.q233.lib.MenuParams;
import com.example.qy.q233.lib.interfaces.OnMenuItemClickListener;
import com.example.qy.q233.lib.interfaces.OnMenuItemLongClickListener;
import com.example.qy.q233.service.Accelerometer;
import com.example.qy.q233.service.Save;
import com.example.qy.q233.view.BarView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

//import android.icu.text.SimpleDateFormat;

/**
 * Created by Xu Yining on 2017/4/1.
 */

public class AccelerometerActivity extends AppCompatActivity implements OnMenuItemClickListener, OnMenuItemLongClickListener {
    public FragmentManager fragmentManager;
    public ContextMenuDialogFragment mMenuDialogFragment;

    private static final int UPDATE_BAR_AND_TEXTVIWE = 0;
    private static final int UPDATE_CHART = 1;
    private boolean sensorOn;
    private boolean exporting;
    private Timer barTimer = new Timer();
    private Timer chartTimer = new Timer();
    private String cache = "";
    private Handler mHandler = new MyHandler();
    private BarView xBarView, yBarView, zBarView;
    private boolean storageAllowed = true;
    private String fileName;
    private FileManager mFileManager;
    LineChart mLinechart;
    DrawLinechart mDrawLineChart;
    public boolean isChart = false;
    public static float savedTime;
    ArrayList<Entry> yVals = new ArrayList<>();
    public Switch mSwitch;
    public MessageToServer messageToServer;
    String user;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acceleration);
        user = ((MyApp)getApplication()).getUsername();
        messageToServer = new MessageToServer(((MyApp)getApplication()).getUrl());

        mFileManager = new FileManager(getApplicationContext());
        fileName = "1.q233";

        xBarView = (BarView) findViewById(R.id.sv1);
        yBarView = (BarView) findViewById(R.id.sv2);
        zBarView = (BarView) findViewById(R.id.sv3);
        mDrawLineChart = new DrawLinechart();
        mLinechart = (LineChart) findViewById(R.id.linechart);
        yVals.add(new Entry(0,0));
        if (!isChart){
            mDrawLineChart.initChart(mLinechart, yVals);
            isChart = true;
        }

        mSwitch = (Switch) findViewById(R.id.sensor_switch);
        mSwitch.toggle();

        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        SDKReceiver mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);
        fragmentManager = getSupportFragmentManager();
        initToolbar();
        initMenuFragment();

    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mToolbar.setNavigationIcon(R.mipmap.btn_back);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
        mMenuDialogFragment.setItemLongClickListener(this);
    }

    private List<MenuObject> getMenuObjects() {

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.mipmap.delet_icon);

        MenuObject send = new MenuObject("Start");
        send.setResource(R.mipmap.add_icon);

        MenuObject like = new MenuObject("Stop");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.storage_icon);
        like.setBitmap(b);

        MenuObject addFr = new MenuObject("Read");
        BitmapDrawable bd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.mipmap.read_icon));
        addFr.setDrawable(bd);

        MenuObject addFav = new MenuObject("Clear");
        addFav.setResource(R.mipmap.icn_4);
//
//        MenuObject block = new MenuObject("Block user");
//        block.setResource(R.mipmap.icn_5);

        menuObjects.add(close);
        menuObjects.add(send);
        menuObjects.add(like);
        menuObjects.add(addFr);
        menuObjects.add(addFav);
//        menuObjects.add(block);
        return menuObjects;
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

        MyTimerTask barTimerTask = new MyTimerTask(mHandler);
        barTimerTask.setMsg(UPDATE_BAR_AND_TEXTVIWE);
        barTimer = new Timer();
        barTimer.schedule(barTimerTask, 1, 5);

        MyTimerTask chartTask = new MyTimerTask(mHandler);
        chartTask.setMsg(UPDATE_CHART);
        chartTimer = new Timer();
        chartTimer.schedule(chartTask, 1, 125);
        //mAccelerometer.resume();

        messageToServer.mThread.open();

        mLinechart.getLineData().clearValues();
        savedTime = 0;
        mDrawLineChart.initChart(mLinechart, yVals);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isChart = false;
        sensorOn = false;
//        if(!Debug.ENABLE)
//        {
        barTimer.cancel();
        chartTimer.cancel();
        //mAccelerometer.pause();
        messageToServer.mThread.close();
//        }

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

//    public void read(View view){
//        if (!storageAllowed && SplashActivity.apiVersion >=23){
//            requestPermissions(new String[]{Permission.allPermissions[1]}, Permission.Codes[1]);
//            return;
//        }
////        fileName = ((EditText) findViewById(R.id.file_name)).getText().toString();
//        try
//        {
//            String content = mFileManager.read(fileName);
//            Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
//        } catch(IOException e){
//            e.printStackTrace();
//            if (SplashActivity.apiVersion >=23){
//                requestPermissions(new String[]{Permission.allPermissions[1]}, Permission.Codes[1]);
//            }
//        }
//    }


    public void sensorControl(View view) {
        if (sensorOn) {
            onPause();
//            ((Button) findViewById(R.id.sensor_control)).setText(R.string.start);
            if (exporting && storageAllowed)
            {
                exporting = false;
                //mFileManager.save(cache);
                try{
                    //mFileManager.save(fileName, cache, true);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (((MyApp) getApplication()).getApiVersion() >= 23){
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

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mMenuDialogFragment != null && mMenuDialogFragment.isAdded()) {
            mMenuDialogFragment.dismiss();
        } else {
            finish();
        }
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        switch (position){
            case 1:
                Save.perm = true;
                startService(new Intent(this, Save.class));
                break;
            case 2:
                Save.perm =false;
                stopService(new Intent(this, Save.class));
                break;
            case 3:
                String content = "";
                try
                {
                    content = mFileManager.read(fileName);
                    //Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
                } catch(IOException e){
                    e.printStackTrace();
                    if (((MyApp)getApplication()).apiVersion >=23){
                        requestPermissions(new String[]{Permission.allPermissions[Permission.CODE_WRITE_EXTERNAL_STORAGE]}, Permission.Codes[Permission.CODE_WRITE_EXTERNAL_STORAGE]);
                    }
                }
                if(((MyApp)getApplication()).apiVersion>23) {
                    FileContent popupWindow = new FileContent(this, content);
                    popupWindow.show();
                }else {
                    Toast.makeText(this, content, Toast.LENGTH_LONG).show();
                }
                break;
            case 4:
                try{
                    mFileManager.save(fileName, "", false);
                }catch (Exception e){
                    e.printStackTrace();
                    if (((MyApp)getApplication()).apiVersion >=23) {
                        requestPermissions(new String[]{Permission.allPermissions[1]}, Permission.Codes[1]);
                    }
                }
                break;
            default:
                break;
        }
        //Toast.makeText(this, "Clicked on position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {
        Toast.makeText(this, "Long clicked on position: " + position, Toast.LENGTH_SHORT).show();
    }

//    public void exportControl(View view) {
//        if (!storageAllowed && SplashActivity.apiVersion >=23){
//            requestPermissions(new String[]{Permission.allPermissions[1]}, Permission.Codes[1]);
//            return;
//        }
//        if (exporting){
//            //mFileManager.save(cache);
//            try{
//                mFileManager.save(fileName, cache, true);
//            } catch (Exception e) {
//                e.printStackTrace();
//                if (SplashActivity.apiVersion >=23) {
//                    requestPermissions(new String[]{Permission.allPermissions[1]}, Permission.Codes[1]);
//                }
//                return;
//            }
//            cache = "";
////            ((Button)findViewById(R.id.export)).setText(R.string.export);
//            exporting = false;
//        } else {
//
//            if (!sensorOn){
//
//                onResume();
////                ((Button) findViewById(R.id.sensor_control)).setText(R.string.stop);
//                sensorOn = true;
//            }
//
////            fileName = ((EditText) findViewById(R.id.file_name)).getText().toString();
//            try{
//                mFileManager.save(fileName, "", false);
//            }catch (Exception e){
//                e.printStackTrace();
//                if (SplashActivity.apiVersion >=23) {
//                    requestPermissions(new String[]{Permission.allPermissions[1]}, Permission.Codes[1]);
//                }
//                return;
//            }
////            ((Button)findViewById(R.id.export)).setText(R.string.stop);
//            exporting = true;
//        }
//    }

//    public void jump(View view){
//        xBarView.closed = true;
//        yBarView.closed = true;
//        zBarView.closed = true;
//
//        Intent intent=new Intent();
//        //setClass函数的第一个参数是一个Context对象
//        //Context是一个类,Activity是Context类的子类,也就是说,所有的Activity对象都可以向上转型为Context对象
//        //setClass函数的第二个参数是Class对象,在当前场景下,应该传入需要被启动的Activity的class对象
//        intent.setClass(AccelerometerActivity.this, BDMapActivity.class);
//        startActivity(intent);
//    }


    class MyHandler extends Handler {

        private void refresh(int id, String s) {
            TextView mTextView = (TextView) findViewById(id);
            mTextView.setText(s);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_BAR_AND_TEXTVIWE:
                    refresh(R.id.val_x, String.format("%.3f", Accelerometer.x));
                    refresh(R.id.val_y, String.format("%.3f", Accelerometer.y));
                    refresh(R.id.val_z, String.format("%.3f", Accelerometer.z));
                    if (Accelerometer.x - xBarView.value > 0.1) {
                        xBarView.value += 0.1;
                    }else if (Accelerometer.x - xBarView.value < -0.1) {
                        xBarView.value -= 0.1;
                    }else {
                        xBarView.value = Accelerometer.x;
                    }
                    if (Accelerometer.y - yBarView.value > 0.1) {
                        yBarView.value += 0.1;
                    }else if (Accelerometer.y - yBarView.value < -0.1) {
                        yBarView.value -= 0.1;
                    }else {
                        yBarView.value = Accelerometer.y;
                    }
                    if (Accelerometer.z - zBarView.value > 0.1) {
                        zBarView.value += 0.1;
                    }else if (Accelerometer.z - zBarView.value < -0.1) {
                        zBarView.value -= 0.1;
                    }else {
                        zBarView.value = Accelerometer.z;
                    }

                    messageToServer.post(formatter.format(new Date(System.currentTimeMillis())), Accelerometer.x, Accelerometer.y,
                            Accelerometer.z, user);


                    break;
                case UPDATE_CHART:
                    if (isChart) {
                        mDrawLineChart.updateData(mLinechart, Accelerometer.norm, savedTime);
                        savedTime += 0.125;
                    }
                default:
                    break;
            }
        }
    }
}
