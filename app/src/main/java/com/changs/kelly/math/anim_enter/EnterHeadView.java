package com.changs.kelly.math.anim_enter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.changs.kelly.math.R;
import com.changs.kelly.math.util.AnimFrameLayout;
import com.changs.kelly.math.util.AnimatorVisibleListener;
import com.changs.kelly.math.util.BezierEvaluator;
import com.changs.kelly.math.util.UpdateLocationListener;

import java.lang.ref.SoftReference;

/**
 * Created by Lenovo on 2016/8/9.
 */
public class EnterHeadView extends AnimFrameLayout<ImageView> {

    private View headView;

    public EnterHeadView(Context context) {
        this(context, null);
    }

    public EnterHeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EnterHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.layout_enter, this);
        headView = findViewById(R.id.container_head);
    }

    @Override
    protected View getAnimView() {
        ImageView imageView = getTempView();
        if (imageView == null) {
            imageView = new ImageView(getContext());
            tempViews.add(new SoftReference<>(imageView));
        }
        imageView.setImageResource(random.nextBoolean() ? R.mipmap.bubble_a : R.mipmap.bubble_c);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = params.height = dp2px(25 + random.nextInt(40));
        addView(imageView, 0, params);

        Log.d(TAG, "当前子view个数:" + getChildCount() + " 缓存view个数：" + tempViews.size());

        return imageView;
    }

    @Override
    public void start() {
        View animView = getAnimView();

        Point pointStr = new Point(halfWidth - headView.getMeasuredWidth() / 2, (int) headView.getY());
        Point pointEnd = new Point(halfWidth, 0);

        int x1 = random.nextInt(halfWidth);
        int x2 = random.nextInt(halfWidth);
        boolean boo = random.nextBoolean();
        if (boo) {
            x1 = x1 + halfWidth;
        } else {
            x2 = x2 + halfWidth;
        }

        Point point1 = new Point(x1, height / 4 * 2);
        Point point2 = new Point(x2, height / 4 * 2);

        BezierEvaluator evaluator = new BezierEvaluator(point1, point2);
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, pointStr, pointEnd);
        animator.addUpdateListener(new UpdateLocationListener(animView));
        animator.addUpdateListener(new UpdateAlphaListener(animView));
        animator.addListener(new AnimatorVisibleListener<>(this, animView));
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(1500);
        animator.setTarget(animator);
        animator.start();
    }

    @Override
    public void stop() {

    }


    private class UpdateAlphaListener implements ValueAnimator.AnimatorUpdateListener {
        private View target;

        public UpdateAlphaListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            final float animatedFraction = animation.getAnimatedFraction();
            if (animatedFraction > 0.8) {
                float alpha = 1 - animatedFraction;
                target.setAlpha(alpha / 0.2F);
            } else if (animatedFraction < 0.3) {
                target.setAlpha(animatedFraction / 0.3F);
            }
        }
    }
}
