package com.liangjing.lovetrajectoryanimation.view;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

import com.liangjing.lovetrajectoryanimation.util.BezierUtil;

/**
 * Created by liangjing on 2017/7/17.
 *  功能：Evaluator是属性动画中非常重要的一个东西，他根据输入的初始值和结束值以及一个进度比，
 *       那么就可以计算出每一个进度比下所要返回的值。
 */

public class BezierEvaluator implements TypeEvaluator<PointF> {

    //控制点１
    private PointF pointF1;
    //控制点２
    private PointF pointF2;

    public BezierEvaluator(PointF pointF1,PointF pointF2) {
        this.pointF1 = pointF1;
        this.pointF2 = pointF2;
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        PointF pointF = new PointF();//结果
        pointF = BezierUtil.CalculateBezierPointForCubic(fraction,startValue,pointF1,pointF2,endValue);
        return pointF;
    }

}
