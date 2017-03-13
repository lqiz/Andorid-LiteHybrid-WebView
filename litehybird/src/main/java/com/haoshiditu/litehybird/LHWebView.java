package com.haoshiditu.litehybird;
/**
 * @author luoruiyi
 * use a flag to monitor whether webview is destoryed
 */

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class LHWebView extends WebView {

    private boolean mIsDestroy = false;

    public LHWebView(Context context) {
        super(context);
        mIsDestroy = false;
    }

    public LHWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mIsDestroy = false;
    }

    @Override
    public void destroy() {
        super.destroy();
        mIsDestroy = true;
    }

    public boolean isDestroy() {
        return mIsDestroy;
    }

}