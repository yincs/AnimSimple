package com.changs.sample.anim.anim;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yincs on 2016/9/19.
 */
public class AnimGroup extends FrameLayout {
    private static final String TAG = "AnimGroup";

    private List<EnterAnim> enterAnimList = new ArrayList<>();
    private List<LikeAnim> likeAnimArrayList = new ArrayList<>();
    private WinAnim winAnim;
    private TaskDone taskDone;


    public AnimGroup(Context context) {
        this(context, null);
    }

    public AnimGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }


    public void startEnterAnim(String nickName, Bitmap bitmap) {
        EnterAnim anim = null;
        for (EnterAnim aAnim : enterAnimList) {
            if (!aAnim.isStarted()) {
                anim = aAnim;
                break;
            }
        }
        if (anim == null) {
            anim = new EnterAnim(this);
            enterAnimList.add(anim);
        }
        anim.setInfo(nickName, bitmap);
        anim.start();
    }

    public void startLikeAnim(String nickName, Bitmap bitmap) {
        LikeAnim likeAnim = null;
        for (LikeAnim aAnim : likeAnimArrayList) {
            if (!aAnim.isStarted()) {
                likeAnim = aAnim;
                break;
            }
        }

        if (likeAnim == null) {
            likeAnim = new LikeAnim(this);
            likeAnimArrayList.add(likeAnim);
        }
        likeAnim.setInfo(nickName, bitmap);
        likeAnim.start();
    }

    /**
     * @return true success
     */
    public boolean startWinAnim(String nickName, Bitmap bitmap) {
        if (winAnim == null)
            winAnim = new WinAnim(this);
        if (winAnim.isStarted())
            return false;
        if (taskDone != null && taskDone.isStarted())
            return false;
        winAnim.setInfo(nickName, bitmap);
        winAnim.start();
        return true;
    }

    public boolean startTaskDoneAnim(String nickName, String taskName) {
        if (taskDone == null)
            taskDone = new TaskDone(this);
        if (taskDone.isStarted())
            return false;
        if (winAnim != null && winAnim.isStarted())
            return false;
        taskDone.setInfo(nickName, taskName);
        taskDone.start();
        return true;
    }
}
