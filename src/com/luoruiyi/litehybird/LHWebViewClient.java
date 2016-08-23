package com.luoruiyi.litehybird;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.luoruiyi.litehybird.loadstatus.ILoadingErrorView;
import com.luoruiyi.litehybird.loadstatus.ILoadingView;
import com.luoruiyi.litehybird.utils.NetworkUtil;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 简单可依赖
 */
public class LHWebViewClient extends WebViewClient {
    private Activity activity;
    private ILoadingView mLoadingView;
    private ILoadingErrorView mEmptyView;

    boolean isCurrentlyLoading;
    private float duration = 0.0f; // 打开页面经历的总时间
    public static final int MAX_TIME_OUT = 2 * 1000; // 超时

    private long startTime;
    private long endTime;

    private String loadUrl = null;

    protected Timer mTimer;
    protected boolean mIsError;

    public LHWebViewClient(Activity ac,
                           ILoadingView loadingView,
                           ILoadingErrorView emptyView) {
        activity = ac;
        mLoadingView = loadingView;
        mEmptyView = emptyView;

        mEmptyView.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (!NetworkUtil.isUrlValid(url)) {
            return true;
        }

        super.shouldOverrideUrlLoading(view, url);

        if (!isWebViewValid(view)) {
            return true;
        }

        view.loadUrl(url);

        return true;
    }


    @Override
    public void onPageStarted(final WebView view, final String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        mIsError = false;
        startTime = System.currentTimeMillis();
        isCurrentlyLoading = true;

        if (!isWebViewValid(view)) {
            return;
        }

        // 没网就直接返回，但是WebViewClient的生命周期照样会继续finish
        if (activity != null && !NetworkUtil.isNetworkAvailable(activity)) {
            mIsError = true;
            mEmptyView.show();
            return;
        }

        mIsError = false;
        mLoadingView.show();

        if (mTimer == null) {
            mTimer = new Timer();
        }

        if (mTimer != null) {
            mTimer.schedule(new TimerTask() {

                @Override
                public void run() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onReceivedError(view, -6, "The connection to the server was unsuccessful.", url);
                        }
                    });

                    if (mTimer != null) {
                        mTimer.cancel();
                        mTimer.purge();
                        mTimer = null;
                    }

                }

            }, MAX_TIME_OUT);
        }
    }

    @Override
    public void onPageFinished(final WebView view, final String url) {
        super.onPageFinished(view, url);
        mLoadingView.hide();

        if (!isCurrentlyLoading && !url.startsWith("about:")) {
            return;
        }

        isCurrentlyLoading = false;

        if (!isWebViewValid(view)) {
            return;
        }

        if (!mIsError) {
            statisticsH5LoadSuccess();
        }

        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        // Ignore error due to stopLoading().
        if (!isCurrentlyLoading) {
            return;
        }

        super.onReceivedError(view, request, error);
        if (!isWebViewValid(view)) {
            return;
        }

        mIsError = true;
        statisticsH5LoadFail(request);
        view.loadUrl("about:blank");
        mLoadingView.hide();
        mEmptyView.show();
    }

    private boolean isValidStatistics(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }

        if (url.equals("about:blank")) {
            return false;
        }
        return true;
    }

    private boolean isWebViewValid(final WebView view) {
        if (view != null && !((LHWebView) view).isDestroy()) {
            return true;
        }
        return false;
    }


    @Override
    public void onReceivedSslError(final WebView view, SslErrorHandler handler, final SslError error) {
        /**
         * 对于加载SSL协议网页失败的直接作失败处理
         */
        mIsError = true;
        super.onReceivedSslError(view, handler, error);
    }

    /**
     * H5 加载失败统计
     *
     * @param request
     */
    private void statisticsH5LoadFail(WebResourceRequest request) {

    }

    /**
     * H5加载成功统计，通常需要时长数据
     */
    private void statisticsH5LoadSuccess() {
        if (!isValidStatistics(loadUrl)) {
            return;
        }
        endTime = System.currentTimeMillis();
        duration = ((endTime - startTime) / 1000.0f);
        DecimalFormat df = new DecimalFormat("#.00");
        duration = Float.valueOf(df.format(duration));

        if (duration <= 0.0f) {
            return;
        }
    }

}
