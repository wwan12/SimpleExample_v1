
package com.hq.tool.animation.simpview.rotating_exits;

import android.animation.ObjectAnimator;
import android.view.View;

import com.hq.tool.animation.simpview.BaseViewAnimator;

public class RotateOutAnimator extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "alpha", 1, 0),
                ObjectAnimator.ofFloat(target, "rotation", 0, 200)
        );
    }
}
