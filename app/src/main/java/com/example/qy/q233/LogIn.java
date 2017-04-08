package com.example.qy.q233;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by Qi Yao on 17-4-8.
 */

public class LogIn extends AppCompatActivity {
    Post post;
    MyHandler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        String url = "http://192.168.1.104/q233/login.php";
        post = new Post(url, mHandler);
    }



    public void logIn(View view) {
        String userName = ((EditText) findViewById(R.id.user_name)).getText().toString();
        String passwd = ((EditText) findViewById(R.id.pass_word)).getText().toString();
        ArrayList<NameValuePair> pairs = new ArrayList<>();
        NameValuePair pair0= new BasicNameValuePair("u", userName);
        NameValuePair pair1= new BasicNameValuePair("p", passwd);
        pairs.add(pair0);
        pairs.add(pair1);
        post.post(pairs);
    }


    class MyHandler extends Handler {
        void jump(){
            Intent intent=new Intent();
            //setClass函数的第一个参数是一个Context对象
            //Context是一个类,Activity是Context类的子类,也就是说,所有的Activity对象都可以向上转型为Context对象
            //setClass函数的第二个参数是Class对象,在当前场景下,应该传入需要被启动的Activity的class对象
            intent.setClass(LogIn.this, MainActivity.class);
            startActivity(intent);
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 3:
                    jump();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), "帐号或密码错误", Toast.LENGTH_SHORT).show();
                default:
                    break;
            }
        }
    }
}
