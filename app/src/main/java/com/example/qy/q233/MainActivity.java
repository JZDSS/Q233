package com.example.qy.q233;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.qy.q233.service.Accelerometer;
import com.example.qy.q233.service.BDMapService;
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
        startService(new Intent(this, Accelerometer.class));
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
        startService(new Intent(this, BDMapService.class));
    }

}
