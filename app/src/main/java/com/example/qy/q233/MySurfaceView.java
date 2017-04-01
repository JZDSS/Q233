package com.example.qy.q233;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by JZBB on 2017/4/1.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder mholder;

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mholder = this.getHolder();
        mholder.addCallback(this);
    }
//    public MySurfaceView(Context context) {
//        super(context);
//        mholder = this.getHolder();
//        mholder.addCallback(this);
//    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        new Thread(new MyThread()).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    class MyThread implements Runnable{

        @Override
        public void run() {
            Canvas canvas = mholder.lockCanvas(null);//获取画布
            canvas.drawColor(Color.WHITE);
            Paint mPaint = new Paint();
            RectF r1 = new RectF(0,900,90,0);//300dp = 900 hhh~
            RectF r2 = new RectF(10,200,80,600);
            mPaint.setAntiAlias(true);// 设置画笔的锯齿效果
            mPaint.setARGB(128, 128, 128, 255);
            canvas.drawRect(r1, mPaint);
            mPaint.setColor(Color.BLUE);
            canvas.drawRoundRect(r2, 20, 20, mPaint);
            mholder.unlockCanvasAndPost(canvas);//解锁画布，提交画好的图像

        }

    }
}
