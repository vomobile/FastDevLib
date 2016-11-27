package com.wangsheng.fastdevlibrary.updateplugin.model;

import com.wangsheng.fastdevlibrary.updateplugin.UpdateConfig;
import com.wangsheng.fastdevlibrary.updateplugin.util.InstallUtil;
import com.wangsheng.fastdevlibrary.updateplugin.util.UpdatePreference;

/**
 * Created by Administrator on 2016/6/20.
 */
public class DefaultChecker implements UpdateChecker {
    @Override
    public boolean check(Update update) {
        try {
            int curVersion = InstallUtil.getApkVersion(UpdateConfig.getConfig().getContext());
            if (update.getVersionCode() > curVersion &&
                    (update.isForced()||
                    !UpdatePreference.getIgnoreVersions().contains(String.valueOf(update.getVersionCode())))) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
