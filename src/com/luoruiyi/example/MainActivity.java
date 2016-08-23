package com.luoruiyi.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.luoruiyi.lhwebview.R;
import com.luoruiyi.litehybird.LHConstant;

public class MainActivity extends Activity implements View.OnClickListener {

    private EditText urlText;
    private Button clean, open;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        urlText = (EditText) findViewById(R.id.urlText);
        clean = (Button) findViewById(R.id.clean);
        open = (Button) findViewById(R.id.open);
        clean.setOnClickListener(this);
        open.setOnClickListener(this);
        urlText.setText(UrlPreference.getUrl(this.getApplicationContext()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clean:
                urlText.setText("");
                save("");
                break;
            case R.id.open:
                Editable url = urlText.getText();
                if (!TextUtils.isEmpty(url)) {
                    save(url.toString().trim());
                    openWebView(url.toString().trim());
                }
                break;
        }
    }

    public void save(String url) {
        UrlPreference.setUrl(this.getApplicationContext(), url);
    }

    public void openWebView(String url) {
        Intent it = new Intent(this, WebViewActivity.class);
        it.putExtra(LHConstant.KEY_INTENT_EXTRA_URL, url);
        startActivity(it);
    }
}
