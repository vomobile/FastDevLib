package com.wangsheng.httplibrary.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Cookie;

/**
 * @description 拼接cookie
 * @See
 * @usage
 */

public class CookieUtil {

    /**
     * 重新拼接成服务端认可的cookie 获取到最后一个
     *
     * @param cookieList 若使用的是httpLibrary 传入 new PersistentCookieStore(context).getCookies()
     */
    public static String getCookie(List<Cookie> cookieList) {
        String sbb = "";
        for (Cookie cookie : cookieList) {
            StringBuffer sb = new StringBuffer();
            sb.append(cookie.name())
                    .append("=")
                    .append(cookie.value())
                    .append("; domain=")
                    .append(cookie.domain())
                    .append("; path=")
                    .append(cookie.path());
            // 时间格式化
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss zzz");
            Calendar cal = Calendar.getInstance();
            // 设置超时时间
            cal.setTimeInMillis(new Date().getTime() + 24 * 60 * 60 * 100l);
            sb.append("; expiry=").append(format.format(cal.getTime()));
            sbb = sb.toString();
        }

        return sbb;
    }

    public static String getCookie(Cookie cookie) {
        StringBuffer sb = new StringBuffer();
        sb.append(cookie.name())
                .append("=")
                .append(cookie.value())
                .append("; domain=")
                .append(cookie.domain())
                .append("; path=")
                .append(cookie.path());
        // 时间格式化
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss zzz");
        Calendar cal = Calendar.getInstance();
        // 设置超时时间
        cal.setTimeInMillis(new Date().getTime() + 24 * 60 * 60 * 100l);
        sb.append("; expiry=").append(format.format(cal.getTime()));
        return sb.toString();
    }
}
