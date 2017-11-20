package com.example.tb.waterdrop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by : tb on 2017/9/1 上午11:00.
 * Description :
 */
public class WaterDropView extends View {
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
    
    
    public WaterDropView(Context context) {
        this(context, null);
    }
    
    public WaterDropView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public WaterDropView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
        mPaint.setStyle(Paint.Style.FILL);
        initDataPoints();
        initCtrlPoints();
    }
    
    private void initDataPoints() {
        for (int i = 0; i < 4; i++) {
            mData[i]=new PointF();
        }
        mData[0].set(0, radius);
        mData[1].set(radius, 0);
        mData[2].set(0, -radius);
        mData[3].set(-radius, 0);
    }
    
    private void initCtrlPoints() {
        for (int i = 0; i < 8; i++) {
            mCtrl[i]=new PointF();
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
        
        Path path = new Path();
        path.moveTo(mData[0].x, mData[0].y);
        path.cubicTo(mCtrl[0].x, mCtrl[0].y, mCtrl[1].x, mCtrl[1].y, mData[1].x, mData[1].y);
        path.cubicTo(mCtrl[2].x, mCtrl[2].y, mCtrl[3].x, mCtrl[3].y, mData[2].x, mData[2].y);
        path.cubicTo(mCtrl[4].x, mCtrl[4].y, mCtrl[5].x, mCtrl[5].y, mData[3].x, mData[3].y);
        path.cubicTo(mCtrl[6].x, mCtrl[6].y, mCtrl[7].x, mCtrl[7].y, mData[0].x, mData[0].y);
        
        canvas.drawPath(path, mPaint);
    }
    
    private float mLastX;
    private float mFirstX;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastX=event.getX();
                mFirstX=event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX=event.getX()-mLastX;
                mLastX=event.getX();
                if(event.getX()-mFirstX>=10*radius&&event.getX()-mFirstX<=12*radius){
                    mData[3].x+=deltaX;
                    mCtrl[5].x+=deltaX;
                    mCtrl[6].x+=deltaX;
                    
                    mData[0].x+=deltaX/2f;
                    mData[2].x+=deltaX/2f;
                    mCtrl[0].x+=deltaX/2f;
                    mCtrl[7].x+=deltaX/2f;
                    mCtrl[3].x+=deltaX/2f;
                    mCtrl[4].x+=deltaX/2f;
                }else{
                    mData[1].x+=deltaX;
                    mCtrl[1].x+=deltaX;
                    mCtrl[2].x+=deltaX;
                    
                    mData[0].x+=deltaX/2f;
                    mData[2].x+=deltaX/2f;
                    mCtrl[0].x+=deltaX/2f;
                    mCtrl[7].x+=deltaX/2f;
                    mCtrl[3].x+=deltaX/2f;
                    mCtrl[4].x+=deltaX/2f;
                    
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
