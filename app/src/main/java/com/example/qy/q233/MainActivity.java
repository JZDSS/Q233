package com.example.qy.q233;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private SensorEventListener mSensorEventListener;
    private boolean sensorOn;
    //private static final String TAG = "SensorTest";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                TextView textView_x = (TextView) findViewById(R.id.val_x);
                TextView textView_y = (TextView) findViewById(R.id.val_y);
                TextView textView_z = (TextView) findViewById(R.id.val_z);
                TextView textView_norm = (TextView) findViewById(R.id.val_norm);
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                float norm = norm(x, y, z);
                textView_x.setText("" + x + "");
                textView_y.setText("" + y + "");
                textView_z.setText("" + z + "");
                textView_norm.setText("" + norm + "");
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //mSensorManager.registerListener( mSensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    protected void onResume() {
        super.onResume();
        ((Button) findViewById(R.id.control)).setText(R.string.stop);
        mSensorManager.registerListener(mSensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorOn = true;
    }

    protected void onPause() {
        super.onPause();
        ((Button) findViewById(R.id.control)).setText(R.string.start);
        mSensorManager.unregisterListener(mSensorEventListener);
        sensorOn = false;
    }

    public void clickButton(View view){
        if(sensorOn){
            onPause();
        }
        else {
            onResume();
        }
    }

    private float norm(float x, float y, float z){
        return (float)Math.sqrt(x * x + y * y + z * z);
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

}
