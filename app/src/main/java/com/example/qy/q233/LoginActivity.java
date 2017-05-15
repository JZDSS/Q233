package com.example.qy.q233;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
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

public class LoginActivity extends AppCompatActivity {

    static final int SUCCEED = 0;
    static final int FAILEDD = 1;
    MyHandler mHandler;
    HashMap<String, Integer> map;
    AskForServerIP popupWindow;

    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        sp = ((MyApp) getApplication()).sp;

        mHandler = new MyHandler();
        map = new HashMap<>();
        map.put("p", SUCCEED);
        map.put("d", FAILEDD);

        EditText editText_u = ((EditText) findViewById(R.id.login_username));
        EditText editText_p = ((EditText) findViewById(R.id.login_password));

        editText_p.setTransformationMethod(PasswordTransformationMethod.getInstance());

        String username = sp.getString("username", "");
        String password = sp.getString("password", "");
        editText_u.setText(username);
        editText_p.setText(password);

        popupWindow = new AskForServerIP(this, ((MyApp) getApplication()).sp);

        editText_u.setOnFocusChangeListener(new EditTextViewBackgroundSwitcher(this, R.drawable.login_editbk, R.drawable.login_editbk_username));
        editText_p.setOnFocusChangeListener(new EditTextViewBackgroundSwitcher(this, R.drawable.login_editbk, R.drawable.login_editbk_password));

        findViewById(R.id.login_bt).setOnTouchListener(new TouchDark());

        findViewById(R.id.login_bt).setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          if(Debug.ENABLE) {
                              Intent intent=new Intent();
                              intent.setClass(LoginActivity.this, MainActivity.class);
                              startActivity(intent);
                              return;
                          }
                          MyApp myApp = (MyApp) getApplication();
                          String url =  myApp.getUrl();//((EditText) findViewById(R.id.ipp)).getText().toString();//"http://192.168.1.104/q233/login.php";

                          if (url.isEmpty()) {
                              String tmp = ((MyApp) getApplication()).sp.getString("ip","");
                              if (tmp.isEmpty()) {
                                  popupWindow.show();
                              } else {
                                  url = tmp;
                              }
                          }

                          if (!url.isEmpty()){
                              url = "http://" + url + "/q233/login.php";
                              PostHelper mpostHelper = new PostHelper(url, mHandler, map);
                              String userName = ((EditText) findViewById(R.id.login_username)).getText().toString();
                              String passwd = ((EditText) findViewById(R.id.login_password)).getText().toString();
                              ArrayList<NameValuePair> pairs = new ArrayList<>();
                              NameValuePair pair0= new BasicNameValuePair("u", userName);
                              NameValuePair pair1= new BasicNameValuePair("p", passwd);
                              pairs.add(pair0);
                              pairs.add(pair1);
                              mpostHelper.post(pairs);
                          }
                      }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        findViewById(R.id.login_username).requestFocus();
        findViewById(R.id.login_password).requestFocus();
        findViewById(R.id.login_focus).requestFocus();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (KeyboardHider.isShouldHideKeyboard(v, ev)) {
                KeyboardHider.hideKeyboard(this, v.getWindowToken());
            }
            findViewById(R.id.login_focus).requestFocus();
        }
        return super.dispatchTouchEvent(ev);
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
            intent.setClass(LoginActivity.this, MainActivity.class);
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
                    EditText editText_u = ((EditText) findViewById(R.id.login_username));
                    EditText editText_p = ((EditText) findViewById(R.id.login_password));

                    String username = editText_u.getText().toString();
                    String password = editText_p.getText().toString();

                    SharedPreferences sp = ((MyApp) getApplication()).sp;
                    sp.edit().putString("username", username).apply();
                    sp.edit().putString("password", password).apply();
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
