package com.example.qy.q233;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Created by Qi Yao on 17-3-15.
 */

public class MainActivity extends AppCompatActivity {

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.button_jump1);
        mButton.setOnClickListener(new ButtonListener1());
        mButton = (Button) findViewById(R.id.button_jump2);
        mButton.setOnClickListener(new ButtonListener2());
        mButton = (Button) findViewById(R.id.button_jump3);
        mButton.setOnClickListener(new ButtonListener3());
    }

    private class ButtonListener1 implements OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            //setClass函数的第一个参数是一个Context对象
            //Context是一个类,Activity是Context类的子类,也就是说,所有的Activity对象都可以向上转型为Context对象
            //setClass函数的第二个参数是Class对象,在当前场景下,应该传入需要被启动的Activity的class对象
            intent.setClass(MainActivity.this, AccelerometerActivity.class);
            startActivity(intent);
        }
    }

    private class ButtonListener2 implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.setClass(MainActivity.this, BDMapActivity.class);
            startActivity(intent);
        }
    }

    class ButtonListener3 implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.setClass(MainActivity.this, AboutUsActivity.class);
            startActivity(intent);
        }
    }
}
