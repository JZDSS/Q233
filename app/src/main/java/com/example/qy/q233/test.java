package com.example.qy.q233;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.qy.q233.service.BDMapService;

/**
 * Created by Qi Yao on 17-5-14.
 */

public class test extends AppCompatActivity {

    TextView textView1,textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        startService(new Intent(this, BDMapService.class));
    }
}
