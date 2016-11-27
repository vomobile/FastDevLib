package com.wangsheng.httplibrary.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.wangsheng.fastdevlibrary.commonutils.LogUtils;
import com.wangsheng.httplibrary.base.HttpBaseActivity;
import com.wangsheng.httplibrary.http.CookiesManager;
import com.wangsheng.httplibrary.http.FileFormDataEntity;
import com.wangsheng.httplibrary.http.FormDataEntity;
import com.wangsheng.httplibrary.http.HttpsUtil;
import com.wangsheng.httplibrary.http.ProgressHelper;
import com.wangsheng.httplibrary.http.ProgressRequestListener;
import com.wangsheng.httplibrary.http.ProgressResponseBody;
import com.wangsheng.httplibrary.http.ProgressResponseListener;
import com.wangsheng.httplibrary.util.ConDict;
import com.wangsheng.httplibrary.util.FileUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @注明 相对于原版修复了bug优化了一些东西 比如下载图形验证码保存图片的bug、信任https证书、多种请求情况的完善
 * @说明 广播安全性问题之前是用的方法是自定义广播发送和接收权限，但是这个方式有个弊端是权限是一串字符容易被反编译查看到，所以安全性低;
 *       使用LocalBroadcastManager的好处是发送的广播只在本进程也就是本应用中有效
 */
public class MInternetService extends IntentService {

    public static final String LOG_TAG = "MInternetService:";

    /**
     * 请求url
     */
    public static final String INTENT_URL = "INTENT_URL";

    /**
     * 请求模式 GET ？POST
     */
    public static final String INTENT_METHOD = "INTENT_METHOD";

    /**
     * 表单数据
     */
    public static final String FORM_DATA = "FORM_DATA";

    /**
     * 请求Tag 用于标识同一页面同时请求以便于一起取消
     */
    public static final String REQUEST_TAG = "REQUEST_TAG";

    /**
     * 请求码，用于标识回调的是哪一个请求，请求码统一定义，不重复
     */
    public static final String REQUEST_CODE = "REQUEST_CODE";

    /**
     * 上传请求，是否需要上传进度
     */
    public static final String UPLOAD_PROGRESS = "UPLOAD_PROGRESS";

    /**
     * 上传请求，是否需要上传进度
     */
    public static final String CANCLE_REQUEST = "CANCLE_REQUEST";

    private OkHttpClient aClient = null;

