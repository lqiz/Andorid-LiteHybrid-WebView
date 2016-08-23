package com.luoruiyi.example.statusview;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.luoruiyi.lhwebview.R;
import com.luoruiyi.litehybird.loadstatus.ILoadingErrorView;

/**
 * @author luoruiyi
 * loading error view, it need to implement ILoadingErrorView
 */
public class LoadingErrorView extends LinearLayout implements ILoadingErrorView {

    public LoadingErrorView(Context context) {
        super(context);
        View.inflate(context, R.layout.widget_loading_error_view, this);
    }

    @Override
    public void show() {
        this.setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        this.setVisibility(View.GONE);
    }

    @Override
    public View getView() {
        return this;
    }
}
