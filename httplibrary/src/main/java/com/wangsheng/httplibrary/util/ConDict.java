package com.wangsheng.httplibrary.util;

/**
 * @description
 * @See
 * @usage
 */

public class ConDict {
    public static final String BankId = "9999";

    public static final String Flag_False = "false";
    public static final String Flag_True = "true";

    /**
     * 临时文件夹目录
     */
    public static final String TempDir = "temp";

    /**
     * 网络请求缓存目录
     */
    public static final String cacheDir = "response";

    /**
     * 网络请求缓存大小（20M）
     */
    public static final int cacheSize = 20 * 1024 * 1024;

    /**
     * 有网络缓存时间 10分钟
     */
    public static final int cacheTimeNet = 10 * 60;

    /**
     * 无网络缓存时间 1天
     */
    public static final int cacheTimeNoNet = 1  * 24 * 60 * 60;

    /**
     * 请求码
     */
    public static final class RequestCode {

        //请求码示例 命名规则：交易名
        //public static final int action = 0x001;

        /**
         * 默认请求码
         */
        public static final int defaultCode = 0x000;

    }
}