    public MInternetService() {
        super("MInternetService");
    }

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "onCreate()");
        aClient = setClient(60, aClient);
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(LOG_TAG, "onStart()");
        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        String requestTag = "";
        if (intent != null && intent.hasExtra(CANCLE_REQUEST)) {
            cancelTag(intent.getStringExtra(REQUEST_TAG));
            return;
        }
        final LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        //初始化公共参数用法
        List<FormDataEntity> list = new FormDataEntity.FormListBuilder().addFormData("_locale", "zh_CN")
                .addFormData("BankId", ConDict.BankId)
                .addFormData("LoginType", "P").build();
        if (intent != null && intent.hasExtra(INTENT_URL)) {
            if (intent.hasExtra(REQUEST_TAG)) {
                requestTag = intent.getStringExtra(REQUEST_TAG);
            }

            Request.Builder requestBuilder = new Request.Builder();
            //tag
            if (!"".equals(requestTag)) {
                requestBuilder.tag(requestTag);
            }
            if (intent.hasExtra(FORM_DATA)) {
                List<FormDataEntity> extraList = intent.getParcelableArrayListExtra(FORM_DATA);
                list.addAll(extraList);
            }
            String intentMethod = intent.getStringExtra(INTENT_METHOD);
            final String intnetUrl = intent.getStringExtra(INTENT_URL);
            //信任https证书
            setCertificates();
            if ("POST".equals(intentMethod)) {
                Log.d(LOG_TAG, "POST");
                try {
                    requestBuilder.url(intnetUrl);
                    RequestBody requestBody = createRequestBody(list);
                    requestBuilder.post(requestBody);
                } catch (IllegalArgumentException e) {
                    Intent broadcast = new Intent(HttpBaseActivity.ACTION_ERROR);
                    broadcast.putExtra(HttpBaseActivity.RESPONSE_DATA, "不支持的URL,请去切换地址页更换服务" + e.toString());
                    broadcast.putExtra(HttpBaseActivity.RESPONSE_ERROR_CODE, "1");
                    broadcast.putExtra(REQUEST_CODE, intent.getIntExtra(REQUEST_CODE, ConDict.RequestCode.defaultCode));
                    localBroadcastManager.sendBroadcast(broadcast);
                    return;
                }
            } else if ("GET".endsWith(intentMethod)) {
                Log.d(LOG_TAG, "GET");
                requestBuilder.url(createGetRequestUrl(list, intnetUrl));
                requestBuilder.get();
            } else if ("MULTIPART".endsWith(intentMethod)) {
                Log.d(LOG_TAG, "MULTIPART");
                //url
                requestBuilder.url(intnetUrl);
                RequestBody requestBody = createMultipartRequestBody(list);
                if (intent.getBooleanExtra(UPLOAD_PROGRESS, false)) {
                    requestBody = ProgressHelper.addProgressRequestListener(requestBody, new ProgressRequestListener() {

                        @Override
                        public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                            Intent broadcast = new Intent(HttpBaseActivity.ACTION_PROGRESS_UPLOAD);
                            broadcast.putExtra(HttpBaseActivity.PROGRESS_CURRENT, bytesWritten);
                            broadcast.putExtra(HttpBaseActivity.PROGRESS_LENGTH, contentLength);
                            broadcast.putExtra(HttpBaseActivity.PROGRESS_DONE, done);
                            broadcast.putExtra(REQUEST_CODE, intent.getIntExtra(REQUEST_CODE, ConDict.RequestCode.defaultCode));
                            localBroadcastManager.sendBroadcast(broadcast);
                        }
                    });
                }
                //url
            } else if ("DOWNLOAD".endsWith(intentMethod)) {
                Log.d(LOG_TAG, "DOWNLOAD");
                //文件下载块
                requestBuilder.url(createGetRequestUrl(list, intnetUrl));
                requestBuilder.get();
                OkHttpClient httpClient = ProgressHelper.addProgressResponseListener(aClient, new ProgressResponseListener() {


                    @Override
                    public void onResponseProgress(long bytesRead, long contentLength, boolean done) {
                        Intent broadcast = new Intent(HttpBaseActivity.ACTION_PROGRESS);
                        broadcast.putExtra(HttpBaseActivity.PROGRESS_CURRENT, bytesRead);
                        broadcast.putExtra(HttpBaseActivity.PROGRESS_LENGTH, contentLength);
                        broadcast.putExtra(HttpBaseActivity.PROGRESS_DONE, done);
                        broadcast.putExtra(REQUEST_CODE, intent.getIntExtra(REQUEST_CODE, ConDict.RequestCode.defaultCode));
                        localBroadcastManager.sendBroadcast(broadcast);
                    }
                });
                httpClient.newCall(requestBuilder.build()).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Intent broadcast = new Intent(HttpBaseActivity.ACTION_ERROR);
                        broadcast.putExtra(HttpBaseActivity.RESPONSE_DATA, "出错啦");
                        broadcast.putExtra(HttpBaseActivity.RESPONSE_ERROR_CODE, "1");
                        broadcast.putExtra(REQUEST_CODE, intent.getIntExtra(REQUEST_CODE, ConDict.RequestCode.defaultCode));
                        sendBroadcast(broadcast, "com.android.permission.receiver_tailong");
                        Log.e(LOG_TAG, "onFailure", e);
                    }

                    @Override
                    public void onResponse(Call call, Response response)
                            throws IOException {
                        InputStream is = null;
                        try {
                            is = response.body().byteStream();
                            String contentDisposition = response.header("Content-Disposition");
                            String fileName = "";
                            if (null == contentDisposition || "".endsWith(contentDisposition)) {
                                String url = intnetUrl;
//								fileName = url.substring(url.lastIndexOf("/")+1);
                                fileName = "gentoken.jpg";
                            } else {
                                fileName = contentDisposition.substring(contentDisposition.lastIndexOf("filename=") + 1);
                            }
//							String filePath = FileUtils.saveTempFile(is,fileName);
                            byte[] bytes = FileUtils.getBytes(is);
                            String filePath = FileUtils.writeData(bytes, fileName);
                            Intent broadcast = new Intent(HttpBaseActivity.ACTION_DOWNLOAD);
                            broadcast.putExtra(HttpBaseActivity.DOWNLOAD_FILE, filePath);
                            broadcast.putExtra(REQUEST_CODE, intent.getIntExtra(REQUEST_CODE, ConDict.RequestCode.defaultCode));
                            localBroadcastManager.sendBroadcast(broadcast);
                        } catch (Exception e) {
                        } finally {
                            if (is != null) {
                                is.close();
                            }
                        }
                    }
                });
                return;
            }
            Request request = requestBuilder.build();
            Call call;
            //上传文件的单独设置超时时间
