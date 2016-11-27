package com.wangsheng.fastdevlibrary.commonutils.crash;

/**
 * @description 崩溃接口协议
 */

public interface ICrashProcess {
    void onException(Thread thread, Throwable exception) throws Exception;
}