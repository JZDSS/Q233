package com.example.qy.q233;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Qi Yao on 17-4-9.
 */

public class SignupActivity extends AppCompatActivity {

    static final int SUCCEED = 0;
    static final int FAILED = 1;
    // mpostHelper;
    SignupActivity.MyHandler mHandler;
    HashMap<String,Integer> map;
    AskForServerIP popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        popupWindow = new AskForServerIP(this, ((MyApp) getApplication()).sp);

        mHandler = new SignupActivity().mHandler;

        map = new HashMap<>();
        map.put("p", SUCCEED);
        map.put("d", FAILED);

        EditText editText_u = ((EditText) findViewById(R.id.signup_username));
        EditText editText_p = ((EditText) findViewById(R.id.signup_password));
        EditText editText_c = ((EditText) findViewById(R.id.signup_confirm));//.setTransformationMethod(PasswordTransformationMethod.getInstance());

        editText_u.setOnFocusChangeListener(new EditTextViewBackgroundSwitcher(this, R.drawable.signup_editbk, R.drawable.signup_editbk_username));
        editText_p.setOnFocusChangeListener(new EditTextViewBackgroundSwitcher(this, R.drawable.signup_editbk, R.drawable.signup_editbk_password));
        editText_c.setOnFocusChangeListener(new EditTextViewBackgroundSwitcher(this, R.drawable.signup_editbk, R.drawable.signup_editbk_confirm));



        editText_p.setTransformationMethod(PasswordTransformationMethod.getInstance());
        editText_c.setTransformationMethod(PasswordTransformationMethod.getInstance());
//        editText_u.setOnTouchListener(new ShowCursor(true));
//        editText_p.setOnTouchListener(new ShowCursor(true));
//        editText_c.setOnTouchListener(new ShowCursor(true));
        ((Button) findViewById(R.id.signup_bt_signup)).setOnTouchListener(new TouchDark());
        ((Button) findViewById(R.id.signup_bt_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        ((EditText) findViewById(R.id.signup_focus)).requestFocus();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (KeyboardHider.isShouldHideKeyboard(v, ev)) {
                KeyboardHider.hideKeyboard(this, v.getWindowToken());
            }
            ((EditText) findViewById(R.id.signup_focus)).requestFocus();
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * Post user's user name and password to server.
     * If successfully registered, jump to Lon In interface.
     * @param view The button.
     */
    public void register(View view) {
        MyApp myApp = (MyApp) getApplication();
        String url =  myApp.getUrl();//((EditText) findViewById(R.id.ipp)).getText().toString();//"http://192.168.1.104/q233/login.php";

        if (url.isEmpty()) {
            popupWindow.show();
        } else {
            url = "http://" + url + "/q233/register.php";
            PostHelper mpostHelper = new PostHelper(url, mHandler, map);
            String userName = ((EditText) findViewById(R.id.signup_username)).getText().toString();
            String passwd = ((EditText) findViewById(R.id.signup_password)).getText().toString();
            String confirm = ((EditText) findViewById(R.id.signup_confirm)).getText().toString();
            if (!passwd.equals(confirm)) {
                Toast.makeText(getApplicationContext(), "两次密码不一致！", Toast.LENGTH_SHORT).show();
                ((EditText) findViewById(R.id.signup_password)).setText("");
                ((EditText) findViewById(R.id.signup_confirm)).setText("");
                return;
            }
            ArrayList<NameValuePair> pairs = new ArrayList<>();
            NameValuePair pair0= new BasicNameValuePair("u", userName);
            NameValuePair pair1= new BasicNameValuePair("p", passwd);
            pairs.add(pair0);
            pairs.add(pair1);
            mpostHelper.post(pairs);
        }
    }


    class MyHandler extends Handler {

        /**
         * Jump to Lon In interface.
         */
        void jump2login(){
            Intent intent=new Intent();
            //setClass函数的第一个参数是一个Context对象
            //Context是一个类,Activity是Context类的子类,也就是说,所有的Activity对象都可以向上转型为Context对象
            //setClass函数的第二个参数是Class对象,在当前场景下,应该传入需要被启动的Activity的class对象
            intent.setClass(SignupActivity.this, LoginActivity.class);
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
                    Toast.makeText(getApplicationContext(), "注册成功！", Toast.LENGTH_SHORT).show();
                    jump2login();
                    break;
                case FAILED:
                    Toast.makeText(getApplicationContext(), "注册失败！", Toast.LENGTH_SHORT).show();
                default:
                    break;
            }
        }
    }
}
