package com.wangsheng.httplibrary.base;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import com.wangsheng.fastdevlibrary.base.FDLBaseActivity;
import com.wangsheng.fastdevlibrary.commonutils.LogUtils;
import com.wangsheng.fastdevlibrary.commonutils.ToastUtil;
import com.wangsheng.httplibrary.http.FormDataEntity;
import com.wangsheng.httplibrary.interfaces.HttpReceiveListener;
import com.wangsheng.httplibrary.services.HttpBroadcastReceiver;
import com.wangsheng.httplibrary.services.MInternetService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @csii activity的基础类  网络请求 继承项目的基础类{@link FDLBaseActivity}
 */
public class HttpBaseActivity extends FDLBaseActivity implements HttpReceiveListener {

    /**
     * 网络请求成功
     */
    public static final String ACTION_SUCCESS = "ACTION_SUCCESS";

    /**
     * 网络请求失败
     */
    public static final String ACTION_ERROR = "ACTION_ERROR";

    /**
     * 网络请求成功返回报文体
     */
    public static final String RESPONSE_DATA = "RESPONSE_DATA";

    /**
     * 网络请求失败code
     */
    public static final String RESPONSE_ERROR_CODE = "RESPONSE_ERROR_CODE";

    /**
     * 网络请求下载进度
     */
    public static final String ACTION_PROGRESS = "ACTION_PROGRESS";

    /**
     * 网络请求上传进度
     */
    public static final String ACTION_PROGRESS_UPLOAD = "ACTION_PROGRESS_UPLOAD";

    /**
     * 当前进度
     */
    public static final String PROGRESS_CURRENT = "PROGRESS_CURRENT";

    /**
     * 总长度
     */
    public static final String PROGRESS_LENGTH = "PROGRESS_LENGTH";

    /**
     * 文件下载
     */
    public static final String ACTION_DOWNLOAD = "ACTION_DOWNLOAD";

    /**
     * 文件路径
     */
    public static final String DOWNLOAD_FILE = "DOWNLOAD_FILE";

    /**
     * 下载是否结束
     */
    public static final String PROGRESS_DONE = "PROGRESS_DONE";

    /**
     * 不需要做错误提示的URL列表 请在项目的BaseActivity中添加 使用{@link #addNoDoneUrl(int)} 查看{@link #httpError(int, String, String)}
     */
    public static List<Integer> noDoneUrls;
//    new ArrayList<>(Arrays.asList(HttpHelper.IntegralExchangeCouponRequestCode,HttpHelper.BankAmericardQueryRequestCode,HttpHelper.SignRequestCode,HttpHelper.LoginRequestCode,HttpHelper.LogoutRequestCode));

    private HttpBroadcastReceiver broadcastReceiver;

    public HttpBaseActivity activity;
    final private static int LoginCode = 101;//登录RequestCode

