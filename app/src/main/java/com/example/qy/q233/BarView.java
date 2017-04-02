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

public class BarView extends SurfaceView implements SurfaceHolder.Callback {

    int mtop, mbottom;
    float value;
    SurfaceHolder mholder;
    Thread t;

    public BarView(Context context, AttributeSet attrs) {
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
        t = new Thread(new MyThread());
        t.start();
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
            while(true) {
                if (value > 0) {
                    mtop = (int) (240 - (240 * value) / 12);
                    mbottom = 240;
                } else {
                    mtop = 240;
                    mbottom = (int) (240 - (240 * value) / 12);
                }
                Canvas canvas = mholder.lockCanvas(null);//获取画布
                canvas.drawColor(Color.WHITE);
                Paint mPaint = new Paint();
                RectF r1 = new RectF(0, 0, 60, 480);//300dp = 900 hhh~
                RectF r2 = new RectF(0, mtop, 60, mbottom);
                mPaint.setAntiAlias(true);// 设置画笔的锯齿效果
                mPaint.setARGB(128, 128, 128, 255);
                canvas.drawRect(r1, mPaint);
                mPaint.setColor(Color.BLUE);
                canvas.drawRoundRect(r2, 30, 30, mPaint);
                mholder.unlockCanvasAndPost(canvas);//解锁画布，提交画好的图像
            }
        }

    }
}
