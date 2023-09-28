
package com.hq.tool.animation.simpview.attention;


import android.animation.ObjectAnimator;
import android.view.View;

import com.hq.tool.animation.simpview.BaseViewAnimator;

public class ShakeAnimator extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
        );
    }
}
