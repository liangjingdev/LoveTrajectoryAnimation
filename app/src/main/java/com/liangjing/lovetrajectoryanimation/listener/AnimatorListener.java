package com.liangjing.lovetrajectoryanimation.listener;

import android.animation.Animator;
import android.view.View;

import com.liangjing.lovetrajectoryanimation.view.FavorLayout;

/**
 * Created by liangjing on 2017/7/18.
 * 功能：监听动画过程,处理动画逻辑(业务处理)
 */

public class AnimatorListener implements Animator.AnimatorListener {


    private View target;
    private FavorLayout relativeLayout;

    public AnimatorListener(FavorLayout relativeLayout , View target) {
        this.relativeLayout = relativeLayout;
        this.target = target;
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        //因为不停的add会导致子view数量只增不减,所以在view动画结束后需要将其remove掉
        relativeLayout.removeView((target));
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }


}
