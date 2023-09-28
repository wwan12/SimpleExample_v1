
package com.hq.tool.animation.simpview.sliders;

import android.animation.ObjectAnimator;
import android.view.View;

import com.hq.tool.animation.simpview.BaseViewAnimator;

public class SlideOutLeftAnimator extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "alpha", 1, 0),
                ObjectAnimator.ofFloat(target, "translationX", 0, -target.getRight())
        );
    }
}
