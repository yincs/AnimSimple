package com.changs.sample.anim.anim;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.changs.sample.anim.AppUtils;
import com.changs.sample.anim.R;
import com.changs.sample.anim.util.BezierEvaluator;
import com.changs.sample.anim.util.MathUtils;

import java.util.Random;

/**
 * Created by yincs on 2016/9/19.
 */
public class EnterAnim extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener {
    private static final String TAG = "EnterAnim";
    private static final int DURATION = 3800;

    private ViewGroup group;
    private View target;
    private TextView tvNickname;
    private ImageView ivHeadView;
    private Random random = new Random();
    private final int width = (int) AppUtils.getDimension(R.dimen.DIMEN_300PX);
    private BobbleAnim bobbleAnim1, bobbleAnim2, bobbleAnim3;

    public EnterAnim(ViewGroup group) {
        this.group = group;
        this.target = LayoutInflater.from(group.getContext()).inflate(R.layout.layout_head, group, false);
        this.tvNickname = (TextView) target.findViewById(R.id.tv_nickname);
        this.ivHeadView = (ImageView) target.findViewById(R.id.iv_head);

        bobbleAnim1 = new BobbleAnim(0.1F, width);
        bobbleAnim2 = new BobbleAnim(0.2F, -width);
        bobbleAnim3 = new BobbleAnim(0.3F, -width);

        addUpdateListener(this);
        setDuration(DURATION);
        setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public void setInfo(String nickName, Bitmap bitmap) {
        tvNickname.setText(nickName);
        ivHeadView.setImageBitmap(bitmap);
    }

    //起始点 x=屏幕右边，y=高度1/4~1/2
    //结束点 x=屏幕左边，y=高度1/2~3/4

    //中间点位置相同 x=屏幕*3/4 y=结束点y+屏幕高度*1/4
    public void start() {
        final int groupWidth = group.getWidth();
        final int groupHeight = group.getHeight();
        if (target.getParent() == null)
            group.addView(target);

        Point pointStr = new Point();
        Point pointEnd = new Point();
        Point point1 = new Point();
        Point point2 = new Point();

        pointStr.x = groupWidth;
        pointStr.y = random.nextInt(groupHeight / 4);

        pointEnd.x = 0 - width;
        pointEnd.y = groupHeight / 4 + random.nextInt(groupHeight / 4);

        point1.x = point2.x = groupWidth * 2 / 3;
        point1.y = point2.y = pointEnd.y + groupHeight / 4;

        BezierEvaluator bezierEvaluator = new BezierEvaluator(point1, point2);
        setObjectValues(pointStr, pointEnd);
        setEvaluator(bezierEvaluator);
        super.start();
    }


    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        Point point = (Point) animation.getAnimatedValue();
        target.setX(point.x);
        target.setY(point.y);

        final float fraction = animation.getAnimatedFraction();
        bobbleAnim1.updateLocation(fraction);
        bobbleAnim2.updateLocation(fraction);
        bobbleAnim3.updateLocation(fraction);

        if (fraction >= 1 && target.getParent() != null)
            group.removeView(target);
    }


    private class BobbleAnim extends ValueAnimator implements AnimatorUpdateListener {
        private ImageView imageView;
        private float offset;
        private int offsetWidth;
        private float fraction;

        public BobbleAnim(float offset, int offsetWidth) {
            this.offset = offset;
            this.offsetWidth = offsetWidth;
            addUpdateListener(this);
            imageView = new ImageView(group.getContext());
            imageView.setImageResource(R.mipmap.bubble_w);
            setFloatValues(0, 1);
            setDuration(1000);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            fraction = animation.getAnimatedFraction();
        }

        private int getStrX() {
            return (int) target.getX() + width / 4;
        }

        public void updateLocation(float progress) {
            if (progress < offset) {
                return;
            }
            if (progress >= 1) {
                if (imageView.getParent() != null)
                    group.removeView(imageView);
                return;
            }
            if (!isStarted()) {
                start();
                imageView.setRotation(random.nextInt(360));

                if (imageView.getParent() == null)
                    group.addView(imageView);
            }

            int x = MathUtils.bezierThree(fraction, getStrX(), getStrX() + offsetWidth, getStrX() - offsetWidth, getStrX());
            int y = MathUtils.bezierThree(fraction, (int) target.getY(), (int) target.getY() / 3, (int) target.getY() / 3, 0);

            if (fraction <= 0.4) {
                final float temp = fraction / 0.4F;
                imageView.setAlpha(temp);
                final float scale = 1 + (1 - temp) * 1F;
                imageView.setScaleX(scale);
                imageView.setScaleY(scale);
            }
            if (fraction >= .8) {
                final float temp = (1 - fraction) / 0.2F;
                imageView.setAlpha(temp);
            }

            imageView.setX(x);
            imageView.setY(y);
        }
    }

}
