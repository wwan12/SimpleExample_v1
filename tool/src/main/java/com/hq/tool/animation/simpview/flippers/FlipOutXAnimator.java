
package com.hq.tool.animation.simpview.flippers;

import android.animation.ObjectAnimator;
import android.view.View;

import com.hq.tool.animation.simpview.BaseViewAnimator;

public class FlipOutXAnimator extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "rotationX", 0, 90),
                ObjectAnimator.ofFloat(target, "alpha", 1, 0)
        );
    }
}
