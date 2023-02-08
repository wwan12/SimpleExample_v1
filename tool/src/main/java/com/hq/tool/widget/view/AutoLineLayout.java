package com.aisino.grblfz.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.aisino.grblfz.R;

public class AutoLineLayout extends ViewGroup {
    private int horizontalSpace;

    private int verticalSpace;

    private int theRealWidth;

    public AutoLineLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.AutoLineLayout);
        horizontalSpace= (int) Math.ceil(array.getDimension(R.styleable.AutoLineLayout_horizontalSpace,0));
        verticalSpace=(int) Math.ceil(array.getDimension(R.styleable.AutoLineLayout_verticalSpace,0));
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //不测算宽度模式，因为不管是wrap_content还是match_parent都按match_parent来算
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int lineWidth=0;
        int lineHeight=0;
        int height=0;
        int paddingStart=getPaddingStart();
        int paddingEnd=getPaddingEnd();
        int size=widthSize-paddingStart-paddingEnd;
        theRealWidth=size;
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        for (int i=0;i<getChildCount();i++){
            View view=getChildAt(i);
            int childHeight=view.getMeasuredHeight();
            int childWidth=view.getMeasuredWidth();
            int nowWidth=childWidth+lineWidth;
            boolean changeLine=nowWidth>size;
            changeLine=changeLine||(nowWidth-horizontalSpace>size);
            if (changeLine){
                lineWidth=childWidth+horizontalSpace;
                height+=lineHeight+verticalSpace;//这里的lineHeight其实是上一行的所有view的高度的最大值
                lineHeight=childHeight;
            }else {
                lineHeight=Math.max(lineHeight,childHeight);
                lineWidth+=childWidth+horizontalSpace;
                if (i==0)
                    height+=lineHeight;
            }
        }
        height+=getPaddingTop()+getPaddingBottom();
        setMeasuredDimension(widthSize,heightMode==MeasureSpec.EXACTLY?heightSize:height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int paddingStart=getPaddingStart();
        int leftOffset = paddingStart;
        int topOffset = getPaddingTop();
        int lineHeight=0;
        for (int i=0;i<getChildCount();i++){
            View childView=getChildAt(i);
            int childWidth=childView.getMeasuredWidth();
            int childHeight=childView.getMeasuredHeight();
            int nowWidth=childWidth+leftOffset;
            boolean changeLine=nowWidth>theRealWidth;
            changeLine=changeLine||(nowWidth-horizontalSpace>theRealWidth);
            if (changeLine){
                leftOffset=paddingStart;
                topOffset+=lineHeight+verticalSpace;
                childView.layout(leftOffset,topOffset,childWidth+leftOffset,topOffset+childHeight);
                leftOffset+=childWidth+horizontalSpace;
                lineHeight=childHeight;
            }else {
                childView.layout(leftOffset,topOffset,childWidth+leftOffset,topOffset+childHeight);
                leftOffset+=childWidth+horizontalSpace;
                lineHeight=Math.max(lineHeight,childHeight);
            }
        }
    }

}
