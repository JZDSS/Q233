package com.example.qy.q233;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by Xu Yining on 2017/3/27.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        boolean mFirst = isFirstEnter(SplashActivity.this,SplashActivity.this.getClass().getName(), SHAREDPREFERENCES_NAME, KEY_GUIDE_ACTIVITY);
        if(mFirst)
            mHandler.sendEmptyMessageDelayed(SWITCH_GUIDACTIVITY,3000);
        else
            mHandler.sendEmptyMessageDelayed(SWITCH_STARTACTIVITY,3000);

//        int apiVersion = android.os.Build.VERSION.SDK_INT;
//        ((MyApp) getApplication()).setApiVersion(apiVersion);
//
//        int SPLASH_DISPLAY_LENGHT = 1000;
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                Intent mainIntent = new Intent(SplashActivity.this,
//                        Start.class);
//                SplashActivity.this.startActivity(mainIntent);
//                SplashActivity.this.finish();
//            }
//        }, SPLASH_DISPLAY_LENGHT);

    }
        public static final String SHAREDPREFERENCES_NAME = "interface";
        public static final String KEY_GUIDE_ACTIVITY = "guide_activity";
        private boolean isFirstEnter(Context context, String className, String SHAREDPREFERENCES_NAME, String KEY_GUIDE_ACTIVITY){
                if(context==null || className==null||"".equalsIgnoreCase(className))
                    return false;
                String mResultStr = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE)
                        .getString(KEY_GUIDE_ACTIVITY, "");//取得所有类名 如 com.my.MainActivity
                if(mResultStr.equalsIgnoreCase("false"))
                    return false;
                else
                    return true;
            }

        private final static int SWITCH_STARTACTIVITY = 1000;
        private final static int SWITCH_GUIDACTIVITY = 1001;
        public Handler mHandler = new Handler(){
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case SWITCH_STARTACTIVITY:
                        Intent mIntent = new Intent();
                        mIntent.setClass(SplashActivity.this, Start.class);
                        SplashActivity.this.startActivity(mIntent);
                        SplashActivity.this.finish();
                        break;
                    case SWITCH_GUIDACTIVITY:
                        mIntent = new Intent();
                        mIntent.setClass(SplashActivity.this, GuideActivity.class);
                        SplashActivity.this.startActivity(mIntent);
                        SplashActivity.this.finish();
                        break;
                }
                super.handleMessage(msg);
            }
        };

}
