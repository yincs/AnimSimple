package com.changs.sample.anim;

import android.app.Application;
import android.content.Context;

/**
 * Created by yincs on 2016/9/21.
 */

public class App extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
