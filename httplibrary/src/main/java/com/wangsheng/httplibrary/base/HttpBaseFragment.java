package com.wangsheng.httplibrary.base;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;

import com.wangsheng.httplibrary.interfaces.HttpReceiveListener;
import com.wangsheng.httplibrary.services.HttpBroadcastReceiver;

import org.json.JSONObject;


/**
 * @description 描述:
 * @auther wangsheng on 16/3/21 14:22.
 * @email 公司邮箱:wangsheng@csii.com.cn  个人邮箱:jingbei110@163.com
 */
public class HttpBaseFragment extends Fragment implements HttpReceiveListener {
    public HttpBaseActivity activity;
    private HttpBroadcastReceiver broadcastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        broadcastReceiver = new HttpBroadcastReceiver(this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (HttpBaseActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter iFilter = new IntentFilter();
        for (String action : HttpBroadcastReceiver.ALLOWED_ACTIONS) {
            iFilter.addAction(action);
        }
        iFilter.addCategory(Intent.CATEGORY_DEFAULT);
        LocalBroadcastManager.getInstance(activity).registerReceiver(broadcastReceiver, iFilter);
    }


    @Override
    public void onPause() {
        super.onPause();
        if(null!=broadcastReceiver) {
            LocalBroadcastManager.getInstance(activity).unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity.cancelTag();
    }

    @Override
    public void httpSuccess(int requestCode, JSONObject response) {

    }

    @Override
    public void httpError(int requestCode, String error, String errorCode) {

    }

    @Override
    public void httpResponse() {

    }

    @Override
    public void downloadSuccess(int requestCode, String filePath) {

    }

    @Override
    public void downloadProgress(int requestCode, long bytesWritten, long contentLength, boolean done) {

    }

    @Override
    public void uploadProgress(int requestCode, long bytesWritten, long contentLength, boolean done) {

    }
}
