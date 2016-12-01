package com.wangsheng.fastdevapp;


import com.lzy.okgo.callback.AbsCallback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wangsheng on 16/11/30.
 *
 * @description
 * @See
 * @usage
 */

public abstract class JsonCallBackk<T> extends AbsCallback<T> {
    @Override
    public void onSuccess(T t, Call call, Response response) {

    }

    @Override
    public T convertSuccess(Response response) throws Exception {
        String str = response.body().string();
        return (T) str;
    }
}
