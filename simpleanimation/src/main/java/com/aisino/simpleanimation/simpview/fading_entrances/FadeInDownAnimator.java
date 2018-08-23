
package com.aisino.simpleanimation.simpview.fading_entrances;

import android.animation.ObjectAnimator;
import android.view.View;

import com.aisino.simpleanimation.simpview.BaseViewAnimator;

public class FadeInDownAnimator extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "alpha", 0, 1),
                ObjectAnimator.ofFloat(target, "translationY", -target.getHeight() / 4, 0)
        );
    }
}
