package com.wangsheng.fastdevlibrary.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

public class FDLBaseApplication extends Application {
    private static FDLBaseApplication sFDLBaseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sFDLBaseApplication = this;
        BasicConfig.getInstance(sFDLBaseApplication).init();
    }

    public static Context getAppContext() {
        return sFDLBaseApplication;
    }

    public static Resources getAppResources() {
        return sFDLBaseApplication.getResources();
    }
}
