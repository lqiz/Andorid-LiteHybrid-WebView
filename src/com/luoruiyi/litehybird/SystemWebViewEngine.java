package com.luoruiyi.litehybird;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class SystemWebViewEngine {
    public static final String TAG = "SystemWebViewEngine";

    protected LHWebView webView;
    protected LHPreferences preferences;
    protected Context mContext;
    private LHWebViewClient webViewClient;
    private LHWebChromeClient webChromeClient;
    private Map<String, Target> methodCache = new LinkedHashMap<>();

    public SystemWebViewEngine(Context context, LHPreferences preferences, Object... owners) {
        this.preferences = preferences;
        this.mContext = context;

        for (Object owner : owners) {
            for (Method method : owner.getClass().getDeclaredMethods()) {
                Annotation annotation = method.getAnnotation(INVOKE.class);
                if (annotation != null) {
                    methodCache.put(((INVOKE) annotation).value(), new Target(owner, method));
                }
            }
        }

        // 注解遍历所有添加方法，并缓存
        webView = new LHWebView(context.getApplicationContext());
        webViewClient = new LHWebViewClient((Activity) mContext,
                preferences.getLoadingView(),
                preferences.getLoadingErrorView());
        webChromeClient = new LHWebChromeClient(this);

        initWebViewSettings();
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                        long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
    }

    public void handleJsPrompt(String message) throws JSONException {

        JSONObject jsObj = new JSONObject(message);
        String strTarget = jsObj.getString("target");
        JSONObject jsParas = jsObj.getJSONObject("paras");

        Target target = methodCache.get(strTarget);

        if (target != null) {
            try {
                target.method.invoke(target.owner, jsParas);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void initWebViewSettings() {

        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);

        webView.requestFocusFromTouch();

        // 设置背景透明
        if (preferences.getBoolean(LHConstant.IS_BG_TRANSPARENT, false)) {
            webView.setBackgroundColor(0);
        }

        webView.setInitialScale(0);
        webView.setVerticalScrollBarEnabled(false);
        // Enable JavaScript
        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);

        // Set the nav dump for HTC 2.x devices (disabling for ICS, deprecated entirely for Jellybean 4.2)
        try {
            Method gingerbread_getMethod = WebSettings.class.getMethod("setNavDump", new Class[]{boolean.class});

            String manufacturer = Build.MANUFACTURER;
            Log.d(TAG, "CordovaWebView is running on device made by: " + manufacturer);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB &&
                    Build.MANUFACTURER.contains("HTC")) {
                gingerbread_getMethod.invoke(settings, true);
            }
        } catch (NoSuchMethodException e) {
            Log.d(TAG, "We are on a modern version of Android, we will deprecate HTC 2.3 devices in 2.8");
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Doing the NavDump failed with bad arguments");
        } catch (IllegalAccessException e) {
            Log.d(TAG, "This should never happen: IllegalAccessException means this isn't Android anymore");
        } catch (InvocationTargetException e) {
            Log.d(TAG, "This should never happen: InvocationTargetException means this isn't Android anymore.");
        }

        //We don't save any form data in the application
        settings.setSaveFormData(false);
        settings.setSavePassword(false);

        // Jellybean rightfully tried to lock this down. Too bad they didn't give us a whitelist
        // while we do this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }
        // Enable database
        // We keep this disabled because we use or shim to get around DOM_EXCEPTION_ERROR_16
        String databasePath = webView.getContext().getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        settings.setDatabaseEnabled(true);
        settings.setDatabasePath(databasePath);


        settings.setGeolocationDatabasePath(databasePath);

        // Enable DOM storage
        settings.setDomStorageEnabled(true);

        // Enable built-in geolocation
        settings.setGeolocationEnabled(true);

        // Enable AppCache
        // Fix for CB-2282
        settings.setAppCacheMaxSize(5 * 1048576);
        settings.setAppCachePath(databasePath);
        settings.setAppCacheEnabled(true);

        // Fix for CB-1405
        // Google issue 4641
        String defaultUserAgent = settings.getUserAgentString();

        // Fix for CB-3360
        String overrideUserAgent = preferences.getString("OverrideUserAgent", null);
        if (overrideUserAgent != null) {
            settings.setUserAgentString(overrideUserAgent);
        } else {
            String appendUserAgent = preferences.getString("AppendUserAgent", null);
            if (appendUserAgent != null) {
                settings.setUserAgentString(defaultUserAgent + " " + appendUserAgent);
            }
        }

        // 本地需求。
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        /**
         * 坑1: 在米4手机上，不断打开关闭 webview，会出现页面不能滑动问题。
         * android.view.Surface.nativeLockCanvas
         * android.view.Surface.lockCanvas
         */
//        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        if (preferences.getBoolean(LHConstant.IS_DEBUG_MODE, false)) {
            // 在 DEBUG 模式下开启调试模式
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView.setWebContentsDebuggingEnabled(true);
            }
        }
    }

    /**
     * Load the url into the webview.
     */
    public void loadUrl(final String url) {
        // 对WebView先进行是否销毁的判断，
        // 而后对webView.getSettings()进行非空判断，
        // 为了解决ZoomManager::onSizeChanged NullPointerException，通过阅读Android 4.2和
        // Android 4.3的源代码来推测, 此空指针异常是由于
        // *******************************************************************************
        // Android 4.3源代码中 ZoomManager
        // Line 560 => if (reflowText && !mWebView.getSettings().getUseFixedViewport())
        // Line 955 => mWebView.getWebView().post(new PostScale(w != ow &&
        //           !mWebView.getSettings().getUseFixedViewport(), mInZoomOverview, w < ow));
        // 中mWebView.getSettings()返回null导致的
        // 此bug乃Android WebView自身的bug,通过webView.getSettings()非空保护规避之。
        if (webView != null && !webView.isDestroy() && webView.getSettings() != null) {
            webView.loadUrl(url);
        }
    }

    public View getView() {
        return webView;
    }

    public void setPaused(boolean value) {
        if (value) {
            webView.pauseTimers();
        } else {
            webView.resumeTimers();
        }
    }

    public void destroy() {
        ViewGroup viewGroup = (ViewGroup) webView.getParent();
        if (null != viewGroup) {
            viewGroup.removeView(webView);
        }

        if (webView != null) {
            webView.removeAllViews();
            webView.destroy();
        }

        webView = null;
    }

    private class Target {
        Method method;
        Object owner;

        public Target(Object owner, Method method) {
            this.method = method;
            this.owner = owner;
        }
    }
}
