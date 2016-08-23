package com.luoruiyi.example.responseview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.method.ScrollingMovementMethod;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.luoruiyi.lhwebview.R;


/**
 * @author luoruiyi
 */
public class JSShowDialog extends Dialog {
    Context mContext;
    TextView jsShow;

    public JSShowDialog(Context context) {
        super(context, R.style.EffectDialog);
        mContext = context;
        init();
    }

    protected int getLayoutId() {
        return R.layout.js_show_dialog;
    }

    protected void init() {
        int layoutResId = getLayoutId();
        if (layoutResId <= 0) {
            throw new InflateException("invalid layout resource id");
        }

        View root = View.inflate(mContext, layoutResId, null);


        jsShow = (TextView) root.findViewById(R.id.js_str);
        jsShow.setMovementMethod(ScrollingMovementMethod.getInstance());

        setContentView(root);


        setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                }
                return true;
            }
        });
    }

    public void setJSStr(String jsStr) {
        jsShow.setText(jsStr + "");
    }

    /**
     * 显示dialog.
     */
    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {

        }
    }

    /**
     * 隐藏dialog.
     */
    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {

        }
    }
}
