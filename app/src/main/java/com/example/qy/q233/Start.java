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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Button login = (Button) findViewById(R.id.start_bt_login);
        Button signup = (Button) findViewById(R.id.start_bt_signup);
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
