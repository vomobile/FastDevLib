package com.wangsheng.fastdevapp;

import com.lzy.okgo.OkGo;
import com.wangsheng.fastdevlibrary.updateplugin.business.UpdateWorker;
import com.wangsheng.fastdevlibrary.updateplugin.model.CheckEntity;

import okhttp3.Response;

/**
 * Created by wangsheng on 16/11/30.
 *
 * @description
 * @See
 * @usage
 */

public class AppCheckWorker extends UpdateWorker {

    @Override
    protected String check(CheckEntity url) throws Exception {
        Response response = OkGo.get("https://mbl.jnbank.cc/pweb/SessionInit.do")//
                .tag(this)//
                .headers("aaa", "111")//
                .params("bbb", "222")
                .execute();
        String s = response.body().string();
//        response.close();

        return s;
    }


}
