package com.changs.sample.anim.anim;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.changs.sample.anim.AppUtils;
import com.changs.sample.anim.R;
import com.changs.sample.anim.util.MathUtils;
import com.changs.sample.anim.util.UiThreadExecutor;

import java.util.Random;

/**
 * Created by yincs on 2016/9/19.
 */
public class LikeAnim {
    private static final int DURATION = 5000;

    private static final int[] animRes = {
            R.mipmap.like1,
            R.mipmap.like2,
            R.mipmap.like3,
            R.mipmap.like4,
            R.mipmap.like5,
            R.mipmap.like6,
    };

    private final String TAG = getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    private ViewGroup group;
    private View target;
    private TextView tvNickname;
    private ImageView ivHeadView;
    private ImageView ivLike;
    private AnimationDrawable likeAnim;
    private Random random = new Random();
    private final int targetWidth = (int) AppUtils.getDimension(R.dimen.DIMEN_300PX);
    private final int targetHeight = (int) AppUtils.getDimension(R.dimen.DIMEN_300PX);
    private GiftAnim[] giftAnims = new GiftAnim[6];
    private boolean started;

    public LikeAnim(ViewGroup group) {
        this.group = group;
        this.target = LayoutInflater.from(group.getContext()).inflate(R.layout.layout_like1, group, false);
        this.tvNickname = (TextView) target.findViewById(R.id.tv_nickname);
        this.ivHeadView = (ImageView) target.findViewById(R.id.iv_head);
        this.ivLike = (ImageView) target.findViewById(R.id.iv_like);
        this.likeAnim = (AnimationDrawable) ivLike.getDrawable();

        for (int i = 0; i < giftAnims.length; i++) {
            giftAnims[i] = new GiftAnim(i % 2 == 0 ? targetWidth : -targetWidth);
        }
    }

    private void initAnim() {
        target.setScaleX(1F);
        target.setScaleY(1F);
        target.setAlpha(1F);

        ivHeadView.setAlpha(0F);
        ivHeadView.setScaleX(0F);
        ivHeadView.setScaleY(0F);
        tvNickname.setAlpha(0F);
        tvNickname.setScaleX(0F);
        tvNickname.setScaleY(0F);

        ivLike.setAlpha(0F);
        ivLike.setTranslationY(-500);
    }


    public void start() {
        started = true;

        if (target.getParent() == null)
            group.addView(target);
        initAnim();

        int height = group.getHeight();

        target.setX(random.nextInt(group.getWidth() - targetWidth));
        target.setY(height / 2 + random.nextInt(height / 2 - targetHeight));

        ivHeadView.animate().alpha(1)
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


        tvNickname.animate().alpha(1)
                .scaleY(1).scaleX(1)
                .setDuration(1000)
                .setStartDelay(500)
                .setInterpolator(new BounceInterpolator())
                .start();

        runTask(startLikeAnim, 1500);
        runTask(startGiftAnimOpt, 1500);
        runTask(stopOpt, 1500 + DURATION);
    }

    private Runnable stopOpt = new Runnable() {
        @Override
        public void run() {
            UiThreadExecutor.cancelAll(TAG);

            if (likeAnim.isRunning()) likeAnim.stop();
            target.animate()
                    .scaleX(0)
                    .scaleY(0)
                    .alpha(0)
                    .setDuration(1000)
                    .withEndAction(resetOpt)
                    .start();
        }
    };

    private Runnable startLikeAnim = new Runnable() {
        @Override
        public void run() {
            if (!likeAnim.isRunning()) likeAnim.start();
        }
    };

    private Runnable startGiftAnimOpt = new Runnable() {
        int order = 0;

        @Override
        public void run() {
            giftAnims[order++ % giftAnims.length].start();
            runTask(this, 100);
        }
    };

    private Runnable resetOpt = new Runnable() {
        @Override
        public void run() {
            started = false;
        }
    };

    public void setInfo(String nickName, Bitmap bitmap) {
        tvNickname.setText(nickName);
        ivHeadView.setImageBitmap(bitmap);
    }


    private void runTask(Runnable task, int delay) {
        UiThreadExecutor.runTask(TAG, task, delay);
    }

    public boolean isStarted() {
        return started;
    }


    private final class GiftAnim extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener {
        ImageView imageView;
        float fraction;
        int offset;

        public GiftAnim(int offset) {
            this.offset = offset;
            imageView = new ImageView(group.getContext());
            setFloatValues(0, 1);
            setDuration(1000);
            addUpdateListener(this);
        }

        @Override
        public void start() {
            if (isStarted()) {
                return;
            }

            if (imageView.getParent() == null)
                group.addView(imageView);
            imageView.setImageResource(animRes[random.nextInt(animRes.length)]);
            super.start();
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            fraction = animation.getAnimatedFraction();

            int x = MathUtils.bezierThree(fraction, getStrX(), getStrX() + offset, getStrX() - offset, getStrX());
            int y = MathUtils.bezierThree(fraction, (int) target.getY(), (int) target.getY() / 3, (int) target.getY() / 3, 0);

            imageView.setX(x);
            imageView.setY(y);

            if (fraction <= .4) {
                final float temp = fraction / 0.4F;
                imageView.setAlpha(temp);
            }
            if (fraction >= .8) {
                final float temp = (1 - fraction) / 0.2F;
                imageView.setAlpha(temp);
            }

            if (fraction >= 1 && imageView.getParent() != null) {
                group.removeView(imageView);
            }
        }


        private int getStrX() {
            return (int) target.getX() + target.getWidth() / 2;
        }
    }


}
