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
 * @auther tb
 * @time 2017/11/20 下午4:41
 * @desc 三阶贝塞尔曲线实现的水滴效果（动画方式）
 */
public class WaterAnim extends View {
    private static final String TAG = "WaterAnim";
    /**
     * 圆的半径
     */
    private float radius = 100f;
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
    
    /**
     * 控件整体移动的距离
     */
    private float distance = 1080f;
    
    /**
     * 注释仅代表向右，左边相反即可（0-7，1-5，2-4状态都是对应的，6是回弹状态，3是维持平移状态）
     * 0:初始状态
     * 1:右半部分向右拉伸
     * 2:逐渐变为椭圆状态
     * 3:椭圆状态维持一段时间，整体右移（前提是移动距离足够长）
     * 4:逐渐变为圆形状态，同时整体右移
     * 5:左半部分向右缩减
     * 6:回弹
     * 7:恢复初始状态
     */
    private int STATUS = 0;
    
    /**
     * 移动的总距离根据半径分为若干份(每一份的长度，最少分为4份，其实3份也行，不过把状态3省略了)
     * 第一份：对应状态STATUS=1
     * 第二份：对应状态STATUS=2
     * 第三份～第N份：对应状态STATUS=3
     * 最后一份：对应状态STATUS=4
     * 最后一份：对应状态STATUS=5
     */
    private float ratio = radius;
    
    /**
     * 总共能分的份数
     */
    private float count = distance / radius;
    
    /**
     * 每次滑动的距离
     */
    private float deltaDistance;
    
    /**
     * 当前已经走过的距离
     */
    private float currentDistance;
    
    /**
     * 动画总时长(单位：ms，松手以后开始)
     */
    private int duration = 2 * 1000;
    
    public WaterAnim(Context context) {
        this(context, null);
    }
    
    public WaterAnim(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public WaterAnim(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        initDataPoints();
        initCtrlPoints();
        initDistance();
    }
    
    private void initDistance() {
        if (count < 4) {
            //保证最低4份
            count = 4f;
            ratio = distance / count;
        }
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
    
    /**
     * 设置当前移动的距离
     *
     * @param deltaDistance
     */
    public void setDeltaDistance(float deltaDistance) {
        this.deltaDistance = deltaDistance;
        currentDistance+=deltaDistance;
//        initDistance();
        postInvalidate();
    }
    
    public void reset(){
        currentDistance=0;
        deltaDistance=0;
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
        
        if (currentDistance <= ratio) {
            STATUS = 1;
        } else if (currentDistance <= ratio * 2f) {
            STATUS = 2;
        } else if (currentDistance <= ratio * (count - 1)) {
            STATUS = 3;
        } else if (currentDistance < distance) {
            STATUS = 4;
        } else {
            STATUS = 5;
        }
        
        switch (STATUS) {
            case 1:
                //最右边的一个数据点和两个控制点同时右移
                mData[1].x += deltaDistance;
                mCtrl[1].x += deltaDistance;
                mCtrl[2].x += deltaDistance;
                break;
            case 2:
                //逐渐拉伸为椭圆状态
                //mDelta的值不断增加但不超过半径的四分之三
                if (mDelta < radius * 3f / 4f) {
                    mDelta += deltaDistance / 5f;
                    initCtrlPoints();
                }
                
                //中间的两个数据点和4个控制点同时右移
                mData[0].x += deltaDistance;
                mData[2].x += deltaDistance;
                
                mCtrl[0].x += deltaDistance;
                mCtrl[7].x += deltaDistance;
                mCtrl[3].x += deltaDistance;
                mCtrl[4].x += deltaDistance;
                
                //最右边的一个数据点和两个控制点同时右移
                mData[1].x += deltaDistance;
                mCtrl[1].x += deltaDistance;
                mCtrl[2].x += deltaDistance;
                break;
            case 3:
                //整体平移
                mData[0].x += deltaDistance;
                mData[1].x += deltaDistance;
                mData[2].x += deltaDistance;
                mData[3].x += deltaDistance;
                
                mCtrl[1].x += deltaDistance;
                mCtrl[2].x += deltaDistance;
                mCtrl[3].x += deltaDistance;
                mCtrl[4].x += deltaDistance;
                mCtrl[5].x += deltaDistance;
                mCtrl[6].x += deltaDistance;
                mCtrl[7].x += deltaDistance;
                mCtrl[0].x += deltaDistance;
                break;
            case 4:
                //逐渐恢复圆形
                //mDelta的值不断减少直至恢复为初始值
                if (mDelta > radius * C) {
                    mDelta -= deltaDistance / 5f;
                    initCtrlPoints();
                } else if (mDelta < radius * C) {
                    mDelta = radius * C;
                    initCtrlPoints();
                }
                
                //最左边的一个数据点和两个控制点同时3倍速度右移
                mData[3].x += (count - 1) * deltaDistance;
                mCtrl[5].x += (count - 1) * deltaDistance;
                mCtrl[6].x += (count - 1) * deltaDistance;
                
                //中间的两个数据点和4个控制点同时2倍速度右移
                mData[0].x += (count - 1) * deltaDistance;
                mData[2].x += (count - 1) * deltaDistance;
                
                mCtrl[0].x += (count - 1) * deltaDistance;
                mCtrl[7].x += (count - 1) * deltaDistance;
                mCtrl[3].x += (count - 1) * deltaDistance;
                mCtrl[4].x += (count - 1) * deltaDistance;
                
                //最右边的一个数据点和两个控制点同时右移
                mData[1].x += deltaDistance;
                mCtrl[1].x += deltaDistance;
                mCtrl[2].x += deltaDistance;
                break;
            case 5:
                //最左边的一个数据点和两个控制点同时右移
                mData[3].x += deltaDistance;
                mCtrl[5].x += deltaDistance;
                mCtrl[6].x += deltaDistance;
                if (mData[3].x == distance - ratio) {
                    //说明已经恢复一个圆了，开始执行状态6
                    STATUS = 6;
                }
                break;
            case 6:
                if (tan < radius / 2f) {
                    if (flag) {
                        if (tan > 0) {
                            mData[3].x -= tan;
                            mCtrl[5].x -= tan;
                            mCtrl[6].x -= tan;
                            tan -= 1;
                        } else {
                            //回弹完成
                            flag = false;
                            tan = 0;
                            STATUS = 7;
                        }
                    } else {
                        mData[3].x += tan;
                        mCtrl[5].x += tan;
                        mCtrl[6].x += tan;
                        tan += 1;
                    }
                } else if (tan == radius / 2f) {
                    flag = true;
                }
                break;
        }
        if (STATUS < 0 && STATUS < 7) {
            postInvalidate();
        }
    }
    
    private int tan = 0;
    private boolean flag = false;
    
    public void setRadius(float radius) {
        this.radius = radius;
    }
    
    public void setDistance(float distance) {
        this.distance = distance;
    }
}
