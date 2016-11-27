package com.wangsheng.fastdevlibrary.widget.webview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.wangsheng.fastdevlibrary.R;

/**
 * 解决安全性和一些通用功能的WebView 有加载进度条 若要重写{@link WebChromeClient} 请继承{@link BaseChromeClient}
 */
public class BaseWebView extends SafeWebView {
    private ProgressBar mProgressBar;
    private boolean isShowProgress;

    public BaseWebView(Context context) {
        super(context);
        initProgressBar(context,null);
        initWebView();
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initProgressBar(context,attrs);
        initWebView();
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initProgressBar(context,attrs);
        initWebView();
    }

    private void initProgressBar(Context context,AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.BaseWebView);
        isShowProgress = a.getBoolean(R.styleable.BaseWebView_isShowProgress,false);
        Drawable progressDrawable =  a.getDrawable(R.styleable.BaseWebView_progress_drawable);

        if(isShowProgress){
            mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 8);
            mProgressBar.setLayoutParams(layoutParams);
            if(null!=progressDrawable){
                mProgressBar.setProgressDrawable(progressDrawable);
            }else{
                mProgressBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.layer_web_progressbar));
            }
            addView(mProgressBar);
        }
        setWebChromeClient(new BaseChromeClient());
        setWebViewClient(new BaseWebClient());
        a.recycle();
    }

    public class BaseChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (isShowProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(GONE);
                } else {
                    if (mProgressBar.getVisibility() == GONE)
                        mProgressBar.setVisibility(VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    public class BaseWebClient extends android.webkit.WebViewClient {
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            view.loadUrl("file:///android_asset/web_error.html");
            super.onReceivedError(view, request, error);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) mProgressBar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        mProgressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * 以后扩展，解决WebView各种问题
     */
    private void initWebView() {

        // 问题1：SDK11，开启硬件加速，会导致白屏。 这里取消硬件加速
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        //问题2：基本都需要支持JS
        this.getSettings().setJavaScriptEnabled(true);


        //问题3：加载任何url，直接跳到系统浏览器去了。覆写下面的函数，可以解决
        this.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        //问题4：onPageFinished回调，经常没有被调用。使用 onProgressChange替换
//        webView.setWebChromeClient(new WebChromeClient() {
//
//            @Override
//            public void onProgressChanged(WebView view, final int newProgress) {
//                super.onProgressChanged(view, newProgress);
//                if (newProgress == 100) {
//                    //这里表示页面加载完成
//                }
//            }
//        });


//        //问题5：点击页面内的下载链接，无反应。这里直接监听，跳到系统浏览器去下载
//        webView.setDownloadListener(new DownloadListener() {
//
//            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
//                                        long contentLength) {
//                // 实现下载的代码
//                Uri uri = Uri.parse(url);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                ctx.startActivity(intent);
//            }
//        });
    }

    /**
     * 向网页更新Cookie，设置cookie后不需要页面刷新即可生效 因为登录是native的 所以需要把登录后的cookies同步到webView中
     */
    protected void updateCookies(String url, String value) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) { // 2.3及以下
            CookieSyncManager.createInstance(getContext().getApplicationContext());
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, value);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) { // 2.3及以下
            CookieSyncManager.getInstance().sync();
        }
    }

}