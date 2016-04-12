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
        this.points=point;
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
                default:
                    break;
            }

        }
    }

    /**
     * 绘制柱形谱线
     * @param canvas
     */
    private void drawColumns(Canvas canvas){
        canvas.drawColor(Color.BLACK);

        for (int i = 0; i < bytes.length - 1; i+=20) {
            int left = getWidth() * i / (bytes.length - 1);
            int top = getHeight() - (byte) (bytes[i + 1] + 128) * getHeight() / 128;
            int right = left + 1;
            int bottom = getHeight();
            canvas.drawRect(left, top, right, bottom, paint);
        }
    }

    /**
     * 绘制线条谱线
     * @param canvas
     */
    private void drawLine(Canvas canvas){
        canvas.drawColor(Color.BLACK);
        Path path=new Path();
        path.moveTo(0, getHeight() / 2);
        for(int i=0;i<bytes.length-1;i++){
            canvas.drawPoint(getWidth()*i/bytes.length,
                             getHeight() - (byte) (bytes[i + 1] + 128) * getHeight() / 128,
                             paint);
//            // 计算第i个点的x坐标
//            points[i * 4] = getWidth()*i/(bytes.length - 1);
//            // 根据bytes[i]的值（波形点的值）计算第i个点的y坐标
//            points[i * 4 + 1] = (getHeight() / 2)
//                    + ((byte) (bytes[i] + 128)) * 128
//                    / (getHeight() / 2);
//            // 计算第i+1个点的x坐标
//            points[i * 4 + 2] = getWidth() * (i + 1)
//                    / (bytes.length - 1);
//            // 根据bytes[i+1]的值（波形点的值）计算第i+1个点的y坐标
//            points[i * 4 + 3] = (getHeight() / 2)
//                    + ((byte) (bytes[i + 1] + 128)) * 128
//                    / (getHeight() / 2);
        }
//        canvas.drawPath(path,paint);

    }

    public void setDrawType(int drawType) {
        this.drawType = drawType;
    }
}

