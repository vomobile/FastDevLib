package com.wangsheng.httplibrary.services;

/**
 * @description 描述:
 * @auther wangsheng on 16/4/6 22:06.
 * @email 公司邮箱:wangsheng@csii.com.cn  个人邮箱:jingbei110@163.com
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wangsheng.httplibrary.util.ConDict;
import com.wangsheng.httplibrary.interfaces.HttpReceiveListener;

import org.json.JSONException;
import org.json.JSONObject;


public class HttpBroadcastReceiver extends BroadcastReceiver
{

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

    public static final String[] ALLOWED_ACTIONS = {ACTION_SUCCESS,
            ACTION_ERROR, ACTION_DOWNLOAD, ACTION_PROGRESS, ACTION_PROGRESS_UPLOAD};


    private HttpReceiveListener httpReceiveListener;

    public HttpBroadcastReceiver() {
    }

    public HttpBroadcastReceiver(HttpReceiveListener httpReceiveListener)
    {
        this.httpReceiveListener = httpReceiveListener;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();

        // switch() doesn't support strings in older JDK.
        String response = intent.getStringExtra(RESPONSE_DATA);
        String errorCode = intent.getStringExtra(RESPONSE_ERROR_CODE);
        int requestCode = intent.getIntExtra(MInternetService.REQUEST_CODE, ConDict.RequestCode.defaultCode);
        if(ACTION_SUCCESS.equals(action)||ACTION_ERROR.equals(action)){
            httpReceiveListener.httpResponse();
        }
        if (ACTION_SUCCESS.equals(action)) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            httpReceiveListener.httpSuccess(requestCode,jsonObject);
        } else if (ACTION_ERROR.equals(action)) {
            httpReceiveListener.httpError(requestCode,response,errorCode);
        } else if (ACTION_DOWNLOAD.equals(action)) {
            httpReceiveListener.downloadSuccess(requestCode, intent.getStringExtra(DOWNLOAD_FILE));
        } else if (ACTION_PROGRESS.equals(action)) {
            httpReceiveListener.downloadProgress(requestCode,intent.getLongExtra(PROGRESS_CURRENT,0L),
                    intent.getLongExtra(PROGRESS_LENGTH,0L),intent.getBooleanExtra(PROGRESS_DONE,false));
        } else if (ACTION_PROGRESS_UPLOAD.equals(action)) {
            httpReceiveListener.uploadProgress(requestCode,intent.getLongExtra(PROGRESS_CURRENT,0L),
                    intent.getLongExtra(PROGRESS_LENGTH,0L),intent.getBooleanExtra(PROGRESS_DONE,false));
        }
    }
}
