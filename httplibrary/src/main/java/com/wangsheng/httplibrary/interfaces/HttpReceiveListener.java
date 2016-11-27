package com.wangsheng.httplibrary.interfaces;

import org.json.JSONObject;

/**
 * @description 描述:
 * @auther wangsheng on 16/4/6 22:04.
 * @email 公司邮箱:wangsheng@csii.com.cn  个人邮箱:jingbei110@163.com
 */
public interface HttpReceiveListener
{

    /**
     * 网络请求成功
     * @param requestCode 请求码
     * @param response 请求返回对象
     */
    void httpSuccess(final int requestCode, JSONObject response);

    /**
     * 网络请求失败
     * @param requestCode 请求码
     * @param error 失败信息
     * @param errorCode 失败码
     */
    void httpError(final int requestCode, String error, String errorCode);

    void httpResponse();

    /**
     * 文件下载成功
     * @param requestCode 请求码
     * @param filePath 文件路径
     */
    void downloadSuccess(final int requestCode, String filePath);

    /**
     * 文件下载进度
     * @param requestCode 请求码
     * @param bytesWritten 当前长度
     * @param contentLength 总长度
     * @param done 是否完成
     */
    void downloadProgress(final int requestCode, long bytesWritten, long contentLength, boolean done);

    /**
     * 文件上传进度
     * @param requestCode 请求码
     * @param bytesWritten 当前长度
     * @param contentLength 总长度
     * @param done 是否完成
     */
    void uploadProgress(final int requestCode, long bytesWritten, long contentLength, boolean done);

}
