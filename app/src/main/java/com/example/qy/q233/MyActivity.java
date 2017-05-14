package com.example.qy.q233;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.qy.q233.service.BDMapService;

/**
 * Created by Xu Yining on 2017/5/6.
 */

public class MyActivity extends AppCompatActivity {
    private BDMapService.MyBinder binder;
    private TextView textView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        textView = (TextView) findViewById(R.id.service_loc);
        location();
    }
    /**
     * 绑定定位服务
     */
    private void location() {
        Intent intent = new Intent(this, BDMapService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
        //传入本地的 Handler
        BDMapService.handler = handler;
    }
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //获取服务的代理对象
            binder = (BDMapService.MyBinder) service;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    /**
     * 接受到消息后将城市名显示出来
     */
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (binder != null) {
                textView.setText(binder.getCity());
            }
        }
    };
}
