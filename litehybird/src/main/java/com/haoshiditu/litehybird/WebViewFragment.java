package com.haoshiditu.litehybird;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.haoshiditu.litehybird.loadstatus.ILoadingErrorView;
import com.haoshiditu.litehybird.loadstatus.ILoadingView;


/**
 * @author luoruiyi
 */
public class WebViewFragment extends Fragment {

    public static String TAG = "WebViewFragment";

    protected SystemWebViewEngine engine;

    private View view = null;
    private FrameLayout webViewContainer;
    private String mUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.webview_container, container, false);
        webViewContainer = (FrameLayout) view.findViewById(R.id.webview_container);
        return view;
    }

    public void setArguments(LHPreferences preferences, Object service) {
        engine = new SystemWebViewEngine(getActivity(), preferences, service);

        webViewContainer.addView(engine.getView());

        final ILoadingView loadingView = preferences.getLoadingView();
        final ILoadingErrorView loadingErrorView = preferences.getLoadingErrorView();
        // 加载动画，失败页面
        webViewContainer.addView(loadingView.getView(), new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        webViewContainer.addView(loadingErrorView.getView(), new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        loadingView.getView().setVisibility(View.GONE);
        loadingErrorView.getView().setVisibility(View.GONE);
        loadingErrorView.getView().setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                              loadingErrorView.hide();
                                                              engine.loadUrl(mUrl);
                                                          }
                                                      });
    }

    public void loadUrl(String url) {
        mUrl = url;
        engine.loadUrl(url);
    }

    public void loadingJs(String js) {
        engine.loadUrl(js);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.engine != null) {
            engine.setPaused(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (this.engine == null || getActivity() == null) {
            return;
        }
        // Force window to have focus, so application always
        // receive user input. Workaround for some devices (Samsung Galaxy Note 3 at least)
        getActivity().getWindow().getDecorView().requestFocus();
        engine.setPaused(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.engine != null) {
            engine.loadUrl("about:blank");
            engine.destroy();
        }
    }
}
