package com.example.bankapp.view.takephoto;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.bankapp.distinguish.faceDistinguish.face.person.face.YtDetectFace;
import com.example.bankapp.service.DrawingThread;


public class DrawSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder; // 用于控制SurfaceView

    private DrawingThread mDrawingThread;

    public DrawSurfaceView(Context context) {
        super(context);
        init();
    }

    public DrawSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mHolder = getHolder(); // 获得SurfaceHolder对象
        mHolder.addCallback(this); // 为SurfaceView添加状态监听

        mHolder.setFormat(PixelFormat.TRANSPARENT);
        setZOrderOnTop(true);
//        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mDrawingThread = new DrawingThread(mHolder, getMeasuredWidth(), getMeasuredHeight());
        mDrawingThread.start(); // 启动线程

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mDrawingThread.quit();
        mDrawingThread = null;
    }

    public void setYtDetectFace(YtDetectFace ytDetectFace, boolean frontCamera) {
        if (mDrawingThread != null) {
            mDrawingThread.setDetectFace(ytDetectFace, frontCamera);
        }
    }

    public void clear() {
        if (mDrawingThread != null) {
            mDrawingThread.clearDraw();
        }
    }

}
