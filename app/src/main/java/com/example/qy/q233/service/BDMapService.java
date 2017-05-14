package com.example.qy.q233.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by Xu Yining on 2017/5/6.
 */

public class BDMapService extends Service {
    private static final String TAG = "MyService";
    //查询到的城市信息
    private String city = "";
    //设置为静态成员方便在 Activity 中引用
    public static Handler handler;
    //Activity 与 Service 交互的通道
    private MyBinder binder = new MyBinder();
    //定位客户端
    private LocationClient client;

    public static double latitude, longitude;
    @Override
    public void onCreate() {
        super.onCreate();
        getLocation();
    }
    /**
     * 初始化定位选项
     */
    private void getLocation() {
        client = new LocationClient(getApplicationContext());
        //注册监听接口
        client.registerLocationListener(new MyLocationListener());
        LocationClientOption option = new LocationClientOption();
        //每 5 秒更新一次定位
        option.setScanSpan(1 * 1000);
        //开启描述性信息，不开启不会返回城市名等信息
        option.setIsNeedAddress(true);
        client.setLocOption(option);
        //开始定位
        client.start();
    }
    @Override
    public IBinder onBind(Intent intent) {
        //返回与 Activity 的交互通道
        return binder;
    }
    /**
     * 在代理中返回真正地城市名信息
     */
    public class MyBinder extends Binder {
        public String getCity() {
            return city;
        }
    }


    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null) {
                Toast.makeText(BDMapService.this, "定位失败", Toast.LENGTH_SHORT).show();
                return;
            }
            if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation
                    || bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                latitude = bdLocation.getLatitude();
                longitude = bdLocation.getLongitude();
                Log.v(TAG, "" + latitude);
                Log.v(TAG, "" + longitude);


            }
        }
        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //服务销毁停止定位
        client.stop();
    }

}