//			if(HttpHelper.FileUploadRequestCode==intent.getIntExtra(REQUEST_CODE, Constant.RequestCode.defaultCode)){
//				OkHttpClient okHttpClient = setClient(60,getCloneClient(aClient));
//				call = okHttpClient.newCall(request);
//			}else{
            call = aClient.newCall(request);
//			}
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Intent broadcast = new Intent(HttpBaseActivity.ACTION_ERROR);
                    broadcast.putExtra(HttpBaseActivity.RESPONSE_DATA, "无法连接服务器,请检查您的网络");
                    broadcast.putExtra(HttpBaseActivity.RESPONSE_ERROR_CODE, "101");
                    broadcast.putExtra(REQUEST_CODE, intent.getIntExtra(REQUEST_CODE, ConDict.RequestCode.defaultCode));
                    localBroadcastManager.sendBroadcast(broadcast);
                    LogUtils.loge(LOG_TAG, "无法连接服务器,请检查您的网络:" + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response)
                        throws IOException {
                    if (200 != response.code()) {
                        Intent broadcast = new Intent(HttpBaseActivity.ACTION_ERROR);
                        broadcast.putExtra(HttpBaseActivity.RESPONSE_DATA, "连接服务器出错");
                        broadcast.putExtra(HttpBaseActivity.RESPONSE_ERROR_CODE, "102");
                        broadcast.putExtra(REQUEST_CODE, intent.getIntExtra(REQUEST_CODE, ConDict.RequestCode.defaultCode));
                        localBroadcastManager.sendBroadcast(broadcast);
                        LogUtils.loge(LOG_TAG, "连接服务器出错:" + response.code());
                        return;
                    }
                    String responseBody = response.body().string();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseBody);
                    } catch (Exception ex) {
                        LogUtils.loge("MInternetService", "接口" + intnetUrl + "----服务端数据格式错误");
                    }

                    if (null == jsonObject) {
                        //有一种情况是用户被另一个用户签退后，第一个请求返回的数据为空，服务端暂时未找到出错原因，
                        //所以客户端抛出这种错误后不给客户提示 这不是解决办法  因为超时后第一个请求返回为空的话会有问题
                        Intent broadcast = new Intent(HttpBaseActivity.ACTION_ERROR);
                        broadcast.putExtra(HttpBaseActivity.RESPONSE_DATA, "服务端返回为空");
                        broadcast.putExtra(HttpBaseActivity.RESPONSE_ERROR_CODE, "103");
                        broadcast.putExtra(REQUEST_CODE, intent.getIntExtra(REQUEST_CODE, ConDict.RequestCode.defaultCode));
                        sendBroadcast(broadcast, "com.android.permission.receiver_tailong");
                        LogUtils.loge(LOG_TAG, "服务端返回为空" + responseBody);
                        return;
                    }
                    JSONObject resultMap = jsonObject.optJSONObject("resultMap");
                    JSONArray jsonError = jsonObject.optJSONArray("jsonError");
                    if (null != resultMap) {
                        Intent broadcast = new Intent(HttpBaseActivity.ACTION_SUCCESS);
                        broadcast.putExtra(HttpBaseActivity.RESPONSE_DATA, resultMap.toString());
                        broadcast.putExtra(REQUEST_CODE, intent.getIntExtra(REQUEST_CODE, ConDict.RequestCode.defaultCode));
                        localBroadcastManager.sendBroadcast(broadcast);
                        LogUtils.logd(LOG_TAG, "服务端返回----地址:" + intnetUrl + ":" + resultMap.toString());
                    } else if (null != jsonError) {
                        if (jsonError.length() > 0) {
                            Intent broadcast = new Intent(HttpBaseActivity.ACTION_ERROR);
                            broadcast.putExtra(HttpBaseActivity.RESPONSE_DATA, jsonError.optJSONObject(0).optString("_exceptionMessage"));
                            broadcast.putExtra(HttpBaseActivity.RESPONSE_ERROR_CODE, jsonError.optJSONObject(0).optString("_exceptionMessageCode"));
                            broadcast.putExtra(REQUEST_CODE, intent.getIntExtra(REQUEST_CODE, ConDict.RequestCode.defaultCode));
                            localBroadcastManager.sendBroadcast(broadcast);
                            LogUtils.loge(LOG_TAG, "出错了----地址:" + intnetUrl + ":" + jsonError.toString());
                        }
                    } else {
                        Intent broadcast = new Intent(HttpBaseActivity.ACTION_ERROR);
                        broadcast.putExtra(HttpBaseActivity.RESPONSE_DATA, "返回数据为空");
                        broadcast.putExtra(HttpBaseActivity.RESPONSE_ERROR_CODE, "104");
                        broadcast.putExtra(REQUEST_CODE, intent.getIntExtra(REQUEST_CODE, ConDict.RequestCode.defaultCode));
                        localBroadcastManager.sendBroadcast(broadcast);
                        LogUtils.loge(LOG_TAG, "返回数据为空----地址:" + intnetUrl);
                    }
                    Log.d(LOG_TAG, "onResponse");
                }
            });
        }
    }

    /**
     * 创建post请求的报文参数
     *
     * @param formList 请求参数
     * @return RequestBody
     */
