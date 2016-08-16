package com.changs.kelly.math.anim_like;

import android.content.Context;
import android.util.AttributeSet;

import com.changs.kelly.math.util.AnimFrameLayout;

import java.lang.ref.SoftReference;

/**
 * Created by yincs on 2016/8/10.
 */
public class LikeView extends AnimFrameLayout<LikeFuncView> {
    public LikeView(Context context) {
        this(context, null);
    }

    public LikeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LikeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected LikeFuncView getAnimView() {
        LikeFuncView likeView = getTempView();
        if (likeView == null) {
            likeView = new LikeFuncView(getContext());
            tempViews.add(new SoftReference<>(likeView));
        }

        addView(likeView, 0);
        return likeView;
    }

    @Override
    public void start() {
        final LikeFuncView animView = getAnimView();
        animView.start();
        final int x = -halfWidth + random.nextInt(width);
        final int y = -halfHeight + random.nextInt(height * 2 / 3);
        animView.setX(x);
        animView.setY(y);

        runTask(new Runnable() {
            @Override
            public void run() {
                animView.stop();
            }
        }, 6000);
        runTask(new Runnable() {
            @Override
            public void run() {
                removeView(animView);
            }
        }, 7000);


    }

    @Override
    public void stop() {

    }
}
