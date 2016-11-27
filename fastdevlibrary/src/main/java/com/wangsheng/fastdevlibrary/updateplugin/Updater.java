package com.wangsheng.fastdevlibrary.updateplugin;

import android.app.Activity;
import android.util.Log;

import com.wangsheng.fastdevlibrary.updateplugin.business.DownloadWorker;
import com.wangsheng.fastdevlibrary.updateplugin.business.IUpdateExecutor;
import com.wangsheng.fastdevlibrary.updateplugin.business.UpdateExecutor;
import com.wangsheng.fastdevlibrary.updateplugin.business.UpdateWorker;
import com.wangsheng.fastdevlibrary.updateplugin.callback.DefaultCheckCB;
import com.wangsheng.fastdevlibrary.updateplugin.callback.DefaultDownloadCB;
import com.wangsheng.fastdevlibrary.updateplugin.model.Update;


/**
 * @author lzh
 */
public class Updater {
    private static Updater updater;
    private IUpdateExecutor executor;

    private Updater() {
        executor = UpdateExecutor.getInstance();
    }
    public static Updater getInstance() {
        if (updater == null) {
            updater = new Updater();
        }
        return updater;
    }

    /**
     * check out whether or not there is a new version on internet
     * @param activity The activity who need to show update dialog
     * @param builder update builder that contained all config.
     */
    public void checkUpdate(Activity activity,UpdateBuilder builder) {
        UpdateConfig.getConfig().context(activity);
        // define a default callback to receive update event send by update task
        DefaultCheckCB checkCB = new DefaultCheckCB(activity);
        checkCB.setBuilder(builder);

        UpdateWorker checkWorker = builder.getCheckWorker();
        if (checkWorker.isRunning()) {
            Log.e("Updater","Already have a update task running");
            checkCB.onCheckError(-1,"Already have a update task running");
            return;
        }
        checkWorker.setEntity(builder.getCheckEntity());
        checkWorker.setParser(builder.getJsonParser());
        checkWorker.setChecker(builder.getUpdateChecker());
        checkWorker.setCheckCB(checkCB);

        executor.check(builder.getCheckWorker());
    }

    /**
     * Request to download apk.
     * @param activity The activity who need to show download and install dialog;
     * @param update update instance,should not be null;
     * @param builder update builder that contained all config;
     */
    public void downUpdate(Activity activity, Update update, UpdateBuilder builder) {
        UpdateConfig.getConfig().context(activity);
        // define a default download callback to receive callback from download task
        DefaultDownloadCB downloadCB = new DefaultDownloadCB(activity);
        downloadCB.setBuilder(builder);
        downloadCB.setUpdate(update);
        downloadCB.setDownloadCB(builder.getDownloadCB());

        DownloadWorker downloadWorker = builder.getDownloadWorker();
        if (downloadWorker.isRunning()) {
            Log.e("Updater","Already have a download task running");
            downloadCB.onUpdateError(-1,"Already have a download task running");
            return;
        }


        downloadWorker.setUrl(update.getUpdateUrl());
        downloadWorker.setDownloadCB(downloadCB);
        downloadWorker.setCacheFileName(builder.getFileCreator().create(update.getVersionName()));

        executor.download(downloadWorker);
    }

}
