package com.changs.sample.anim.anim;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.changs.sample.anim.AppUtils;
import com.changs.sample.anim.R;
import com.changs.sample.anim.util.MathUtils;
import com.changs.sample.anim.util.UiThreadExecutor;

import java.util.Random;

/**
 * Created by yincs on 2016/9/20.
 */
public class WinAnim {
    private static final int DURATION = 5000;

    private final String TAG = getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    private ViewGroup group;
    private ViewGroup target;
    private TextView tvNickname;
    private ImageView ivHeadView;
    private Random random = new Random();
    private final int width = (int) AppUtils.getDimension(R.dimen.DIMEN_300PX);
    private final int offsetY = (int) AppUtils.getDimension(R.dimen.DIMEN_720PX);
    private boolean started;
    private StarAnim[] starAnims = new StarAnim[15 * 15];

    public WinAnim(ViewGroup group) {
        this.group = group;
        this.target = (ViewGroup) LayoutInflater.from(group.getContext()).inflate(R.layout.layout_win2, group, false);
        this.tvNickname = (TextView) target.findViewById(R.id.tv_nickname);
        this.ivHeadView = (ImageView) target.findViewById(R.id.iv_head);

        for (int i = 0; i < starAnims.length; i++) {
            starAnims[i] = new StarAnim();
        }
    }

    public void setInfo(String nickName, Bitmap bitmap) {
        tvNickname.setText(nickName);
        ivHeadView.setImageBitmap(bitmap);
    }

    public boolean isStarted() {
        return started;
    }

    private void initAnim() {

        target.setTranslationY(-offsetY);
    }

    public void start() {
        started = true;
        if (target.getParent() == null)
            group.addView(target);

        initAnim();
        target.animate()
                .translationY(0F)
                .setDuration(1000)
                .setInterpolator(new BounceInterpolator())
                .start();

        runTask(starOpt, 500);

        runTask(stopOpt, 1000 + DURATION);
    }

    private Runnable starOpt = new Runnable() {
        private int order;

        @Override
        public void run() {
            for (int i = 0; i < 6; i++) {
                starAnims[order++ % starAnims.length].start();
            }
            runTask(this, 80);
        }
    };

    private Runnable stopOpt = new Runnable() {
        @Override
        public void run() {
            UiThreadExecutor.cancelAll(TAG);
            target.animate()
                    .translationY(-offsetY)
                    .setStartDelay(1000)
                    .setDuration(500)
                    .setInterpolator(new AccelerateInterpolator())
                    .withEndAction(resetOpt)
                    .start();
        }
    };

    private Runnable resetOpt = new Runnable() {
        @Override
        public void run() {
            if (target.getParent() != null) {
                group.removeView(target);
            }
            started = false;
        }
    };

    private void runTask(Runnable task, int delay) {
        UiThreadExecutor.runTask(TAG, task, delay);
    }

    private int strX, strY;
    private ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    public void initStaXY() {
        if (strX == 0 && strY == 0) {
            strX = target.getWidth() / 2;
            strY = target.getHeight() / 2;
        }
    }

    private class StarAnim extends ValueAnimator implements TypeEvaluator<Point> {
        private ImageView imageView;
        private Point pointStr, pointEnd, point1;

        public StarAnim() {
            this.imageView = new ImageView(group.getContext());
            this.pointEnd = new Point();
            this.point1 = new Point();

            setDuration(2000);
        }

        @Override
        public void start() {
            if (pointStr == null) {
                initStaXY();
                this.pointStr = new Point(strX, strY);
            }
            if (isStarted())
                return;

            if (imageView.getParent() == null)
                target.addView(imageView, 0, params);
            imageView.setImageResource(R.mipmap.star);
            imageView.setRotation(random.nextInt(360));

            final int degree = random.nextInt(360);
            pointEnd.set(strX + (int) (width * Math.cos(degree)), strY + (int) (width * Math.sin(degree)));
            point1.set(strX, strY - width / 3);
            setObjectValues(pointStr, pointEnd);
            setEvaluator(this);
            super.start();
        }


        @Override
        public Point evaluate(float fraction, Point startValue, Point endValue) {
            final int x = MathUtils.bezierTwo(fraction, pointStr.x, point1.x, pointEnd.x);
            final int y = MathUtils.bezierTwo(fraction, pointStr.y, point1.y, pointEnd.y);

            imageView.setX(x);
            imageView.setY(y);

            if (fraction <= .5) {
                imageView.setAlpha(0F);
            } else if (fraction <= .7) {
                imageView.setAlpha((fraction - .5F) / .2F);
            } else if (fraction > .8) {
                imageView.setAlpha((1 - fraction) / .2F);
            }
            imageView.setRotation(imageView.getRotation() + 3.6F);

            if (fraction >= 1 && imageView.getParent() != null)
                group.removeView(imageView);
            return null;
        }
    }
}
