
package com.hq.simpleanimation.simpview.fading_exits;

import android.animation.ObjectAnimator;
import android.view.View;

import com.hq.simpleanimation.simpview.BaseViewAnimator;

public class FadeOutAnimator extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "alpha", 1, 0)
        );
    }
}
