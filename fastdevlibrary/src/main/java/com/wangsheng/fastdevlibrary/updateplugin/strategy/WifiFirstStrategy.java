package com.wangsheng.fastdevlibrary.updateplugin.strategy;


import com.wangsheng.fastdevlibrary.updateplugin.model.Update;
import com.wangsheng.fastdevlibrary.updateplugin.util.NetworkUtil;

/**
 * @author Administrator
 */
public class WifiFirstStrategy implements UpdateStrategy {

    boolean isWifi;

    @Override
    public boolean isShowUpdateDialog(Update update) {
        isWifi = NetworkUtil.isConnectedByWifi();
        return !isWifi;
    }

    @Override
    public boolean isAutoInstall() {
        return !isWifi;
    }

    @Override
    public boolean isShowDownloadDialog() {
        return !isWifi;
    }
}
