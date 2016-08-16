package com.changs.kelly.math.anim_win;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
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
 * Created by yincs on 2016/8/10.
 */
public class WinView extends AnimFrameLayout<ImageView> {
    private static final int[] animRes = {
            R.mipmap.win1,
            R.mipmap.win2,
            R.mipmap.win3,
            R.mipmap.win4,
            R.mipmap.win5,
            R.mipmap.win6,
            R.mipmap.win7,
            R.mipmap.win8,
    };

    private View containerHead;
    private View ivWin;
    private View tvNickname;
    private Animation animation;
    private boolean start;

    public WinView(Context context) {
        this(context, null);
    }

    public WinView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WinView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.layout_win, this);
        containerHead = findViewById(R.id.container_head);
        ivWin = findViewById(R.id.iv_win);
        tvNickname = findViewById(R.id.tv_nickname);

        initAnim();
    }

    @Override
    protected View getAnimView() {
        return null;
    }


    private void initAnim() {
        ivWin.setAlpha(0F);
        ivWin.setScaleX(0F);
        ivWin.setScaleY(0F);

        containerHead.setScaleX(0F);
        containerHead.setScaleY(0F);
        containerHead.setAlpha(0F);

        tvNickname.setAlpha(0);
        tvNickname.setScaleX(0F);
        tvNickname.setScaleY(0F);
    }

    @Override
    public void start() {
        if (start)
            return;

        start = true;

        initAnim();
        ivWin.animate()
                .alpha(1)
                .scaleX(1).scaleY(1)
                .setDuration(1500)
                .setInterpolator(new BounceInterpolator())
                .start();
        runTask(new Runnable() {
            @Override
            public void run() {
                ivWin.animate()
                        .alpha(0)
                        .setDuration(2000)
                        .setInterpolator(new AccelerateInterpolator())
                        .setStartDelay(0)
                        .start();
            }
        }, 1500);


        containerHead.animate() //enter
                .alpha(1)
                .scaleX(1).scaleY(1)
                .setDuration(2000)
                .setInterpolator(new AccelerateInterpolator())
                .start();

        runTask(new Runnable() {
            @Override
            public void run() {
                containerHead.startAnimation(
                        AnimationUtils.loadAnimation(getContext(), R.anim.scale_small_));
            }
        }, 1800);

        runTask(new Runnable() {
            @Override
            public void run() {
                containerHead.animate()
                        .scaleX(1.8F).scaleY(1.8F)
                        .alpha(0)
                        .setDuration(3000)
                        .start();
            }
        }, 2800);


        tvNickname.animate()
                .scaleX(1).scaleY(1)
                .setStartDelay(1800)
                .alpha(1)
                .setDuration(3000)
                .setInterpolator(new BounceInterpolator());

        runTask(new Runnable() {
            @Override
            public void run() {
                tvNickname.animate()
                        .scaleX(0).scaleY(0)
                        .setStartDelay(0)
                        .alpha(0)
                        .setDuration(2500)
                        .setInterpolator(new AccelerateInterpolator())
                        .start();
            }
        }, 3800);

        runTask(dropOpt, 1200);
        runTask(new Runnable() {
            @Override
            public void run() {
                stop();
            }
        }, 6000);
    }

    private Runnable dropOpt = new Runnable() {
        @Override
        public void run() {
            startDrop();
            runTask(this, 150);
        }
    };

    private void startDrop() {
        final int x = random.nextInt(width);
        final int y = random.nextInt(halfHeight / 3);
        Point pointStr = new Point(x, y);
        Point pointEnd = new Point(x, height);
        Point point1 = new Point(x, halfHeight);
        Point point2 = new Point(x, halfHeight);

        final View dropView = getDropView();
        BezierEvaluator evaluator = new BezierEvaluator(point1, point2);
        ValueAnimator dropAnim = ValueAnimator.ofObject(evaluator, pointStr, pointEnd);
        dropAnim.addUpdateListener(new UpdateLocationListener(dropView));
        dropAnim.addListener(new AnimatorVisibleListener<>(this, dropView));
        dropAnim.setInterpolator(new AccelerateInterpolator());
        dropAnim.setDuration(3000);
        dropAnim.start();
    }


    @Override
    public void stop() {
        UiThreadExecutor.cancelAll(TAG);

        runTask(new Runnable() {
            @Override
            public void run() {
                start = false;
            }
        }, 1000);
    }

    public boolean isStart() {
        return start;
    }

    protected View getDropView() {
         ImageView imageView = getTempView();
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
}
