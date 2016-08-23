package com.luoruiyi.litehybird;

import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

import org.json.JSONException;


public class LHWebChromeClient extends WebChromeClient {

    private static final String TAG = "LHWebChromeClient";
    private long MAX_QUOTA = 100 * 1024 * 1024;
    protected final SystemWebViewEngine parentEngine;

    public LHWebChromeClient(SystemWebViewEngine parentEngine) {
        this.parentEngine = parentEngine;
    }

    @Override
    public boolean onJsPrompt(WebView view, String origin,
                              String message, String defaultValue,
                              final JsPromptResult result) {
        try {
            parentEngine.handleJsPrompt(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result.confirm();
        return true;
    }


    /**
     * Handle database quota exceeded notification.
     */
    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier,
                                        long currentQuota, long estimatedSize,
                                        long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater)
    {
        quotaUpdater.updateQuota(MAX_QUOTA);
    }
}
