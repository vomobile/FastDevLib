package com.wangsheng.fastdevlibrary.updateplugin.business;

import com.wangsheng.fastdevlibrary.updateplugin.callback.UpdateCheckCB;
import com.wangsheng.fastdevlibrary.updateplugin.model.CheckEntity;
import com.wangsheng.fastdevlibrary.updateplugin.model.Update;
import com.wangsheng.fastdevlibrary.updateplugin.model.UpdateChecker;
import com.wangsheng.fastdevlibrary.updateplugin.model.UpdateParser;
import com.wangsheng.fastdevlibrary.updateplugin.util.HandlerUtil;
import com.wangsheng.fastdevlibrary.updateplugin.util.Recycler;

/**
 * The network task to check out whether or not a new version of apk is exist
 */
public abstract class UpdateWorker extends UnifiedWorker implements Runnable,Recycler.Recycleable {

    /**
     * To see {@link com.wangsheng.fastdevlibrary.updateplugin.UpdateConfig#url}
     */
//    protected String url;

    protected CheckEntity entity;
    /**
     * The instance of {@link com.wangsheng.fastdevlibrary.updateplugin.callback.DefaultCheckCB}
     */
    protected UpdateCheckCB checkCB;

    /**
     * set by {@link UpdateChecker(UpdateChecker)} or
     * {@link UpdateChecker(UpdateChecker)}<br>
     *     <br>
     *     according to instance {@link Update} to check out whether or not should be updated
     */
    protected UpdateChecker checker;
    /**
     * set by {@link com.wangsheng.fastdevlibrary.updateplugin.UpdateConfig#jsonParser(UpdateParser)]} or
     * {@link com.wangsheng.fastdevlibrary.updateplugin.UpdateBuilder#jsonParser(UpdateParser)}<br><br>
     *
     *     according to response data from url to create update instance
     */
    protected UpdateParser parser;

    public void setEntity(CheckEntity entity) {
        this.entity = entity;
    }

    public void setCheckCB(UpdateCheckCB checkCB) {
        this.checkCB = checkCB;
    }

    public void setParser(UpdateParser parser) {
        this.parser = parser;
    }

    public void setChecker(UpdateChecker checker) {
        this.checker = checker;
    }

    @Override
    public void run() {
        try {
            String response = check(entity);
            Update parse = parser.parse(response);
            if (parse == null) {
                throw new IllegalArgumentException("parse response to update failed by " + parser.getClass().getCanonicalName());
            }
            if (checker.check(parse)) {
                sendHasUpdate(parse);
            } else {
                sendNoUpdate();
            }
        } catch (HttpException he) {
            he.printStackTrace();
            sendOnErrorMsg(he.getCode(),he.getErrorMsg());
        } catch (Exception e) {
            e.printStackTrace();
            sendOnErrorMsg(-1,e.getMessage());
        } finally {
            setRunning(false);
        }
    }

    /**
     * access the url and get response data back
     * @param url The url to be accessed
     * @return response data from url
     * @throws Exception
     */
    protected abstract String check(CheckEntity url) throws Exception;

    private void sendHasUpdate(final Update update) {
        if (checkCB == null) return;
        HandlerUtil.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                checkCB.hasUpdate(update);
                Recycler.release(this);
            }
        });
    }

    private void sendNoUpdate() {
        if (checkCB == null) return;
        HandlerUtil.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                checkCB.noUpdate();
                Recycler.release(this);
            }
        });
    }

    private void sendOnErrorMsg(final int code, final String errorMsg) {
        if (checkCB == null) return;
        HandlerUtil.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                checkCB.onCheckError(code,errorMsg);
                Recycler.release(this);
            }
        });
    }

    @Override
    public void release() {
        this.checkCB = null;
        this.checker = null;
        this.parser = null;
    }
}
