package com.luoruiyi.example.responseview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.luoruiyi.example.JSReference;
import com.luoruiyi.lhwebview.R;

/**
 * @author luoruiyi
 */
public class JSRunDialog extends Dialog {
    Context mContext;
    JsRunListener listener;
    public JSRunDialog(Context context) {
        super(context, R.style.EffectDialog);
        mContext=context;
        init();
    }

    public void setJsRunListener(JsRunListener listener){
        this.listener=listener;
    }
    protected int getLayoutId() {
        return R.layout.js_run_dialog;
    }

    protected void init() {
        int layoutResId = getLayoutId();
        if (layoutResId <= 0) {
            throw new InflateException("invalid layout resource id");
        }

        View root =View.inflate(mContext, layoutResId, null);


        final EditText jsInput = (EditText) root.findViewById(R.id.js_input);
        jsInput.setText(JSReference.getJS(mContext.getApplicationContext()));
        final Button jsRun = (Button) root.findViewById(R.id.js_run);

        jsRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(listener!=null)
                {
                    listener.runJs(jsInput.getText().toString().trim());
                }
            }
        });

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
