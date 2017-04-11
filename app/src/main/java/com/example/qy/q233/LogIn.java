package com.example.qy.q233;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Qi Yao on 17-4-8.
 */

public class LogIn extends AppCompatActivity {

    static final int SUCCEED = 0;
    static final int FAILEDD = 1;
    // mpostHelper;
    MyHandler mHandler;
    HashMap<String, Integer> map;
    AskForServerIP popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        mHandler = new MyHandler();

        map = new HashMap<>();
        map.put("p", SUCCEED);
        map.put("d", FAILEDD);

        ((EditText) findViewById(R.id.pass_word)).setTransformationMethod(PasswordTransformationMethod.getInstance());

        popupWindow = new AskForServerIP(this);

//        new AlertDialog.Builder(this).setTitle(R.string.ip).setView(R.layout.server_ip).setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                MyApp myApp = (MyApp) getApplication();
//                String url =  ((EditText) findViewById(R.id.ip)).getText().toString();//"http://192.168.1.104/q233/login.php";
//                url = "http://" + url;
//                myApp.setUrl(url);
//            }
//        }).show();
    }

//    private void showPopWindow(){
//        View contentView = this.getLayoutInflater().inflate(R.layout.server_ip, null);
//        Button button = (Button) contentView.findViewById(R.id.bt1);
//        contentView.setBackgroundColor(Color.RED);
////        button.setOnClickListener(new Button.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                View contentView = LayoutInflater.from(getApplicationContext())
////                        .inflate(R.layout.server_ip, null);
////                contentView.setBackgroundColor(Color.RED);
////                MyApp myApp = (MyApp) getApplication();
////                EditText editText = (EditText) contentView.findViewById(R.id.ip);
////                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
////                String s = editText.getText().toString();
////                myApp.setUrl(s);
////            }
////        });
//
//        final PopupWindow popupWindow = new PopupWindow(contentView,
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        popupWindow.setFocusable(true);
//        popupWindow.setOutsideTouchable(false);
//        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
//
//    }


    /**
     * Post user name and password to server.
     * @param view The button.
     */
    public void logIn(View view) {

        MyApp myApp = (MyApp) getApplication();
        String url =  myApp.getUrl();//((EditText) findViewById(R.id.ipp)).getText().toString();//"http://192.168.1.104/q233/login.php";

        if (url.isEmpty()) {
            popupWindow.show();
        } else {
            url = url + "/q233/login.php";
            PostHelper mpostHelper = new PostHelper(url, mHandler, map);
            String userName = ((EditText) findViewById(R.id.user_name)).getText().toString();
            String passwd = ((EditText) findViewById(R.id.pass_word)).getText().toString();
            ArrayList<NameValuePair> pairs = new ArrayList<>();
            NameValuePair pair0= new BasicNameValuePair("u", userName);
            NameValuePair pair1= new BasicNameValuePair("p", passwd);
            pairs.add(pair0);
            pairs.add(pair1);
            mpostHelper.post(pairs);
        }
    }

    /**
     * Jump to register interface.
     * @param view The button.
     */
    public void jump2reg(View view) {
        Intent intent=new Intent();
        intent.setClass(LogIn.this, Register.class);
        startActivity(intent);
    }


    class MyHandler extends Handler {

        /**
         * Jump to MainActivity interface.
         */
        void jump2main(){
            Intent intent=new Intent();
            //setClass函数的第一个参数是一个Context对象
            //Context是一个类,Activity是Context类的子类,也就是说,所有的Activity对象都可以向上转型为Context对象
            //setClass函数的第二个参数是Class对象,在当前场景下,应该传入需要被启动的Activity的class对象
            intent.setClass(LogIn.this, MainActivity.class);
            startActivity(intent);
        }

        /**
         * What this function will do depends on the massage from server.
         * @param msg Massage from server(has been translated by PostHelper).
         */
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCEED:
                    jump2main();
                    break;
                case FAILEDD:
                    Toast.makeText(getApplicationContext(), "帐号或密码错误", Toast.LENGTH_SHORT).show();
                default:
                    break;
            }
        }
    }
}
