package com.liangjing.lovetrajectoryanimation.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.liangjing.lovetrajectoryanimation.R;
import com.liangjing.lovetrajectoryanimation.listener.AnimatorListener;

import java.util.Random;
/**
 *
 *
 * 使用方法：
 *
 * 1、     <!-- 可自行设置 -->
 *         <com.liangjing.lovetrajectoryanimation.view.FavorLayout
 *         android:layout_width="match_parent"
 *         android:layout_height="match_parent"
 *         android:id="@+id/favorAnimation"
 *         android:background="#d2d2c9">
 *
 * 2、
 *      final FavorLayout favorLayout = (FavorLayout) findViewById(R.id.favorAnimation);
 *            favorLayout.setOnClickListener(new View.OnClickListener() {
 *                  @Override
 *                  public void onClick(View v) {
 *                    favorLayout.addHeart();
 *                  }
 *           });
 */


/**
 * Created by liangjing on 2017/7/17.
 * 实现：爱心气泡运动轨迹动画
 */
public class FavorLayout extends RelativeLayout {

    private int dHeight;//爱心的高度
    private int dWidth;//爱心的宽度

    //动画监听器
    private AnimatorListener animatorListener;

    private int mHeight;//FavorLayout的高度
    private int mWidth;//FavorLayout的宽度

    private Random random = new Random();//用于实现随机功能

    private LayoutParams lp;//用于管理子view在布局上的显示位置

    private Drawable[] drawables;//保存爱心气泡类型

    private LinearInterpolator line = new LinearInterpolator();//线性插值器
    private AccelerateInterpolator acc = new AccelerateInterpolator();//加速插值器
    private DecelerateInterpolator dce = new DecelerateInterpolator();//减速插值器
    private AccelerateDecelerateInterpolator accDec = new AccelerateDecelerateInterpolator();//先减速后加速插值器


    public FavorLayout(Context context) {
        super(context);
        init();
    }

    public FavorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FavorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        //初始化显示的爱心图片
        drawables = new Drawable[3];
        Drawable red = getResources().getDrawable(R.drawable.pl_red, null);
        Drawable yellow = getResources().getDrawable(R.drawable.pl_yellow, null);
        Drawable blue = getResources().getDrawable(R.drawable.pl_blue, null);
        drawables[0] = red;
        drawables[1] = yellow;
        drawables[2] = blue;

        //获取子图的宽高，用于后面的计算。由于这里几张图的大小都是一样的，所以只取一个进行测量即可。
        dHeight = red.getIntrinsicHeight();
        dWidth = red.getIntrinsicWidth();

        //让子view位于底部并且水平居中
        lp = new LayoutParams(dWidth, dHeight);
        lp.addRule(CENTER_HORIZONTAL, TRUE);
        lp.addRule(ALIGN_PARENT_BOTTOM, TRUE);

        //初始化插值器
        Interpolator[] interpolator = new Interpolator[4];
        interpolator[0] = line;
        interpolator[1] = acc;
        interpolator[2] = dce;
        interpolator[3] = accDec;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
    }

    /**
     * 功能：添加气泡样式并且启动动画效果
     */
    public void addHeart() {
        ImageView imagview = new ImageView(getContext());
        //随机选一个
        imagview.setImageDrawable(drawables[random.nextInt(3)]);
        imagview.setLayoutParams(lp);
        addView(imagview);

        Animator animator = getAnimator(imagview);
        animator.addListener(new AnimatorListener(this,imagview));
        animator.start();
    }

    /**
     * 功能：合并前俩种动画,做成最终的动画
     *
     * @param target
     * @return　返回最终合成的动画效果
     */
    private Animator getAnimator(View target) {
        AnimatorSet set = getEnterAnimator(target);
        ValueAnimator bezierValueAnimator = getBezierValueAnimator(target);
        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playSequentially(set);
        finalSet.playSequentially(set, bezierValueAnimator);
        finalSet.setTarget(target);
        return finalSet;
    }

    /**
     * 功能：利用数值发生器ValueAnimator来控制某一时刻爱心泡泡在Bezier曲线上的显示位置
     *
     * @param target
     * @return　返回路径动画效果
     */
    private ValueAnimator getBezierValueAnimator(View target) {
        //初始化一个贝塞尔曲线的计算器
        BezierEvaluator evaluator = new BezierEvaluator(getPoint(2), getPoint(1));
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, new PointF((mWidth - dWidth) / 2,
                mHeight - dHeight), new PointF(random.nextInt(getWidth()), 0));
        animator.addUpdateListener(new BezierListener(target));
        animator.setTarget(target);
        animator.setDuration(3000);
        return animator;
    }


    /**
     * 功能：完成透明度变化以及缩放的动画
     *
     * @param target view
     * @return　返回透明度变化以及缩放的动画效果
     */
    private AnimatorSet getEnterAnimator(View target) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 0.2f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, 0.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, 0.2f, 1f);
        AnimatorSet enter = new AnimatorSet();
        enter.setDuration(500);
        enter.setInterpolator(new LinearInterpolator());
        enter.playTogether(alpha, scaleX, scaleY);//三个动画效果共同执行
        return enter;
    }

    /**
     * 获取两个控制点的坐标
     *
     * @return
     */
    public PointF getPoint(int scale) {
        PointF pointF = new PointF();
        //减去100 是为了控制 x轴活动范围,看效果(随意)
        pointF.x = random.nextInt((mWidth - 100));
        //再Y轴上 为了确保第二个点 在第一个点之上,这里把Y分成了上下两半 这样动画效果好一些 也可以用其他方法
        pointF.y = random.nextInt((mHeight - 100)) / scale;
        return pointF;
    }

    private class BezierListener implements ValueAnimator.AnimatorUpdateListener {

        private View target;

        public BezierListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            //这里获取到贝塞尔曲线计算出来的坐标值，将其赋给view,这样就可以让爱心随着曲线走啦
            PointF pointF = (PointF) animation.getAnimatedValue();
            target.setX(pointF.x);
            target.setY(pointF.y);
            //这里顺便做一个alpha动画
            target.setAlpha(1 - animation.getAnimatedFraction());
        }
    }
}
