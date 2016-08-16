package com.changs.kelly.math.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yincs on 2016/8/10.
 */
public abstract class AnimFrameLayout<T extends View> extends FrameLayout {
    protected String TAG = getClass().getSimpleName() + '@' + Integer.toHexString(hashCode());
    protected final List<SoftReference<T>> tempViews = new ArrayList<>();


    protected int width;
    protected int height;
    protected int halfWidth;
    protected int halfHeight;
    protected Random random = new Random();


    public AnimFrameLayout(Context context) {
        super(context);
    }

    public AnimFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getMeasuredWidth();
        halfWidth = width / 2;
        height = getMeasuredHeight();
        halfHeight = height / 2;
    }

    protected int dp2px(int dp) {
        return (int) (getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    protected void runTask(Runnable task, long delay) {
        UiThreadExecutor.runTask(TAG, task, delay);
    }

    protected T getTempView(Class clazz) {
        int size = tempViews.size();
        for (int i = 0; i < size; i++) {
            final SoftReference<T> reference = tempViews.get(i);
            final T headView = reference.get();
            if (headView == null) {
                tempViews.remove(i);
                i--;
                size--;
                continue;
            }
            if (clazz != null && headView.getClass() != clazz) {
                continue;
            }
            if (headView.getParent() == null) {
                return headView;
            }
        }
        return null;
    }

    protected T getTempView() {
        return getTempView(null);
    }

    protected abstract View getAnimView();

    public abstract void start();

    public abstract void stop();

    @Override
    public String toString() {
        return super.toString();
    }
}
