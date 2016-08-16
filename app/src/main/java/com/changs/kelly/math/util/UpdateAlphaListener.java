package com.changs.kelly.math.util;

import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by Lenovo on 2016/8/9.
 */
public class UpdateAlphaListener implements ValueAnimator.AnimatorUpdateListener {
    private View target;

    public UpdateAlphaListener(View target) {
        this.target = target;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        final float animatedFraction = animation.getAnimatedFraction();
        if (animatedFraction > 0.8) {
            float alpha = 1 - animatedFraction;
            alpha = alpha / 0.2F;
            target.setAlpha(alpha);
        }
    }
}