    public View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        broadcastReceiver = new HttpBroadcastReceiver(this);
    }

    /**
     * 网络请求成功
     *
     * @param requestCode 请求码
     * @param response    请求返回对象
     */
    public void httpSuccess(final int requestCode, JSONObject response) {

    };

    /**
     * 网络请求失败
     *
     * @param requestCode 请求码
     * @param error       失败信息
     */
    public void httpError(int requestCode, String error, String errorCode) {
        //需要统一处理的错误请在这里
//        if (requestCode != HttpHelper.LoginRequestCode) {//登录要在activity中单独处理
//            if (errorCode.equals(ConDict.USER_TIMEOUT)) {//用户被签退
//                if(!isShowTimeOut&&!(activity instanceof LoginActivity)&&!(activity instanceof GestureLoginActivity)){
//                    isShowTimeOut = true;
//                    new CustomDialog.Builder(activity).setCancelable(false).setTitle("提示").setMessage("会话超时或在其它设备登录，请重新登录。")
//                            .setPositiveButton("重新登录",new DialogInterface.OnClickListener(){
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    isShowTimeOut = false;
//                                    jumpToMainAndLogin(2);
//                                }
//                            })
//                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            isShowTimeOut = false;
//                            jumpToMainAndLogin(1);
//                        }
//                    }).create(activity).show();
//                }
//            } else {
        //不做错误弹窗的报错
//                if (!DataUtil.isContain(noDoneUrls,requestCode)&&!"103".equals(errorCode)&&!"104".equals(errorCode)&&!"105".equals(errorCode)) {//如果没有在错误列表中，就弹出错误对话框，如果在，就在对应的activity中自己弹出错误
//                    mDialogFactory.showConfirmDialog("提示", error + "", true, ConfirmDialogFragment.ConfirmStyle.ONLY_CONFIRM, new ConfirmDialogFragment.ConfirmDialogListener() {
//                        @Override
//                        public void onPositiveClick() {
//
//                        }
//
//                        @Override
//                        public void onNegativeClick() {
//
//                        }
//                    });
//                }
//            }
//        }
    }

    @Override
    public void httpResponse() {

    }

    /**
     * 文件下载成功
     *
     * @param requestCode 请求码
     * @param filePath    文件路径
     */
    public void downloadSuccess(final int requestCode, String filePath) {

    };

    /**
     * 文件下载进度
     *
     * @param requestCode   请求码
     * @param bytesWritten  当前长度
     * @param contentLength 总长度
     * @param done          是否完成
     */
    public void downloadProgress(final int requestCode, long bytesWritten, long contentLength, boolean done) {

    };

    /**
     * 文件上传进度
     *
     * @param requestCode   请求码
     * @param bytesWritten  当前长度
     * @param contentLength 总长度
     * @param done          是否完成
     */
    public void uploadProgress(final int requestCode, long bytesWritten, long contentLength, boolean done) {

    };

    /**
     * Post 请求
     *
     * @param url         请求地址
     * @param requestCode 请求码
     * @param formData    请求参数
     */
    public void post(String url, int requestCode, ArrayList<FormDataEntity> formData) {
        String Url = null;
//        if(null!=aCache.getAsString(Constant.IsChangedService)&&Constant.Flag_True.equals(aCache.getAsString(Constant.IsChangedService))){
//            Constant.baseUrl = aCache.getAsString(Constant.CurrentServiceIpStr);
//            Url = Constant.baseUrl+"/mobile/";
//        }else{
            Url = HttpHelper.Url;
//        }
        if ("".equals(getNetWork(getApplicationContext()))) {
            ToastUtil.showLong("无网络,请检查您的网络");
            Intent broadcast = new Intent(activity.ACTION_ERROR);
            broadcast.putExtra(activity.RESPONSE_DATA, "无网络,请检查您的");
            broadcast.putExtra(activity.RESPONSE_ERROR_CODE, "105");
            broadcast.putExtra("REQUEST_CODE", requestCode);
            LocalBroadcastManager.getInstance(activity).sendBroadcast(broadcast);
        } else {
            Intent serviceCall = new Intent(this, MInternetService.class);
            serviceCall.putExtra(MInternetService.INTENT_URL, Url + url);
            serviceCall.putParcelableArrayListExtra(MInternetService.FORM_DATA, formData);
            serviceCall.putExtra(MInternetService.REQUEST_TAG, this.getClass().toString());
            serviceCall.putExtra(MInternetService.REQUEST_CODE, requestCode);
            serviceCall.putExtra(MInternetService.INTENT_METHOD, "POST");
            startService(serviceCall);
            LogUtils.logd("请求上传-----地址:" + HttpHelper.Url + url, formData.toString());
        }
    }

    /**
     * Post 请求
     *
     * @param url         请求地址
     * @param requestCode 请求码
     * @param formData    请求参数
     */
    public void postMULTIPART(String url, int requestCode, ArrayList<FormDataEntity> formData) {
        if ("".equals(getNetWork(getApplicationContext()))) {
            ToastUtil.showShort("无网络,请检查您的网络");
        } else {
            Intent serviceCall = new Intent(this, MInternetService.class);
            serviceCall.putExtra(MInternetService.INTENT_URL, HttpHelper.Url + url);
            serviceCall.putParcelableArrayListExtra(MInternetService.FORM_DATA, formData);
            serviceCall.putExtra(MInternetService.REQUEST_TAG, this.getClass().toString());
            serviceCall.putExtra(MInternetService.REQUEST_CODE, requestCode);
            serviceCall.putExtra(MInternetService.INTENT_METHOD, "MULTIPART");
            startService(serviceCall);
            LogUtils.logd("请求上传-----地址:" + HttpHelper.Url + url, formData.toString());
        }
    }

    /**
     * Post 请求 无参数
     *
     * @param url         请求地址
     * @param requestCode 请求码
     */
    public void post(String url, int requestCode) {
        String Url = null;
        //切换地址
//        if(null!=aCache.getAsString(Constant.IsChangedService)&&Constant.Flag_True.equals(aCache.getAsString(Constant.IsChangedService))){
//            Constant.baseUrl = aCache.getAsString(Constant.CurrentServiceIpStr);
//            Url = Constant.baseUrl+"/mobile/";
//        }else{
            Url = HttpHelper.Url;
//        }

        if ("".equals(getNetWork(getApplicationContext()))) {
            ToastUtil.showShort("无网络,请检查您的网络");
            Intent broadcast = new Intent(activity.ACTION_ERROR);
            broadcast.putExtra(activity.RESPONSE_DATA, "无网络,请检查您的");
            broadcast.putExtra(activity.RESPONSE_ERROR_CODE, "105");
            broadcast.putExtra("REQUEST_CODE", requestCode);
            LocalBroadcastManager.getInstance(activity).sendBroadcast(broadcast);
        } else {
            Intent serviceCall = new Intent(this, MInternetService.class);
            serviceCall.putExtra(MInternetService.INTENT_URL,Url + url);
            serviceCall.putExtra(MInternetService.REQUEST_TAG, this.getClass().toString());
            serviceCall.putExtra(MInternetService.REQUEST_CODE, requestCode);
            serviceCall.putExtra(MInternetService.INTENT_METHOD, "POST");
            startService(serviceCall);
        }
    }

    /**
     * GET 请求
     *
     * @param url         请求地址
     * @param requestCode 请求码
     * @param formData    请求参数
     */
    public void get(String url, int requestCode, ArrayList<FormDataEntity> formData) {
        Intent serviceCall = new Intent(this, MInternetService.class);
        serviceCall.putExtra(MInternetService.INTENT_URL, HttpHelper.Url + url);
        serviceCall.putParcelableArrayListExtra(MInternetService.FORM_DATA, formData);
        serviceCall.putExtra(MInternetService.REQUEST_TAG, this.getClass().toString());
        serviceCall.putExtra(MInternetService.REQUEST_CODE, requestCode);
        serviceCall.putExtra(MInternetService.INTENT_METHOD, "GET");
        startService(serviceCall);
    }

    /**
     * GET 请求 无参数
     *
     * @param url         请求地址
     * @param requestCode 请求码
     */
    public void get(String url, int requestCode) {
        Intent serviceCall = new Intent(this, MInternetService.class);
        serviceCall.putExtra(MInternetService.INTENT_URL, HttpHelper.Url + url);
        serviceCall.putExtra(MInternetService.REQUEST_TAG, this.getClass().toString());
        serviceCall.putExtra(MInternetService.REQUEST_CODE, requestCode);
        serviceCall.putExtra(MInternetService.INTENT_METHOD, "GET");
        startService(serviceCall);
    }

    /**
     * 获取图形验证码
     *
     * @param url         请求地址
     * @param requestCode 请求码
     */
    public void downloadVerifyPic(String url, int requestCode) {
        Intent serviceCall = new Intent(this, MInternetService.class);
        serviceCall.putExtra(MInternetService.INTENT_URL, HttpHelper.Url + url);
        serviceCall.putExtra(MInternetService.REQUEST_TAG, this.getClass().toString());
        serviceCall.putExtra(MInternetService.REQUEST_CODE, requestCode);
        serviceCall.putExtra(MInternetService.INTENT_METHOD, "DOWNLOAD");
        startService(serviceCall);
    }

    /**
     * 取消请求 默认取消整个activity的 所以tag是activity的name
     */
    public void cancelTag() {
        Intent serviceCall = new Intent(this, MInternetService.class);
        serviceCall.putExtra(MInternetService.CANCLE_REQUEST, "CANCEL_REQUEST");
        serviceCall.putExtra(MInternetService.REQUEST_TAG, this.getClass().toString());
        startService(serviceCall);
        Log.d("取消请求----->", this.getClass().toString());
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter iFilter = new IntentFilter();
        for (String action : HttpBroadcastReceiver.ALLOWED_ACTIONS) {
            iFilter.addAction(action);
        }
        iFilter.addCategory(Intent.CATEGORY_DEFAULT);
        LocalBroadcastManager.getInstance(activity).registerReceiver(broadcastReceiver, iFilter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != broadcastReceiver) {
            LocalBroadcastManager.getInstance(activity).unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void finish() {
        super.finish();
        cancelTag();
    }

    /**
     * @param context
     * 		上下文
     * @return "" 无网络 wifi 2G/3G/4G
     */
    public String getNetWork(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(null != networkInfo && networkInfo.isAvailable()) {
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return "wifi";
            }
            if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return "2G/3G/4G";
            }
        }
        return "";

    }

    /**
     * 添加不要公共处理的 请求码
     * @param urlCode
     */
    public void addNoDoneUrl(int urlCode){
        noDoneUrls.add(urlCode);
    }
}
