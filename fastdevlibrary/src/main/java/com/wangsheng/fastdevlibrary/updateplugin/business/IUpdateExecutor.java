package com.wangsheng.fastdevlibrary.updateplugin.business;

/**
 */
public interface IUpdateExecutor {

    /**
     * check if is new version exist;
     */
    void check(UpdateWorker worker);

    /**
     * request download new version apk
     */
    void download(DownloadWorker worker);
}