//    private RequestBody createRequestBody(final List<FormDataEntity> formList) {
//        List<Map<String,String>> list = new ArrayList<>();
//        FormBody.Builder builder = new FormBody.Builder();
//        for (FormDataEntity formDataEntity : formList) {
////            builder.add(formDataEntity.getFormName(), formDataEntity.getFormValue());
//            Map<String,String> map = new HashMap<String, String>();
//            map.put(formDataEntity.getFormName(),formDataEntity.getFormValue());
//            list.add(map);
//        }
//        try {
//            builder.add("AesList", AesUtil.encrypt("key",JSON.toJSONString(list).getBytes()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return builder.build();
//    }

    /**
     * 创建post请求的报文参数
     *
     * @param formList 请求参数
     * @return RequestBody
     */
    private RequestBody createRequestBody(final List<FormDataEntity> formList) {
        FormBody.Builder builder = new FormBody.Builder();
        for (FormDataEntity formDataEntity : formList) {
            builder.add(formDataEntity.getFormName(), formDataEntity.getFormValue());
        }
        return builder.build();
    }

    /**
     * 创建post请求的报文参数
     *
     * @param formList 请求参数
     * @return RequestBody
     */
    private RequestBody createObjectRequestBody(final List<FormDataEntity> formList) {
        FormBody.Builder builder = new FormBody.Builder();
        for (FormDataEntity formDataEntity : formList) {
            builder.add(formDataEntity.getFormName(), formDataEntity.getFormValue());
        }
        return builder.build();
    }

    /**
     * 创建 带文件上传的 post请求的报文参数
     *
     * @param formList 请求参数（含文件路径）
     * @return RequestBody
     */
    private RequestBody createMultipartRequestBody(final List<FormDataEntity> formList) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (FormDataEntity formDataEntity : formList) {
            if (formDataEntity instanceof FileFormDataEntity) {
                FileFormDataEntity fileFormDataEntity = (FileFormDataEntity) formDataEntity;
                File file = new File(fileFormDataEntity.getFilePath());
                RequestBody requestBody = RequestBody.create(MediaType.parse(FileUtils.getMimeType(fileFormDataEntity.getFilePath())), file);
                builder.addFormDataPart(fileFormDataEntity.getFormName(), file.getName(), requestBody);
            } else {
                builder.addFormDataPart(formDataEntity.getFormName(), formDataEntity.getFormValue());
            }
        }
        return builder.build();

    }

    /**
     * 创建get请求的Url
     *
     * @param formList 请求参数
     * @param url      请求交易url
     * @return HttpUrl
     */
    private HttpUrl createGetRequestUrl(final List<FormDataEntity> formList, String url) {
        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
        for (FormDataEntity formDataEntity : formList) {
            builder.addEncodedQueryParameter(formDataEntity.getFormName(), formDataEntity.getFormValue());
        }
        return builder.build();
    }

    /**
     * 取消请求
     *
     * @param tag activity的name
     */
    private void cancelTag(Object tag) {
        //取消等待中的请求
        for (Call call : aClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                try {
                    call.cancel();
                    LogUtils.logd("取消请求成功-->", call.request().tag().toString());
                } catch (Exception e) {
                    Log.e("取消请求失败-->", call.request().tag().toString() + e.toString());
                }
            }
        }
        //取消正在进行中的请求
        for (Call call : aClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                try {
                    call.cancel();
                    LogUtils.logd("取消请求成功-->", call.request().tag().toString());
                } catch (Exception e) {
                    LogUtils.loge("取消请求失败-->", call.request().tag().toString() + e.toString());
                }
            }
        }
    }

    /**
     * 克隆一个OkHttpClient
     *
     * @param client
     * @return
     */
    private OkHttpClient getCloneClient(OkHttpClient client) {
        OkHttpClient cloneClient = client.newBuilder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response orignalResponse = chain.proceed(chain.request());
                return orignalResponse.newBuilder().body(new ProgressResponseBody(orignalResponse.body(), new ProgressResponseListener() {
                    @Override
                    public void onResponseProgress(long bytesRead, long contentLength, boolean done) {

                    }
                })).build();
            }
        }).build();
        return cloneClient;
    }

    private OkHttpClient setClient(long time, OkHttpClient okHttpClient) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设计超时时间30s
        builder.connectTimeout(time, TimeUnit.SECONDS);
        //读取超时时间60s
        builder.readTimeout(60, TimeUnit.SECONDS);
        //写入超时时间120s
        builder.writeTimeout(120, TimeUnit.SECONDS);
        //cookie管理
        builder.cookieJar(new CookiesManager(getApplicationContext()));
        //文件缓存目录
        File file = new File(FileUtils.getAvailableCacheDir(getApplicationContext()), ConDict.cacheDir);
        //创建缓存
        Cache cache = new Cache(file, ConDict.cacheSize);
        builder.cache(cache);
        //过滤器添加请求头 和返回头
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain)
                    throws IOException {
                Request request = chain.request();
                //添加请求头
                request = request.newBuilder().addHeader("Connection", "Keep-Alive")
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8").build();
//					   .addHeader("Content-Type", "application/json").build();

                Response response = chain.proceed(request);
//				if(!response.cacheControl().noCache()) {
//
//				}
                return response;
            }
        });
        okHttpClient = builder.build();
        return okHttpClient;
    }


    /**
     * 支持https 信任指定SSL证书的https站点 传参数null 就是不验证证书
     * @param certificates
     */
//    public void setCertificates(InputStream... certificates) {
//        SSLSocketFactory sslSocketFactory = HttpsUtil.getSslSocketFactory(certificates, null, null);
//        OkHttpClient.Builder builder = getCloneClient(aClient).newBuilder();
//        //设置通过所有站点的域名  这里需要用apache的参数
//        builder.hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//        builder = builder.sslSocketFactory(sslSocketFactory);
//        aClient = builder.build();
//    }

    public void setCertificates() {
        SSLSocketFactory sslSocketFactory = HttpsUtil.getSslSocketFactory(null, null, null);
        OkHttpClient.Builder builder = getCloneClient(aClient).newBuilder();
        //设置通过所有站点的域名  这里需要用apache的参数
        builder.hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        builder = builder.sslSocketFactory(sslSocketFactory);
        aClient = builder.build();
    }
}

