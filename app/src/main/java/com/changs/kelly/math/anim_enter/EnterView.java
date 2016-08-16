package com.changs.kelly.math.anim_enter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.changs.kelly.math.util.AnimFrameLayout;
import com.changs.kelly.math.util.AnimatorVisibleListener;
import com.changs.kelly.math.util.BezierEvaluator;
import com.changs.kelly.math.util.UpdateLocationListener;

import java.lang.ref.SoftReference;

/**
 * Created by Lenovo on 2016/8/9.
 */
public class EnterView extends AnimFrameLayout<EnterHeadView> {

    private static final int ANIM_INTERVAL = 500;

    public EnterView(Context context) {
        this(context, null);
    }

    public EnterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EnterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected EnterHeadView getAnimView() {
        EnterHeadView headViewTemp = getTempView();

        if (headViewTemp == null) {
            headViewTemp = new EnterHeadView(getContext());
            tempViews.add(new SoftReference<>(headViewTemp));
        }
        addView(headViewTemp);
        Log.d(TAG, "当前子view个数:" + getChildCount() + " 缓存view个数：" + tempViews.size());
        return headViewTemp;
    }


    public void start() {
        EnterHeadView headView = getAnimView();

        final int offset = -random.nextInt(height / 2);
        Point pointStr = new Point(width / 2, height / 4 * 3 - height + offset);
        Point pointEnd = new Point(-width / 2, height / 4 * 3 - height + offset);
        Point point1 = new Point(0, offset);
        Point point2 = new Point(0, offset);

        BezierEvaluator evaluator = new BezierEvaluator(point1, point2);
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, pointStr, pointEnd);
        animator.addUpdateListener(new UpdateLocationListener(headView));
        animator.addUpdateListener(new ShowHeadViewListener(headView));
        animator.addUpdateListener(new UpdateAlphaListener(headView));
        animator.addListener(new AnimatorVisibleListener<>(this, headView));
        animator.setDuration(5000);
        animator.setTarget(headView);
        animator.start();
    }

    @Override
    public void stop() {

    }

    private class ShowHeadViewListener implements ValueAnimator.AnimatorUpdateListener {
        private EnterHeadView target;

        private long lastAnim = System.currentTimeMillis();

        public ShowHeadViewListener(EnterHeadView target) {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            final long fraction = System.currentTimeMillis();
            if (fraction - lastAnim > 500) {
                lastAnim = fraction;
                target.start();
            }

        }
    }

    private class UpdateAlphaListener implements ValueAnimator.AnimatorUpdateListener {
        private View target;

        public UpdateAlphaListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            final float animatedFraction = animation.getAnimatedFraction();
            if (animatedFraction < 0.2) {
                float alpha = animatedFraction / 0.2F;
                target.setAlpha(alpha);

                float scale = 0.5F + alpha / 2;
                target.setScaleX(scale);
                target.setScaleY(scale);
            } else if (animatedFraction > 0.8) {
                float alpha = 1 - animatedFraction;
                alpha = alpha / 0.2F;
                target.setAlpha(alpha);

                float scale = 0.5F + alpha / 2;
                target.setScaleX(scale);
                target.setScaleY(scale);
            }
        }
    }

}
