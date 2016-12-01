package com.wangsheng.fastdevapp;

import android.app.Application;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.store.PersistentCookieStore;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.wangsheng.fastdevlibrary.base.AppErrorActivity;
import com.wangsheng.fastdevlibrary.base.FDLBaseApplication;
import com.wangsheng.fastdevlibrary.commonutils.ToastUitl;
import com.wangsheng.fastdevlibrary.commonutils.ToastUtil;
import com.wangsheng.fastdevlibrary.updateplugin.UpdateConfig;
import com.wangsheng.fastdevlibrary.updateplugin.callback.UpdateCheckCB;
import com.wangsheng.fastdevlibrary.updateplugin.model.CheckEntity;
import com.wangsheng.fastdevlibrary.updateplugin.model.Update;
import com.wangsheng.fastdevlibrary.updateplugin.model.UpdateChecker;
import com.wangsheng.fastdevlibrary.updateplugin.model.UpdateParser;

import java.util.logging.Level;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

/**
 * Created by wangsheng on 16/11/21.
 *
 * @description
 * @See
 * @usage
 */

public class AppApplication extends FDLBaseApplication {
    private String apkFile;

    @Override
    public void onCreate() {
        super.onCreate();
//        CustomActivityOnCrash.setShowErrorDetails(false);
//        CustomActivityOnCrash.setDefaultErrorActivityDrawable(R.drawable.ic_empty_picture);
        CustomActivityOnCrash.setErrorActivityClass(AppErrorActivity.class);
        CustomActivityOnCrash.install(this);
        initOkGo(this);
        initUpdate();
    }

    /**
     * 初始化okgo
     *
     * @return
     */
    public void initOkGo(Application application) {

        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        headers.put("Connection", "Keep-Alive");    //header不支持中文
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpParams params = new HttpParams();
        params.put("_locale", "zh_CN");     //param支持中文,直接传,不要自己编码
        params.put("BankId", "9999");
        params.put("TermEquipment", "");
        params.put("DeviceName", "");
        params.put("SysVersion", "");
        params.put("LoginType", "P");
        //-----------------------------------------------------------------------------------//

        //必须调用初始化
        OkGo.init(application);

        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        //好处是全局参数统一,特定请求可以特别定制参数
        try {
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            OkGo.getInstance()

                    // 打开该调试开关,打印级别INFO,并不是异常,是为了显眼,不需要就不要加入该行
                    // 最后的true表示是否打印okgo的内部异常，一般打开方便调试错误
                    .debug("OkGo", Level.INFO, true)

                    //如果使用默认的 60秒,以下三行也不需要传
                    .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //全局的写入超时时间

                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy/
                    .setCacheMode(CacheMode.NO_CACHE)

                    //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)

                    //可以全局统一设置超时重连次数,默认为三次,那么最差的情况会请求4次(一次原始请求,三次重连请求),不需要可以设置为0
                    .setRetryCount(3)

                    //如果不想让框架管理cookie（或者叫session的保持）,以下不需要
//              .setCookieStore(new MemoryCookieStore())            //cookie使用内存缓存（app退出后，cookie消失）
                    .setCookieStore(new PersistentCookieStore())        //cookie持久化存储，如果cookie不过期，则一直有效

                    //可以设置https的证书,以下几种方案根据需要自己设置
                    .setCertificates()                                  //方法一：信任所有证书,不安全有风险
//              .setCertificates(new SafeTrustManager())            //方法二：自定义信任规则，校验服务端证书
//              .setCertificates(getAssets().open("srca.cer"))      //方法三：使用预埋证书，校验服务端证书（自签名证书）
//              //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
//               .setCertificates(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"))//

                    //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
//               .setHostnameVerifier(new SafeHostnameVerifier())

                    //可以添加全局拦截器，不需要就不要加入，错误写法直接导致任何回调不执行
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        return chain.proceed(chain.request());
//                    }
//                })

                    //这两行同上，不需要就不要加入
                    .addCommonHeaders(headers)  //设置全局公共头
                    .addCommonParams(params);   //设置全局公共参数

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUpdate() {
        apkFile = "http://apk.hiapk.com/web/api.do?qt=8051&id=721";
        // Application中对Config进行配置
        UpdateConfig.getConfig()
                .checkEntity(new CheckEntity().setUrl("https://mbl.jnbank.cc/pweb/SessionInit.do"))
                .jsonParser(new UpdateParser() {
                    @Override
                    public Update parse(String response) {
                        //这里处理接口返回的数据  塞入更新实体类中

                        // 此处模拟一个Update对象，传入接口返回的原始数据进去保存。
                        // 若用户需要自定义的时候。对于有额外参数。可从中获取并定制。
                        Update update = new Update("");
                        // 此apk包的更新时间
                        update.setUpdateTime(System.currentTimeMillis());
                        // 此apk包的下载地址
                        update.setUpdateUrl(apkFile);
                        // 此apk包的版本号
                        update.setVersionCode(2);
                        // 此apk包的版本名称
                        update.setVersionName("2.0");
                        // 此apk包的更新内容
                        update.setUpdateContent("测试更新");
                        // 此apk包是否为强制更新
                        update.setForced(false);
                        // 是否显示忽略此次版本更新
                        update.setIgnore(true);
                        return update;
                    }
                })
                // 此参数可不配置。配置在此作为全局的更新接口回调通知
                .checkCB(new UpdateCheckCB() {
                    @Override
                    public void hasUpdate(Update update) {

                    }

                    @Override
                    public void noUpdate() {
                        ToastUitl.showLong("没有更新");
                    }

                    @Override
                    public void onCheckError(int code, String errorMsg) {
                        ToastUtil.showLong("出错");
                    }

                    @Override
                    public void onUserCancel() {
                        ToastUitl.showLong("取消更新");
                    }

                    @Override
                    public void onCheckIgnore(Update update) {
                        ToastUitl.showLong("忽略");
                    }
                })
                .checkWorker(new AppCheckWorker())
                .updateChecker(new UpdateChecker() {
                    @Override
                    public boolean check(Update update) {
                        return true;
                    }
                });
    }

}
