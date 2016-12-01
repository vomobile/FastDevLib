package com.wangsheng.fastdevapp;

import com.lzy.okgo.callback.AbsCallback;

import okhttp3.Response;

/**
 * Created by wangsheng on 16/11/30.
 *
 * @description
 * @See
 * @usage
 */

public abstract class StrCallback extends AbsCallback<String> {
    @Override
    public String convertSuccess(Response response) throws Exception {
        String res = response.body().string();
        return res;
    }
}
