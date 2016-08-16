package com.changs.kelly.math.anim_task;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.changs.kelly.math.R;
import com.changs.kelly.math.util.UiThreadExecutor;
import com.changs.kelly.math.util.AnimFrameLayout;
import com.changs.kelly.math.util.AnimatorVisibleListener;
import com.changs.kelly.math.util.BezierEvaluator;
import com.changs.kelly.math.util.UpdateLocationListener;

import java.lang.ref.SoftReference;

/**
 * Created by yincs on 2016/8/12.
 */
public class TaskDoneView extends AnimFrameLayout<ImageView> {

    private View ivShine;
    private View ivQueen;
    private View ivTitle;
    private View tvNickname;
    private View tvTaskName;


    private Animation animShine;
    private boolean start;

    public TaskDoneView(Context context) {
        this(context, null);
    }

    public TaskDoneView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TaskDoneView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_task_done, this);

        ivShine = findViewById(R.id.iv_shine);
        ivQueen = findViewById(R.id.iv_queen);
        ivTitle = findViewById(R.id.iv_title);
        tvNickname = findViewById(R.id.tv_nickname);
        tvTaskName = findViewById(R.id.tv_task_name);

        initAnim();
    }

    @Override
    protected View getAnimView() {
        ImageView imageView = getTempView();
        if (imageView == null) {
            imageView = new ImageView(getContext());
            tempViews.add(new SoftReference<>(imageView));
        }
        imageView.setRotation(random.nextInt(360));
        imageView.setImageResource(R.mipmap.star);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = params.height = 50 + random.nextInt(100);
        addView(imageView, params);

        Log.d(TAG, "当前子view个数:" + getChildCount() + " 缓存view个数：" + tempViews.size());
        return imageView;
    }

    private void initAnim() {
        ivTitle.setAlpha(0);
        ivTitle.setScaleX(0F);
        ivTitle.setScaleY(0F);

        ivQueen.setAlpha(0F);
        ivQueen.setScaleX(0F);
        ivQueen.setScaleY(0F);

        tvNickname.setAlpha(0F);
        tvNickname.setScaleX(0F);
        tvNickname.setScaleY(0F);

        tvTaskName.setAlpha(0F);
        tvTaskName.setScaleX(0F);
        tvTaskName.setScaleY(0F);

        ivShine.setAlpha(0F);
    }

    @Override
    public void start() {
        if (start)
            return;

        start = true;

        initAnim();

        setAlpha(1F);
        setScaleX(1F);
        setScaleY(1F);
        setTranslationY(0);

        runTask(new Runnable() {
            @Override
            public void run() {
                stop();
            }
        }, 10000);


        ivTitle.animate()
                .alpha(1F)
                .scaleX(1F).scaleY(1F)
                .setDuration(2000)
                .setInterpolator(new BounceInterpolator())
                .start();


        ivQueen.animate()
                .alpha(1F)
                .scaleX(1F).scaleY(1F)
                .setStartDelay(1000)
                .setDuration(2000)
                .setInterpolator(new BounceInterpolator())
                .start();


        tvNickname.animate()
                .alpha(1F)
                .scaleX(1F).scaleY(1F)
                .setStartDelay(1000)
                .setDuration(2000)
                .setInterpolator(new BounceInterpolator())
                .start();


        tvTaskName.animate()
                .alpha(1F)
                .scaleX(1F).scaleY(1F)
                .setStartDelay(2000)
                .setDuration(2000)
                .setInterpolator(new BounceInterpolator())
                .start();


        UiThreadExecutor.runTask(new Runnable() {


            @Override
            public void run() {
                ivShine.setAlpha(1F);
                animShine = AnimationUtils.loadAnimation(getContext(), R.anim.shine);
                ivShine.startAnimation(
                        animShine);
            }
        }, 2000);


        runTask(startStarOpt, 2000);
    }

    private Runnable startStarOpt = new Runnable() {
        @Override
        public void run() {
            startStar();
            runTask(this, 30);
        }
    };

    private void startStar() {
        final int x = halfWidth - 50 + random.nextInt(100);
        Point pointStr = new Point(x, halfHeight);
        Point pointEnd;
        final int y = random.nextInt(halfHeight / 2) + halfHeight / 2;
        Point point2 = pointEnd = new Point(random.nextInt(width), y);
        Point point1 = new Point(x, y);

        final View animView = getAnimView();
        BezierEvaluator bezierEvaluator = new BezierEvaluator(point1, point2);
        ValueAnimator animator = ValueAnimator.ofObject(bezierEvaluator, pointStr, pointEnd);
        animator.addUpdateListener(new UpdateLocationListener(animView));
        animator.addUpdateListener(new UpdateAlphaListener(animView));
        animator.addListener(new AnimatorVisibleListener<>(this, animView));
        animator.setDuration(3000);
        animator.start();
    }

    @Override
    public void stop() {
        UiThreadExecutor.cancelAll(TAG);
        animShine.cancel();

        animate()
                .alpha(0F)
                .scaleX(0F).scaleY(0F)
                .setDuration(2000)
                .translationY(-halfHeight)
                .start();

        runTask(new Runnable() {
            @Override
            public void run() {
                start = false;
            }
        }, 3000);
    }


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
            } else if (animatedFraction < 0.4) {
                target.setAlpha(animatedFraction / 0.4F);
            }
        }
    }
}
