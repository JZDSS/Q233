package com.example.qy.q233;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by Qi Yao on 17-3-23.
 */

public class SDKReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR))
        {
            // key 验证失败，相应处理
            Toast.makeText(context, "BDMap SDK Failed!", Toast.LENGTH_SHORT).show();

        }
    }
}
