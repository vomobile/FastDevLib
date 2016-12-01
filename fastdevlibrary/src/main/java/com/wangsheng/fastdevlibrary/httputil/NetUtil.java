package com.wangsheng.fastdevlibrary.httputil;

import android.app.Activity;
import android.content.Context;

import java.util.Map;

public class NetUtil {

	private static NetUtil sNetUtil;
	private Context context;
	public static NetUtil getInstance(Activity activity){
		if (sNetUtil ==null) {
			sNetUtil = new NetUtil(activity);
		}
		return sNetUtil;
	}
	public NetUtil(Activity activity) {
		context = activity;
	}
	/**
	 * http请求方法（请求方式get）
	 * 备注：客户端所有get方法都通过此方法执行，可在此方法给对“请求参数”、“返回参数”等做处理。
	 * @param url
	 *            ip+交易名
	 * @param map
	 *            请求参数
	 * @param resultCallBack
	 *            请求回调方法
	 * */
	public void requestGet(String url, Map<String, Object> map,final
			ResultCallBack resultCallBack) {
		//在此处执行HttpGet请求，结果通过resultCallBack返回到通讯插件
	}

	/**
	 * http请求方法（请求方式post）
	 * 备注：客户端所有post方法都通过此方法执行，可在此方法给对“请求参数”、“返回参数”等做处理。
	 * @param url
	 *            ip+交易名
	 * @param map
	 *            请求参数
	 * @param resultCallBack
	 *            请求回调方法
	 * */
	public void requestPost(String url, Map<String, Object> map,final
			ResultCallBack resultCallBack) {
		//在此处执行HttpPost请求，结果通过resultCallBack返回到通讯插件
		
	}
	/**
	 * 图片异步请求
	 * @param url
	 *            ip+图片交易名
	 * @param map
	 *            请求参数
	 * @param resultCallBack
	 *            请求结果回调方法（回调返回类型bitmap）
	 * */
	public void requestImageDownload(String url, Map<String, Object> map,final
			ResultCallBack resultCallBack) {
		//在此处执行ImageLoad图片请求，结果通过resultCallBack返回到通讯插件（返回参数Bitmap）
	}
	/**
	 * 请求文件流
	 * @param url
	 * @param map
	 * @param resultCallBack
	 */
	public void RequestStream(String url, Map<String, Object> map,final
			ResultCallBack resultCallBack){
		//在此处执行文件流请求，结果通过resultCallBack返回到通讯插件（返回参数InputStream）
	}
	/**
	 * 请求结果回调接口
	 * */
	public interface ResultCallBack {
		/**
		 * 请求成功返回数据
		 * */
		public void onSuccess(Object response);

		/**
		 * 请求异常
		 * */
		public void onError(Object errorMsg);
	}
}