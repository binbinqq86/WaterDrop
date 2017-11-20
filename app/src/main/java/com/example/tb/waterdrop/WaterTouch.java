package com.example.tb.waterdrop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by : tb on 2017/9/1 上午11:00.
 * Description :三阶贝塞尔曲线实现的水滴效果（触摸实现）
 */
public class WaterTouch extends View {
    private static final String TAG = "WaterDropView";
    /**
     * 圆的半径
     */
    private float radius = 200f;
    /**
     * 画布的坐标中心
     */
    private float centerX, centerY;
    /**
     * 一个常量，用来计算绘制圆形贝塞尔曲线控制点的位置
     */
    private static final float C = 0.551915024494f;
    /**
     * 圆形的控制点与数据点的差值
     */
    private float mDelta = radius * C;
    /**
     * 绘制圆的画笔
     */
    private Paint mPaint;
    /**
     * 逆时针记录绘制圆形的四个数据点
     */
    private PointF[] mData = new PointF[4];
    /**
     * 逆时针记录绘制圆形的八个控制点
     */
    private PointF[] mCtrl = new PointF[8];
    
    public WaterTouch(Context context) {
        this(context, null);
    }
    
    public WaterTouch(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public WaterTouch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        initDataPoints();
        initCtrlPoints();
    }
    
    private void initDataPoints() {
        for (int i = 0; i < 4; i++) {
            mData[i] = new PointF();
        }
        mData[0].set(0, radius);
        mData[1].set(radius, 0);
        mData[2].set(0, -radius);
        mData[3].set(-radius, 0);
    }
    
