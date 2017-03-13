package com.haoshiditu.litehybird;

import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

import org.json.JSONException;


public class LHWebChromeClient extends WebChromeClient {

    private static final String TAG = "LHWebChromeClient";
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

}
