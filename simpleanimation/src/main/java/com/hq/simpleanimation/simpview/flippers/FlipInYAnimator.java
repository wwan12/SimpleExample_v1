
package com.hq.simpleanimation.simpview.flippers;

import android.animation.ObjectAnimator;
import android.view.View;

import com.hq.simpleanimation.simpview.BaseViewAnimator;

public class FlipInYAnimator extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "rotationY", 90, -15, 15, 0),
                ObjectAnimator.ofFloat(target, "alpha", 0.25f, 0.5f, 0.75f, 1)
        );
    }
}
