package com.wangsheng.fastdevlibrary.base;

import android.content.Context;
import android.support.annotation.NonNull;

import com.wangsheng.fastdevlibrary.BuildConfig;
import com.wangsheng.fastdevlibrary.commonutils.LogUtils;
import com.wangsheng.fastdevlibrary.commonutils.SDcardUtil;
import com.wangsheng.fastdevlibrary.commonutils.ToastUtil;
import com.wangsheng.fastdevlibrary.commonutils.crash.CrashHandler;
import com.wangsheng.fastdevlibrary.commonutils.crash.DefaultCrashProcess;

/**
 * 全局配置
 * 1.初始化应用存储目录
 * 2.初始日志打印
 * 3.初始化全局异常捕获
 * 4.初始化toast
 * 如果以上在具体的项目中不需要或者不符合场景，请自行修改
 */
public final class BasicConfig {
    private Context mContext;
    private BasicConfig(Context context){ this.mContext = context; }
    private volatile static BasicConfig sBasicConfig;

    public static final BasicConfig getInstance(@NonNull Context context){
        if(null == sBasicConfig){
            synchronized (BasicConfig.class){
                if(null == sBasicConfig){
                    sBasicConfig = new BasicConfig(context.getApplicationContext());
                }
            }
        }
        return sBasicConfig;
    }

    /**
     * 默认配置
     */
    public void init(){
        initDir();
        initLog(BuildConfig.DEBUG);
        initExceptionHandler();
        initToast();
    }

    /**
     * 初始化SDCard缓存目录
     * <pre>默认根目录名称：当前包名</pre>
     * @return
     */
    public BasicConfig initDir(){
        initDir(mContext.getPackageName());
        return this;
    }

    /**
     * 初始化SDCard缓存目录
     * @param dirName 根目录名称
     * @return
     */
    public BasicConfig initDir(@NonNull String dirName){
        SDcardUtil.setRootDirName(dirName);
        SDcardUtil.initDir();
        return this;
    }

    /**
     * 默认异常信息处理
     * @return
     */
    public BasicConfig initExceptionHandler(){
        return initExceptionHandler(new DefaultCrashProcess(mContext));
    }
    /**
     * 自定义异常信息处理
     * @return
     */
    public BasicConfig initExceptionHandler(DefaultCrashProcess crashProcess){
        CrashHandler.getInstance(crashProcess);
        return this;
    }

    /**
     * 日志打印参数配置
     * @param isDebug true:打印全部日志，false:不打印日志
     * @return
     */
    public BasicConfig initLog(boolean isDebug){
        LogUtils.logInit(isDebug);
        return this;
    }

    /**
     * 初始化toast
     */
    public BasicConfig initToast(){
        ToastUtil.init(mContext.getApplicationContext());
        return this;
    }

}
