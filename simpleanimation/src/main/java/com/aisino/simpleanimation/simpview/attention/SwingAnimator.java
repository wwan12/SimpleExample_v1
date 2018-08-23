
package com.aisino.simpleanimation.simpview.attention;

import android.animation.ObjectAnimator;
import android.view.View;

import com.aisino.simpleanimation.simpview.BaseViewAnimator;

public class SwingAnimator extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "rotation", 0, 10, -10, 6, -6, 3, -3, 0)
        );
    }
}
