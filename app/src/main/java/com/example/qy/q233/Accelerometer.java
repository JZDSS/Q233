package com.example.qy.q233;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by qy on 17-3-15.
 */

public class Accelerometer extends Activity {

    public SensorManager mSensorManager;
    private Sensor mSensor;
    public MySensorEventListener mSensorEventListener;
    public float x, y, z, norm;

    public Accelerometer(Context mContext) {
        mSensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorEventListener = new MySensorEventListener();
    }

    protected void resume() {
        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void pause() {
        mSensorManager.unregisterListener(mSensorEventListener);
    }

    private float norm(float x, float y, float z) {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    private class MySensorEventListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            norm = norm(x, y, z);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
}
