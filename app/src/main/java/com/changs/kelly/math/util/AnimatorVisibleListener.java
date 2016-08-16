package com.changs.kelly.math.util;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Lenovo on 2016/8/9.
 */
public class AnimatorVisibleListener<T extends View> implements ValueAnimator.AnimatorListener {
    private ViewGroup parent;
    private T target;

    public AnimatorVisibleListener(ViewGroup parent, T target) {
        this.parent = parent;
        this.target = target;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        target.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        parent.removeView(target);
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        parent.removeView(target);
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
