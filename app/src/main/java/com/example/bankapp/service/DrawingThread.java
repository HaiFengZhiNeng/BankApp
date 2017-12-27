package com.example.bankapp.service;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.SurfaceHolder;


import com.example.bankapp.common.Constants;
import com.example.bankapp.distinguish.faceDistinguish.face.person.face.Face;
import com.example.bankapp.distinguish.faceDistinguish.face.person.face.YtDetectFace;

import java.util.List;

/**
 * Created by zhangyuanyuan on 2017/9/28.
 */

public class DrawingThread extends HandlerThread implements Handler.Callback {

    private static final int MSG_DRAW = 101;

    private SurfaceHolder mDrawingSurface;
    private Paint mPaint;
    private Handler mReceiver;
    private int measuredWidth;
    private int measuredHeight;

    // 定义一个记录图像是否开始渲染的旗帜
    private boolean mRunning;

    private boolean frontCamera = true;

    public DrawingThread(SurfaceHolder holder, int measuredWidth, int measuredHeight) {
        super("DrawingThread");
        mDrawingSurface = holder;
        this.measuredWidth = measuredWidth;
        this.measuredHeight = measuredHeight;

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5f);
        mPaint.setTextSize(30);
        mPaint.setColor(Color.rgb(255, 203, 15));
        mPaint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    protected void onLooperPrepared() {
        mReceiver = new Handler(getLooper(), this);
        // 开始渲染
        mRunning = true;
        mReceiver.sendEmptyMessage(MSG_DRAW);


    }

    @Override
    public boolean quit() {
        // 退出之前清除所有的消息
        mRunning = false;
        mReceiver.removeCallbacksAndMessages(null);

        return super.quit();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_DRAW:
                if (!mRunning) return true;
                YtDetectFace ytDetectFace = (YtDetectFace) msg.obj;
                if (ytDetectFace != null) {
                    // 锁定 SurfaceView，并返回到要绘图的 Canvas
                    Canvas canvas = mDrawingSurface.lockCanvas();
                    if (canvas == null) {
                        break;
                    }
                    try {
                        synchronized (mDrawingSurface) {
                            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                            // 绘制每个条目
                            List<Face> faces = ytDetectFace.getFace();
                            for (Face face : faces) {
                                drawFace(canvas, face);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        // 解锁 Canvas，并渲染当前的图像
                        mDrawingSurface.unlockCanvasAndPost(canvas);
                    }
                }
                break;
        }
        return true;
    }

    private void drawFace(Canvas canvas, Face face) {

        int x = 0;
        int y = 0;
        float width = 0;
        float height = 0;
        if (measuredHeight > 800) {
            x = face.getX() * measuredWidth / 480;//150
            y = face.getY() * measuredWidth / 480;
            width = face.getWidth() * measuredWidth / 480;
            height = face.getHeight() * measuredWidth / 480 + (measuredHeight - measuredWidth) / 2;
        } else {
            x = face.getX() * measuredWidth / 480;//150
            y = face.getY() * measuredWidth / 480;
            width = face.getWidth() * measuredWidth / 480;
            height = face.getHeight() * measuredWidth / 480;
        }
//        int x = (Constants.displayWidth - measuredWidth) / 2 + face.getX() * 5 / 3;//150
//        int y = (Constants.displayHeight - measuredHeight) / 2 + face.getY() * 5 / 3;


        if (frontCamera) {
//            x = (int)(measuredWidth - x - width);
        }
        //**
        if (Constants.unusual) {

        } else {
            x = (int) (measuredWidth - x - width);
        }

//        canvas.drawRect(x, y, x + width, y + height, mPaint);

        String testString = "性别：" + face.getGender() + "; 年龄：" + face.getAge() + "; 微笑：" + face.getExpression();
        Rect bounds = new Rect();
        mPaint.getTextBounds(testString, 0, testString.length(), bounds);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(testString, x + width / 2, y, mPaint);
//        LogUtils.e(face.getFace_id() + " " + x + " " + y);
    }

    public void clearDraw() {
        Canvas canvas = null;
        try {
            canvas = mDrawingSurface.lockCanvas();
            if (canvas == null) {
                return;
            }
            if (canvas != null) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                mDrawingSurface.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void setDetectFace(YtDetectFace detectFace, boolean frontCamera) {
        this.frontCamera = frontCamera;
        // 通过 Message 参数将位置传给处理程序
        Message msg = Message.obtain(mReceiver, MSG_DRAW, detectFace);
        mReceiver.sendMessage(msg);
    }

}
