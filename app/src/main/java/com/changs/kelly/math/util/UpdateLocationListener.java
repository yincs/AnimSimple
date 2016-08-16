package com.changs.kelly.math.util;

import android.animation.ValueAnimator;
import android.graphics.Point;
import android.view.View;

/**
 * Created by Lenovo on 2016/8/9.
 */
public class UpdateLocationListener implements ValueAnimator.AnimatorUpdateListener {
    private View target;

    public UpdateLocationListener(View target) {
        this.target = target;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        Point point = (Point) animation.getAnimatedValue();
        target.setX(point.x);
        target.setY(point.y);
    }
}
