package com.changs.kelly.math.util;

import android.animation.TypeEvaluator;
import android.graphics.Point;

/**
 * Created by yincs on 2016/8/9.
 */
public class BezierEvaluator implements TypeEvaluator<Point> {
    private Point point1;
    private Point point2;

    private Point result = new Point();

    public BezierEvaluator(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    @Override
    public Point evaluate(float fraction, Point startValue, Point endValue) {
        result.x = MathUtils.bezierThree(fraction, startValue.x, point1.x, point2.x, endValue.x);
        result.y = MathUtils.bezierThree(fraction, startValue.y, point1.y, point2.y, endValue.y);
        return result;
    }
}
