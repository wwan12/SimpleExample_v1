package com.hq.simpleanimation.load.ball;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.FloatRange;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

public class InfectionBallBuilder extends BaseBallBuilder
{
    //动画间隔时间
    private static final long DURATION_TIME   = 888;
    private static final long DURATION_TIME_1 = 222;
    private static final long DURATION_TIME_2 = 333;
    private static final long DURATION_TIME_3 = 1333;
    private static final long DURATION_TIME_4 = 1333;
    //最终阶段
    private static final int  FINAL_STATE     = 4;
    private static final int  SUM_POINT_POS   = 3;
    private float mBallR;
    private Path mPath;
    //当前动画阶段
    private int mCurrAnimatorState = 0;
    //每个小球的偏移量
    private float mCanvasTranslateOffset;

    @Override
    protected void initParams(Context context)
    {
        mBallR = getAllSize() / SUM_POINT_POS;
        mCanvasTranslateOffset = getIntrinsicWidth() / SUM_POINT_POS;
        mPath = new Path();
        initPaint(5);
        initPoints(mBallR);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        drawBall(canvas);
    }

    /**
     * 绘制小球
     *
     * @param canvas
     */
    private void drawBall(Canvas canvas)
    {
        canvas.save();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.translate(0, -mCanvasTranslateOffset);
        super.drawBall(canvas, mPath, mPaint);
        canvas.restore();
    }

    @Override
    protected void prepareStart(ValueAnimator floatValueAnimator)
    {

    }

    @Override
    protected void prepareEnd()
    {

    }

    @Override
    protected void computeUpdateValue(ValueAnimator animation, @FloatRange(from = 0.0, to = 1.0) float animatedValue)
    {
        float offset = mCanvasTranslateOffset;
        switch (mCurrAnimatorState)
        {
            case 0:
                animation.setDuration(DURATION_TIME);
                animation.setInterpolator(new AccelerateInterpolator());
                mBallPoints.get(2).setOffsetY(animatedValue * offset);
                mBallPoints.get(3).setOffsetY(animatedValue * offset);
                mBallPoints.get(4).setOffsetY(animatedValue * offset);
                break;
            case 1:
                animation.setDuration(DURATION_TIME_1);
                animation.setInterpolator(new LinearInterpolator());
                mBallPoints.get(5).setOffsetY(animatedValue * offset);
                mBallPoints.get(6).setOffsetY(animatedValue * offset);
                mBallPoints.get(7).setOffsetY(animatedValue * offset);
                mBallPoints.get(1).setOffsetY(animatedValue * offset);
                mBallPoints.get(0).setOffsetY(animatedValue * offset);
                mBallPoints.get(11).setOffsetY(animatedValue * offset);
                break;
            case 2:
                animation.setDuration(DURATION_TIME_2);
                animation.setInterpolator(new AccelerateInterpolator());
                for (int i = 0; i < mBallPoints.size(); i++)
                {
                    if (i > 10 || i < 8)
                    {
                        mBallPoints.get(i).setOffsetY(animatedValue * offset + offset);
                    }
                    else
                    {
                        mBallPoints.get(i).setOffsetY(animatedValue * offset);
                    }
                }
                break;
            case 3:
                animation.setDuration(DURATION_TIME_3);
                animation.setInterpolator(new DecelerateInterpolator());

                mBallPoints.get(8).setOffsetY(animatedValue * offset + offset);
                mBallPoints.get(9).setOffsetY(animatedValue * offset + offset);
                mBallPoints.get(10).setOffsetY(animatedValue * offset + offset);


                mBallPoints.get(5).setOffsetX(animatedValue * offset);
                mBallPoints.get(6).setOffsetX(animatedValue * offset);
                mBallPoints.get(7).setOffsetX(animatedValue * offset);
                mBallPoints.get(1).setOffsetX(-animatedValue * offset);
                mBallPoints.get(0).setOffsetX(-animatedValue * offset);
                mBallPoints.get(11).setOffsetX(-animatedValue * offset);
                break;
            case 4:
                animation.setDuration(DURATION_TIME_4);
                mPaint.setAlpha((int) ((1 - animatedValue) * 255));
                break;
        }
    }

    @Override
    public void onAnimationRepeat(Animator animation)
    {
        if (++mCurrAnimatorState > FINAL_STATE)
        {//还原到第一阶段
            mCurrAnimatorState = 0;
            for (CirclePoint point : mBallPoints)
            {
                point.setOffsetY(0);
                point.setOffsetX(0);
            }
            mPaint.setAlpha(255);
        }
    }

}
