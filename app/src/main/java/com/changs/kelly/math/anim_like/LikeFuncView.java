package com.changs.kelly.math.anim_like;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.changs.kelly.math.R;
import com.changs.kelly.math.util.UiThreadExecutor;
import com.changs.kelly.math.util.AnimFrameLayout;
import com.changs.kelly.math.util.AnimatorVisibleListener;
import com.changs.kelly.math.util.BezierEvaluator;
import com.changs.kelly.math.util.UpdateLocationListener;

import java.lang.ref.SoftReference;

/**
 * Created by yincs on 2016/8/10.
 */
public class LikeFuncView extends AnimFrameLayout {

    private static final int[] animRes = {
            R.mipmap.like1,
            R.mipmap.like2,
            R.mipmap.like3,
            R.mipmap.like4,
            R.mipmap.like5,
            R.mipmap.like6,
    };

    private View containerHead;
    private AnimationDrawable likeAnim;
    private ImageView ivLike;

    public LikeFuncView(Context context) {
        this(context, null);
    }

    public LikeFuncView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LikeFuncView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.layout_like, this);

        containerHead = findViewById(R.id.container_head);
        ivLike = (ImageView) findViewById(R.id.iv_like);
        likeAnim = (AnimationDrawable) ivLike.getDrawable();

        initAnim();
    }


    @Override
    protected View getAnimView() {
        ImageView imageView = (ImageView) getTempView(ImageView.class);
        if (imageView == null) {
            imageView = new ImageView(getContext());
            tempViews.add(new SoftReference<>(imageView));
        }

        imageView.setImageResource(animRes[random.nextInt(animRes.length)]);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = params.height = dp2px(25 + random.nextInt(40));
        addView(imageView, 0, params);
        Log.d(TAG, "当前子view个数:" + getChildCount() + " 缓存view个数：" + tempViews.size());

        return imageView;
    }

    private void initAnim() {
        containerHead.setAlpha(0F);
        containerHead.setScaleX(0F);
        containerHead.setScaleY(0F);
        ivLike.setAlpha(0F);
        ivLike.setTranslationY(-dp2px(100));
    }

    @Override
    public void start() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setScaleY(1);
                setScaleX(1);
                setAlpha(1F);

                initAnim();

                containerHead.animate().alpha(1)
                        .scaleY(1).scaleX(1)
                        .setDuration(1000)
                        .setInterpolator(new BounceInterpolator())
                        .start();

                ivLike.animate().alpha(1)
                        .setDuration(1000)
                        .translationY(0)
                        .setInterpolator(new BounceInterpolator())
                        .setStartDelay(500)
                        .start();

                int startStarTime = 1500;
                runTask(new Runnable() {
                    @Override
                    public void run() {
                        likeAnim.start();
                    }
                }, startStarTime);

                runTask(startOpt, startStarTime);
            }
        });
    }

    @Override
    public void stop() {
        likeAnim.stop();
        UiThreadExecutor.cancelAll(TAG);

        animate().scaleX(0).scaleY(0).alpha(0).setDuration(1000).start();
    }

    private Runnable startOpt = new Runnable() {
        @Override
        public void run() {
            startStar();
            runTask(this, 100 + random.nextInt(300));
        }
    };

    private void startStar() {
        int tempX = (int) containerHead.getX() + random.nextInt(containerHead.getMeasuredWidth() / 2);
        int tempY = (int) containerHead.getY();


        Point pointStr = new Point(tempX, tempY);
        Point pointEnd = new Point(tempX, 0);

        boolean swap = random.nextBoolean();
        int p1x = halfWidth + halfWidth / 2;
        int p2x = halfWidth - halfWidth / 2;
        if (swap) {
            int temp = p1x;
            p1x = p2x;
            p2x = temp;
        }
        Point point1 = new Point(p1x, tempY / 3);
        Point point2 = new Point(p2x, tempY / 3);

        final View animView = getAnimView();
        BezierEvaluator evaluator = new BezierEvaluator(point1, point2);
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, pointStr, pointEnd);
        animator.addUpdateListener(new UpdateLocationListener(animView));
        animator.addUpdateListener(new UpdateListener(animView));
        animator.addListener(new AnimatorVisibleListener<>(this, animView));
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setTarget(animator);
        animator.setDuration(2000);
        animator.start();
        startText();
    }


    public void startText() {

        TextView textView;
        textView = (TextView) getTempView(TextView.class);
        if (textView == null) {
            textView = new TextView(getContext());
            tempViews.add(new SoftReference(textView));
        }

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(textView, 0, params);
        textView.setText(String.format("+%d", random.nextInt(10) + 1));
        textView.setTextColor(Color.WHITE);

        textView.setX(ivLike.getX() + ivLike.getMeasuredWidth() / 2);
        textView.setY(ivLike.getY());
        textView.animate().translationYBy(-dp2px(100)).setDuration(2000).setListener(new AnimatorVisibleListener<>(this, textView)).start();

        Log.d(TAG, "当前子view个数:" + getChildCount() + " 缓存view个数：" + tempViews.size());

    }


    private class UpdateListener implements ValueAnimator.AnimatorUpdateListener {
        private View target;

        public UpdateListener(View target) {
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
            } else if (animatedFraction > 0.5) {
                float alpha = 1 - animatedFraction;
                alpha = alpha / 0.5F;
                target.setAlpha(alpha);

                target.setScaleX(alpha);
                target.setScaleY(alpha);
            }
        }
    }

}
