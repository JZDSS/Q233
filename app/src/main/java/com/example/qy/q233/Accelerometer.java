package com.example.qy.q233;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Qi Yao on 17-3-15.
 */

public class Accelerometer extends Activity {

    public SensorManager mSensorManager;
    private Sensor mSensor;
    public MySensorEventListener mSensorEventListener;
    public float x, y, z, norm;

    /**
     * Initialize an Accelerometer variable.
     *
     * @param mContext Accelerometer is not an Activity Class, it need a Context
     *                 in order to call getSystemService.
     */
    public Accelerometer(Context mContext) {
        mSensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorEventListener = new MySensorEventListener();
    }

    /**
     * Restart to collect data form the sensor.
     */
    protected void resume() {
        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Stop collecting data from the sensor.
     */
    protected void pause() {
        mSensorManager.unregisterListener(mSensorEventListener);
    }

    /**
     * L2 normalization.
     *
     * @param x X-coordinate;
     * @param y Y-coordinate;
     * @param z Z-coordinate.
     * @return L2 norm.
     */
    private float norm(float x, float y, float z) {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }


    private class MySensorEventListener implements SensorEventListener {
        /**
         * Do when the sensor value changed.
         *
         * @param event From which we can get values, accuracy, ect.
         */
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
