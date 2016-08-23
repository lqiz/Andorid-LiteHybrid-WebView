package com.luoruiyi.litehybird.loadstatus;

import android.view.View;

/**
 * @author luoruiyi
 */
public interface ILoadingView {
    /**
     * 显示，或者添加进入动画都可以在这里
     */
    void show();
    void hide();
    View getView();
}
