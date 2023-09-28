
package com.hq.tool.animation.simpview.fading_exits;

import android.animation.ObjectAnimator;
import android.view.View;

import com.hq.tool.animation.simpview.BaseViewAnimator;

public class FadeOutAnimator extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "alpha", 1, 0)
        );
    }
}
