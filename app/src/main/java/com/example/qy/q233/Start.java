package com.example.qy.q233;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Qi Yao on 17-4-12.
 */

public class Start extends AppCompatActivity {

//    public static final View.OnTouchListener TouchDark = new View.OnTouchListener() {
//
//        public final float[] BT_SELECTED = new float[] {1,0,0,0,-50,0,1,0,0,-50,0,0,1,0,-50,0,0,0,1,0};
//        public final float[] BT_NOT_SELECTED = new float[] {1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0};
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                v.getBackground().setColorFilter(
//                        new ColorMatrixColorFilter(BT_SELECTED));
//                v.setBackgroundDrawable(v.getBackground());
//            } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                v.getBackground().setColorFilter(
//                        new ColorMatrixColorFilter(BT_NOT_SELECTED));
//                v.setBackgroundDrawable(v.getBackground());
//            }
//            return false;
//        }
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        Button login = (Button) findViewById(R.id.login_bt);
        Button signup = (Button) findViewById(R.id.signup_bt);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(Start.this, Login.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(Start.this, Signup.class);
                startActivity(intent);
            }
        });

        login.setOnTouchListener(new TouchDark());
        signup.setOnTouchListener(new TouchDark());
    }


}
