
package com.hq.simpleanimation.simpview.fading_entrances;

import android.animation.ObjectAnimator;
import android.view.View;

import com.hq.simpleanimation.simpview.BaseViewAnimator;

public class FadeInAnimator extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "alpha", 0, 1)
        );
    }
}
