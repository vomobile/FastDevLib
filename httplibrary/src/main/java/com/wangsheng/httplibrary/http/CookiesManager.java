package com.wangsheng.httplibrary.http;

import android.content.Context;

import com.wangsheng.fastdevlibrary.commonutils.LogUtils;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;


/**
 * 自动管理Cookies
 */
public class CookiesManager implements CookieJar
{
	private PersistentCookieStore cookieStore;


	public CookiesManager()
	{
	}

	public CookiesManager(Context context)
	{
		cookieStore = new PersistentCookieStore(context);
	}


	@Override
	public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
		if (cookies != null && cookies.size() > 0) {
			for (Cookie item : cookies) {
				cookieStore.add(url, item);
			}
		}
		LogUtils.logd("服务端返回的cookies",cookies.toString());
	}

	@Override
	public List<Cookie> loadForRequest(HttpUrl url) {
		List<Cookie> cookies = cookieStore.get(url);
		LogUtils.logd("上送的cookies",cookies.toString());
		return cookies;
	}
}
