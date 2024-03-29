
package com.hq.simpleanimation.simpview.rotating_exits;

import android.animation.ObjectAnimator;
import android.view.View;

import com.hq.simpleanimation.simpview.BaseViewAnimator;

public class RotateOutDownLeftAnimator extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        float x = target.getPaddingLeft();
        float y = target.getHeight() - target.getPaddingBottom();
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "alpha", 1, 0),
                ObjectAnimator.ofFloat(target, "rotation", 0, 90),
                ObjectAnimator.ofFloat(target, "pivotX", x, x),
                ObjectAnimator.ofFloat(target, "pivotY", y, y)
        );
    }
}
