
package com.aisino.simpleanimation.simpview.fading_entrances;

import android.animation.ObjectAnimator;
import android.view.View;

import com.aisino.simpleanimation.simpview.BaseViewAnimator;

public class FadeInLeftAnimator extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "alpha", 0, 1),
                ObjectAnimator.ofFloat(target, "translationX", -target.getWidth() / 4, 0)
        );
    }
}