    private void initCtrlPoints() {
        for (int i = 0; i < 8; i++) {
            mCtrl[i] = new PointF();
        }
        mCtrl[0].set(mData[0].x + mDelta, mData[0].y);
        mCtrl[1].set(mData[1].x, mData[1].y + mDelta);
        mCtrl[2].set(mData[1].x, mData[1].y - mDelta);
        mCtrl[3].set(mData[2].x + mDelta, mData[2].y);
        mCtrl[4].set(mData[2].x - mDelta, mData[2].y);
        mCtrl[5].set(mData[3].x, mData[3].y - mDelta);
        mCtrl[6].set(mData[3].x, mData[3].y + mDelta);
        mCtrl[7].set(mData[0].x - mDelta, mData[0].y);
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = radius;
        centerY = h / 2f;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        canvas.translate(centerX, centerY); // 将坐标系移动到画布中央
        
        //绘制数据点和控制点
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(20);
        for (int i = 0; i < mData.length; i++) {
//            canvas.drawPoint(mData[i].x, mData[i].y, mPaint);
        }
        for (int i = 0; i < mCtrl.length; i++) {
//            canvas.drawPoint(mCtrl[i].x, mCtrl[i].y, mPaint);
        }
        
        // 绘制辅助线
        mPaint.setStrokeWidth(4);
//        canvas.drawLine(mData[0].x, mData[0].y, mCtrl[0].x, mCtrl[0].y, mPaint);
//        canvas.drawLine(mData[0].x, mData[0].y, mCtrl[7].x, mCtrl[7].y, mPaint);
//
//        canvas.drawLine(mData[1].x, mData[1].y, mCtrl[1].x, mCtrl[1].y, mPaint);
//        canvas.drawLine(mData[1].x, mData[1].y, mCtrl[2].x, mCtrl[2].y, mPaint);
//
//        canvas.drawLine(mData[2].x, mData[2].y, mCtrl[3].x, mCtrl[3].y, mPaint);
//        canvas.drawLine(mData[2].x, mData[2].y, mCtrl[4].x, mCtrl[4].y, mPaint);
//
//        canvas.drawLine(mData[3].x, mData[3].y, mCtrl[5].x, mCtrl[5].y, mPaint);
//        canvas.drawLine(mData[3].x, mData[3].y, mCtrl[6].x, mCtrl[6].y, mPaint);
        
        //绘制贝塞尔曲线
        Path path = new Path();
        path.moveTo(mData[0].x, mData[0].y);
        path.cubicTo(mCtrl[0].x, mCtrl[0].y, mCtrl[1].x, mCtrl[1].y, mData[1].x, mData[1].y);
        path.cubicTo(mCtrl[2].x, mCtrl[2].y, mCtrl[3].x, mCtrl[3].y, mData[2].x, mData[2].y);
        path.cubicTo(mCtrl[4].x, mCtrl[4].y, mCtrl[5].x, mCtrl[5].y, mData[3].x, mData[3].y);
        path.cubicTo(mCtrl[6].x, mCtrl[6].y, mCtrl[7].x, mCtrl[7].y, mData[0].x, mData[0].y);
        
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, mPaint);
    }
    
    private float mLastX;
    private float mFirstX;
    /**
     * 注释仅代表向右，左边相反即可（0-5，1-4，2-3状态都是对应的）
     * 0:初始状态
     * 1:右半部分向右拉伸
     * 2:逐渐变为椭圆状态
     * 3:逐渐变为圆形状态，同时整体右移
     * 4:左半部分向右缩减
     * 5:恢复初始状态
     */
    private int STATUS = 0;
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                STATUS = 0;
                mLastX = event.getX();
                mFirstX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                //总体移动距离为半径的三倍
                float deltaX = event.getX() - mLastX;
                mLastX = event.getX();
                
                if (event.getX() - mFirstX <= radius) {
                    STATUS = 1;
                } else if (event.getX() - mFirstX <= 2 * radius) {
                    STATUS = 2;
                } else if (event.getX() - mFirstX <= 3 * radius) {
                    STATUS = 3;
                } else if (event.getX() - mFirstX <= 4 * radius) {
                    STATUS = 4;
                } else {
                    STATUS = 5;
                }
                if (STATUS == 1) {
                    //最右边的一个数据点和两个控制点同时右移
                    mData[1].x += deltaX;
                    mCtrl[1].x += deltaX;
                    mCtrl[2].x += deltaX;
                }
                if (STATUS == 2) {
                    //逐渐拉伸为椭圆状态
                    //mDelta的值不断增加但不超过半径的四分之三
                    if (mDelta < radius * 3f / 4f) {
                        mDelta += deltaX / 5f;
                        initCtrlPoints();
                    }
                    
                    //中间的两个数据点和4个控制点同时右移
                    mData[0].x += deltaX;
                    mData[2].x += deltaX;
                    
                    mCtrl[0].x += deltaX;
                    mCtrl[7].x += deltaX;
                    mCtrl[3].x += deltaX;
                    mCtrl[4].x += deltaX;
                    
                    //最右边的一个数据点和两个控制点同时右移
                    mData[1].x += deltaX;
                    mCtrl[1].x += deltaX;
                    mCtrl[2].x += deltaX;
                }
                if (STATUS == 3) {
                    //逐渐恢复圆形
                    //mDelta的值不断减少直至恢复为初始值
                    if (mDelta > radius * C) {
                        mDelta -= deltaX / 5f;
                        initCtrlPoints();
                    } else if (mDelta < radius * C) {
                        mDelta = radius * C;
                        initCtrlPoints();
                    }
                    
                    //最左边的一个数据点和两个控制点同时2倍速度右移
                    mData[3].x += 2 * deltaX;
                    mCtrl[5].x += 2 * deltaX;
                    mCtrl[6].x += 2 * deltaX;
                    
                    //中间的两个数据点和4个控制点同时2倍速度右移
                    mData[0].x += 2 * deltaX;
                    mData[2].x += 2 * deltaX;
                    
                    mCtrl[0].x += 2 * deltaX;
                    mCtrl[7].x += 2 * deltaX;
                    mCtrl[3].x += 2 * deltaX;
                    mCtrl[4].x += 2 * deltaX;
                    
                    //最右边的一个数据点和两个控制点同时右移
                    mData[1].x += deltaX;
                    mCtrl[1].x += deltaX;
                    mCtrl[2].x += deltaX;
                }
                if (STATUS == 4) {
                    //最左边的一个数据点和两个控制点同时右移
                    mData[3].x += deltaX;
                    mCtrl[5].x += deltaX;
                    mCtrl[6].x += deltaX;
                }
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
            default:
                break;
        }
        return true;
    }
    
}
