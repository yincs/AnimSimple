package com.changs.sample.anim;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.AnimRes;
import android.support.annotation.DimenRes;
import android.support.annotation.StringRes;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.lang.reflect.Method;

/**
 * Created by yincs on 2016/4/1.
 */
public class AppUtils {
    private static final boolean DEBUG = true;

    public static Context getContext() {
        return App.getContext();
    }

    public static String getString(@StringRes int resId) {
        return getContext().getString(resId);
    }

    public static String getString(@StringRes int resId, Object... formatArgs) {
        return getContext().getString(resId, formatArgs);
    }

    public static float getDimension(@DimenRes int id) {
        return getContext().getResources().getDimension(id);
    }

    public static Animation loadAnimation(@AnimRes int id) {
        return AnimationUtils.loadAnimation(getContext(), id);
    }

    /**
     * @return true 没网
     */
    public static boolean netNull() {
        ConnectivityManager conn = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return info == null || !info.isConnected() || info.getState() != NetworkInfo.State.CONNECTED;
    }

    public static boolean isWifi() {
        ConnectivityManager conn = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return info != null && info.getType() == ConnectivityManager.TYPE_WIFI;
    }


    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                result = rest == PackageManager.PERMISSION_GRANTED;
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }


    public static boolean isDebug() {
        return DEBUG;
    }
}
