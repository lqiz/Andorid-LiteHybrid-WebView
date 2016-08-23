package com.luoruiyi.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.luoruiyi.example.responseview.JSRunDialog;
import com.luoruiyi.example.responseview.JSShowDialog;
import com.luoruiyi.example.responseview.JsRunListener;
import com.luoruiyi.example.statusview.LoadingErrorView;
import com.luoruiyi.example.statusview.LoadingView;
import com.luoruiyi.lhwebview.R;
import com.luoruiyi.litehybird.INVOKE;
import com.luoruiyi.litehybird.LHConstant;
import com.luoruiyi.litehybird.LHPreferences;
import com.luoruiyi.litehybird.WebViewFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class WebViewActivity extends Activity {
    private WebViewFragment webviewFragment;
    private TextView tvTitle;
    private String mUrl = "";
    private TextView rightButton;
    private JSRunDialog jsRunDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        mUrl = getIntent().getStringExtra(LHConstant.KEY_INTENT_EXTRA_URL);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        rightButton = (TextView) findViewById(R.id.right_button);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsRunDialog = new JSRunDialog(WebViewActivity.this);
                jsRunDialog.setJsRunListener(new JsRunListener() {
                    @Override
                    public void runJs(String jsStr) {
                        jsRunDialog.dismiss();
                        jsStr = jsStr.trim();
                        jsStr = "javascript:window." + jsStr;
                        if (jsStr != null && jsStr.isEmpty()) {
                            JSReference.setJS(WebViewActivity.this.getApplicationContext(), jsStr);
                            webviewFragment.loadingJs(jsStr);
                        }
                    }
                });
                jsRunDialog.show();
            }
        });
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webviewFragment = (WebViewFragment) getFragmentManager().findFragmentById(R.id.fg_webview);

        // 设置参数
        LHPreferences preferences = new LHPreferences();
        preferences.setLoadingView(new LoadingView(this));
        preferences.setLoadingErrorView(new LoadingErrorView(this));
        preferences.set("AppendUserAgent", "hitehybird");
        if(true) {
            preferences.set(LHConstant.IS_DEBUG_MODE, true);
        }
        webviewFragment.setArguments(preferences, new Invoker1());

        // 启动加载
        webviewFragment.loadUrl(mUrl);

    }

    public class Invoker1 {
        @INVOKE("popToast")
        public void popToast(JSONObject paras) {
            JSShowDialog jsShowDialog = new JSShowDialog(WebViewActivity.this);
            jsShowDialog.setJSStr("action: " +  paras.toString());
            jsShowDialog.show();
        }

        /**
         * 打开新的webview
         */
        @INVOKE("openWebView")
        public void openWebView(JSONObject paras) throws JSONException {
            String newUrl = paras.getString("url");

            Intent intent = new Intent(WebViewActivity.this, WebViewActivity.class);
            intent.putExtra(LHConstant.KEY_INTENT_EXTRA_URL, newUrl);

            startActivity(intent);

            if (paras.getBoolean("closeCurrent")) {
                finish();
            }
        }

        @INVOKE("refresh")
        public void refreshWebView(JSONObject paras) {
            webviewFragment.loadUrl(mUrl);
        }

        @INVOKE("closeCurrentActivity")
        public void closeCurrentActivity(JSONObject paras) {
            finish();
        }
    }
}
