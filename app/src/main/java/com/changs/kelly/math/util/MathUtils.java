package com.changs.kelly.math.util;


/**
 * Created by yincs on 2016/5/14.
 */
public class MathUtils {


    public static int bezier(int str, int end, int... c) {
        return 0;
    }

    public static int bezierThree(float t, int str, int p1, int p2, int end) {
        float f = 1 - t;
        return (int) (str * Math.pow(f, 3)
                + 3 * p1 * t * Math.pow(f, 2)
                + 3 * p2 * Math.pow(t, 2) * f
                + end * Math.pow(t, 3));
    }

    public static int bezierTwo(float t, int str, int p1, int end) {
        return (int) (Math.pow(1 - t, 2) * str
                + 2 * t * (1 - t) * p1
                + Math.pow(t, 2) * end);
    }

    public static int bezierOne(float t, int str, int end) {
        return (int) ((1 - t) * str + t * end);
    }

}
