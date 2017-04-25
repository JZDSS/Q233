package com.example.qy.q233;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.qy.q233.view.CircleImageView;
import com.example.qy.q233.view.CircleLayout;

/**
 * Created by Qi Yao on 17-3-15.
 */

public class MainActivity extends AppCompatActivity {

//    private Button mButton;
    TextView selectedTextView;
    CircleLayout circleMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (((MyApp)getApplication()).getApiVersion() >= 23) {
            requestPermissions(Permission.allPermissions, 0);
        }

        circleMenu = (CircleLayout)findViewById(R.id.main_circle_layout);
        circleMenu.setOnItemSelectedListener(new CircleLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id, String name) {
                selectedTextView.setText(((CircleImageView)circleMenu.getSelectedItem()).getName());
            }
        });

        circleMenu.setOnItemClickListener(new CircleLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id, String name) {

//                circleMenu.rotateViewToCenter((CircleImageView) circleMenu.getSelectedItem(),
//                        false);
//                long DELAY = 10000000;
//                while(DELAY--!=0);
                Intent intent=new Intent();
                switch(circleMenu.getSelectedItem().getId()){
                    case R.id.acc:
                        intent.setClass(MainActivity.this, AccelerometerActivity.class);
                        break;
                    case R.id.bdmap:
                        intent.setClass(MainActivity.this, BDMapActivity.class);
                        break;
                    case R.id.db:
                        intent.setClass(MainActivity.this, SoundActivity.class);
                        break;
                    case R.id.count:
                        intent.setClass(MainActivity.this, CounterActivity.class);
                        break;
                    case R.id.abt_us:
                        intent.setClass(MainActivity.this, AboutUsActivity.class);
                        break;
                    default:
                        return;
                }
                startActivity(intent);

            }
        });


        selectedTextView = (TextView)findViewById(R.id.main_selected_textView);
        selectedTextView.setText(((CircleImageView)circleMenu.getSelectedItem()).getName());



//        mButton = (Button) findViewById(R.id.button_jump1);
//        mButton.setOnClickListener(new ButtonListener());
//        mButton = (Button) findViewById(R.id.button_jump2);
//        mButton.setOnClickListener(new ButtonListener2());
//        mButton = (Button) findViewById(R.id.button_jump3);
//        mButton.setOnClickListener(new ButtonListener3());
//        mButton = (Button) findViewById(R.id.button_jump4);
//        mButton.setOnClickListener(new ButtonListener4());
//        mButton = (Button) findViewById(R.id.button_jump5);
//        mButton.setOnClickListener(new ButtonListener5());
    }

//    private class ButtonListener implements OnClickListener {
//
//        @Override
//        public void onClick(View v) {
//            Intent intent=new Intent();
//            //setClass函数的第一个参数是一个Context对象
//            //Context是一个类,Activity是Context类的子类,也就是说,所有的Activity对象都可以向上转型为Context对象
//            //setClass函数的第二个参数是Class对象,在当前场景下,应该传入需要被启动的Activity的class对象
//            intent.setClass(MainActivity.this, AccelerometerActivity.class);
//            startActivity(intent);
//        }
//    }
//
//    private class ButtonListener2 implements OnClickListener {
//        @Override
//        public void onClick(View v) {
//            Intent intent=new Intent();
//            intent.setClass(MainActivity.this, BDMapActivity.class);
//            startActivity(intent);
//        }
//    }
//
//    class ButtonListener3 implements OnClickListener {
//        @Override
//        public void onClick(View v) {
//            Intent intent=new Intent();
//            intent.setClass(MainActivity.this, AboutUsActivity.class);
//            startActivity(intent);
//        }
//    }
//
//
//    private class ButtonListener4 implements OnClickListener {
//
//        @Override
//        public void onClick(View v) {
//            Intent intent=new Intent();
//            intent.setClass(MainActivity.this, SoundActivity.class);
//            startActivity(intent);
//        }
//    }
//
//    private class ButtonListener5 implements OnClickListener {
//
//        @Override
//        public void onClick(View v) {
//            Intent intent=new Intent();
//            intent.setClass(MainActivity.this, CounterActivity.class);
//            startActivity(intent);
//        }
//    }
}
