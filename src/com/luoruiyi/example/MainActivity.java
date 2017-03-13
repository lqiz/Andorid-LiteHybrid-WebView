package com.luoruiyi.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;


import com.haoshiditu.litehybird.LHConstant;
import com.luoruiyi.lhwebview.R;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button  open;

    // 该文件在本地 assert/h5_page_demo.html, 与服务器上完全一致，
    // 但是注意,如果使用本地文件，LHWebViewClient 里有 NetworkUtil.isUrlValid 的校验，需要删掉
    private String url = "http://www.haoshiditu.com/h5_page_demo.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        open = (Button) findViewById(R.id.open);
        open.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open:
                if (!TextUtils.isEmpty(url)) {
                    openWebView(url);
                }
                break;
        }
    }

    public void openWebView(String url) {
        Intent it = new Intent(this, WebViewActivity.class);
        it.putExtra(LHConstant.KEY_INTENT_EXTRA_URL, url);
        startActivity(it);
    }
}
