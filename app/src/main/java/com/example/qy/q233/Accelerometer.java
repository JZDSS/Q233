package com.example.qy.q233;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;

import java.io.IOException;

/**
 * Created by Qi Yao on 17-3-15.
 */

class Accelerometer{

    static {
        System.loadLibrary("native-lib");
    }

    private SQLiteDatabase database;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private MySensorEventListener mSensorEventListener;
    float x, y, z, norm;

    /**
     * Initialize an Accelerometer variable.
     *
     * @param mContext Accelerometer is not an Activity Class, it need a Context
     *                 in order to call getSystemService.
     */
    Accelerometer(Context mContext) {
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorEventListener = new MySensorEventListener();
        try {
            database = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().getCanonicalPath() + "/.0a.db", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restart to collect data form the sensor.
     */
    void resume() {
        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Stop collecting data from the sensor.
     */
    void pause() {
        mSensorManager.unregisterListener(mSensorEventListener);
        database.close();
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

    private native int maxFromJNI(int[] arr);

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
