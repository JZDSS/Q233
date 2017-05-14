package com.example.qy.q233.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Qi Yao on 17-3-15.
 */

public class Accelerometer extends Service{

    static {
        System.loadLibrary("native-lib");
    }

    //private SQLiteDatabase database;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private MySensorEventListener mSensorEventListener;
    public static float x;
    public static float y;
    public static float z;
    public static float norm;

//    /**
//     * Initialize an Accelerometer variable.
//     *
//     * @param mContext Accelerometer is not an Activity Class, it need a Context
//     *                 in order to call getSystemService.
//     */
//    public Accelerometer(Context mContext) {
//        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
//        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        mSensorEventListener = new MySensorEventListener();
////        try {
////            database = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().getCanonicalPath() + "/.0a.db", null);
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getApplication().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorEventListener = new MySensorEventListener();
        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mSensorManager.unregisterListener(mSensorEventListener);
    }

//    /**
//     * Restart to collect data form the sensor.
//     */
//    public void resume() {
//        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
//    }

//    /**
//     * Stop collecting data from the sensor.
//     */
//    public void pause() {
//        mSensorManager.unregisterListener(mSensorEventListener);
//        //database.close();
//    }

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
