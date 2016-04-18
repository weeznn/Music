package com.example.weezn.music;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * VisualizerView
 *
 * @author: weezn
 * @time: 2016/3/25 21:37
 */
public class VisualizerView extends View {
    private static final String TAG = "VisualizerView";
    private Paint paint = new Paint();

    private byte[] bytes;
    private float[] points;

    private int drawType;

    public VisualizerView(Context context) {
        super(context);
        init();
    }

    public VisualizerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VisualizerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public VisualizerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        Log.i(TAG, "init");
        bytes = null;
        //初始化画笔
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1f);
    }

    public void updata(byte[] up) {
        Log.i(TAG, "updata");
        this.bytes = up;
        //重绘
        invalidate();
    }

    public void updata(float point[]) {
        Log.i(TAG, "updata");
        this.points = point;
        //重绘
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (null == bytes) {
            canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paint);
        } else {
            switch (drawType) {
                case Contanst.COLUMNS:
                    drawColumns(canvas);
                    break;
                case Contanst.LINE:
                    drawLine(canvas);
                    break;
                case Contanst.RING:
                    drawRing(canvas);
                    break;
                case Contanst.Cicle:
                    drawCircle(canvas);
                    break;
                default:
                    break;
            }

        }
    }

    /**
     * 绘制柱形谱线
     *
     * @param canvas
     */
    private void drawColumns(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        for (int i = 0; i < bytes.length - 1; i += 10) {
            int left = getWidth() * i / (bytes.length - 1);
            int top = getHeight() - (byte) (bytes[i + 1]) * getHeight() / 128;
            int right = left + 10;
            int bottom = getHeight();
            canvas.drawRect(left, top, right, bottom, paint);
        }
    }

    /**
     * 绘制线条谱线
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);
        Log.i(TAG," "+bytes[0]);
        float sita = (float) ((bytes[0]) * 30/ 16);
//        canvas.drawLine(0, (float) (getHeight() / 2 - Math.tan(sita) * getWidth() / 2),
//                getWidth(), (float) (getHeight() / 2 + Math.tan(sita) * getWidth() / 2), paint);
        Path p=new Path();
        p.moveTo(0, (float) (getHeight() / 2 - Math.tan(sita) * getWidth() / 2));
        p.lineTo(0,getHeight());
        p.lineTo(getWidth(),getHeight());
        p.lineTo(getWidth(), (float) (getHeight() / 2 + Math.tan(sita) * getWidth() / 2));
        p.lineTo(0, (float) (getHeight() / 2 - Math.tan(sita) * getWidth() / 2));
        canvas.drawPath(p,paint);
    }

    /**
     * 绘制环形谱线
     *
     * @param canvas
     */
    private void drawRing(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(20);

        float one = 360 / bytes.length;
        int radius = 200;

        for (int i = 0, j = 1; i < bytes.length; j++, i += 20) {
            float len = bytes[i] * radius / 18;
            canvas.drawLine(getWidth() / 2, getHeight() / 2,
                    (float) (getWidth() / 2 + len * Math.sin(one * j)), (float) (getHeight() / 2 + len * Math.cos(j * one)), paint);
        }
        paint.setColor(Color.BLACK);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);
    }

    /**
     * 绘制波纹图谱
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);

        int radius = 500 / 18;
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, bytes[0] * radius, paint);
        canvas.drawCircle(0,0,bytes[0] * radius, paint);
        canvas.drawCircle(0,getHeight(),bytes[0] * radius, paint);
        canvas.drawCircle(getWidth(),0,bytes[0] * radius, paint);
        canvas.drawCircle(getWidth(),getHeight(),bytes[0] * radius, paint);
    }

    public void setDrawType(int drawType) {
        this.drawType = drawType;
    }
}

