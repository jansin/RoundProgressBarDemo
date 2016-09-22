package com.miao.roundprogressbar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.text.DecimalFormat;

/**
 * Created by miaojx on 16/9/18.
 */
public class RoundProgressBar extends View {

    private Paint mPaint;

    private int mRoundColor;
    private int mRoundPrOneColor;
    private int mRoundPrTwoColor;
    private int mRoundPrThreeColor;

    private int mTopTextColor;
    private int mBottomTextColor;

    private float mTopTextSize;
    private float mBottomTextSize;

    private float mRoundWidth;
    private float mRoundPrOneWidth;
    private float mRoundPrTwoWidth;
    private float mRoundPrThreeWidth;

    private float mSmallRoundWith;

    private double mProgressMax;
    private double mProgress;
    private Typeface mTypeface;
    private float mCurrentAngle;
    private int mRadius;
    private double mPercent;
    private boolean mIsShowSmallCicle = false;

    public RoundProgressBar(Context context) {
        this(context, null);
        mTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/cond.ttf");
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/cond.ttf");
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/cond.ttf");
        mPaint = new Paint();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
        mRadius = mTypedArray.getColor(R.styleable.RoundProgressBar_radius, 10);
        mRoundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, 0XFFFFFF);
        mRoundPrOneColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressOneColor, 0XFFFFFF);
        mRoundPrTwoColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressTwoColor, 0XFFFFFF);
        mRoundPrThreeColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressThreeColor, 0X000000);

        mTopTextColor = mTypedArray.getColor(R.styleable.RoundProgressBar_topTextColor, 0XFFFFFF);
        mBottomTextColor = mTypedArray.getColor(R.styleable.RoundProgressBar_bottomTextColor, 0XFFFFFF);

        mTopTextSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_topTextSize, 15);
        mBottomTextSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_bottomTextSize, 5);

        mRoundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 100);
        mRoundPrOneWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundProgressOneWidth, 5);
        mRoundPrTwoWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundProgressTwoWidth, 5);
        mRoundPrThreeWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundProgressThreeWidth, 5);
        mSmallRoundWith = mTypedArray.getDimension(R.styleable.RoundProgressBar_smallRoundWidth, 6);

        mProgressMax = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 0);

        mTypedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        mPaint.setColor(mRoundColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mRoundWidth);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);

        //第一个圆弧
        mPaint.setColor(mRoundPrOneColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mRoundPrOneWidth);
        mPaint.setAntiAlias(true);
        RectF oval1 = new RectF(0, 0, mRadius * 2, mRadius * 2);
        canvas.drawArc(oval1, -90, 360 * 0.8f, false, mPaint);
        //第二个圆弧
        mPaint.setColor(mRoundPrTwoColor);
        mPaint.setStrokeWidth(mRoundPrTwoWidth);
        mPaint.setAntiAlias(true);
        RectF oval2 = new RectF(0, 0, mRadius * 2, mRadius * 2);
        canvas.drawArc(oval2, -90, 360 * 7 / 10, false, mPaint);
        //圆弧
        mPaint.setStrokeWidth(mRoundPrThreeWidth);
        mPaint.setColor(mRoundPrThreeColor);
        canvas.drawArc(new RectF(0, 0, mRadius * 2, mRadius * 2), -90, 360 * mCurrentAngle, false, mPaint);

        mPaint.setStrokeWidth(0);
        mPaint.setColor(mTopTextColor);
        mPaint.setTextSize(mTopTextSize);
        mPaint.setTypeface(mTypeface);
        DecimalFormat df = new DecimalFormat("###.0");
        if(mPercent >mProgress)
            mPercent = mProgress;
        float textWidth = mPaint.measureText(df.format(mPercent) + "");
        canvas.drawText(df.format(mPercent) + "", mRadius - textWidth / 2, mRadius + mTopTextSize / 2 - 80, mPaint);

        if (mIsShowSmallCicle) {
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mRoundPrThreeColor);
            float currentDegreeFlag = 360 * mCurrentAngle;
            float smallCircleX = 0, smallCircleY = 0;
            float hudu = (float) Math.abs(Math.PI * currentDegreeFlag / 180);//Math.abs：绝对值 ，Math.PI：表示π ， 弧度 = 度*π / 180
            smallCircleX = (float) Math.abs(Math.sin(hudu) * mRadius + mRadius);
            smallCircleY = (float) Math.abs(mRadius - Math.cos(hudu) * mRadius);
            canvas.drawCircle(smallCircleX, smallCircleY, mSmallRoundWith, mPaint);
        }
        canvas.restore();

    }

    public void startCountDownTime(final OnProgressFinishListener listener, double prrogress, double progressMax) {
        mIsShowSmallCicle = false;
        mProgress = prrogress;
        mProgressMax = progressMax;
        mPercent = (mProgress / 30.0);
        float cicle = (float) (mProgress / mProgressMax);
        if(cicle > 1)
            cicle = 1;
        ValueAnimator animator = ValueAnimator.ofFloat(0, cicle);
        //动画时长，让进度条在CountDown时间内正好从0-360走完，这里由于用的是CountDownTimer定时器，倒计时要想减到0则总时长需要多加1000毫秒，所以这里时间也跟着+1000ms
        animator.setDuration(3000);
        animator.setInterpolator(new LinearInterpolator());//匀速
        animator.setRepeatCount(0);//表示不循环，-1表示无限循环
        //值从0-1.0F 的动画，动画时长为countdownTime，ValueAnimator没有跟任何的控件相关联，那也正好说明ValueAnimator只是对值做动画运算，而不是针对控件的，我们需要监听ValueAnimator的动画过程来自己对控件做操作
        //添加监听器,监听动画过程中值的实时变化(animation.getAnimatedValue()得到的值就是0-1.0)
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**
                 * 这里我们已经知道ValueAnimator只是对值做动画运算，而不是针对控件的，因为我们设置的区间值为0-1.0f
                 * 所以animation.getAnimatedValue()得到的值也是在[0.0-1.0]区间，而我们在画进度条弧度时，设置的当前角度为360*currentAngle，
                 * 因此，当我们的区间值变为1.0的时候弧度刚好转了360度
                 */
                mCurrentAngle = (float) animation.getAnimatedValue();
                invalidate();//实时刷新view，这样我们的进度条弧度就动起来了
            }
        });
        //开启动画
        animator.start();
        mIsShowSmallCicle = true;
//        还需要另一个监听，监听动画状态的监听器
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //倒计时结束的时候，需要通过自定义接口通知UI去处理其他业务逻辑
                if (listener != null) {
                    listener.progressFinished();
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        countdownMethod();
    }

    //倒计时的方法
    private void countdownMethod() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (mPercent < mProgress) {
                    Log.e("miaojx", "----1---" + mPercent + "");
                    mPercent = mPercent + (mProgress / 30.0);
                    Log.e("miaojx", "----2---" + mPercent + "");
                    postInvalidate();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize;
        int heightSize;
        int strokeWidth = Math.max((int) mRoundWidth, (int) mRoundPrThreeWidth);
        if (widthMode != MeasureSpec.EXACTLY) {
            widthSize = getPaddingLeft() + mRadius * 2 + strokeWidth + getPaddingRight();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize = getPaddingTop() + mRadius * 2 + strokeWidth + getPaddingBottom();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setmRadius(int mRadius) {
        this.mRadius = mRadius;
    }

    public interface OnProgressFinishListener {
        void progressFinished();
    }
}
