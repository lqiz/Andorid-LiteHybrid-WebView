package com.luoruiyi.litehybird;


import com.luoruiyi.litehybird.loadstatus.ILoadingErrorView;
import com.luoruiyi.litehybird.loadstatus.ILoadingView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author luoruiyi
 * Pass settings to webview
 */
public class LHPreferences {
    private HashMap<String, String> prefs = new HashMap<>(20);

    private ILoadingView loadingView;
    private ILoadingErrorView loadingErrorView;

    public void set(String name, String value) {
        prefs.put(name.toLowerCase(Locale.ENGLISH), value);
    }

    public void set(String name, boolean value) {
        set(name, "" + value);
    }

    public void set(String name, int value) {
        set(name, "" + value);
    }

    public void set(String name, double value) {
        set(name, "" + value);
    }

    public Map<String, String> getAll() {
        return prefs;
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        name = name.toLowerCase(Locale.ENGLISH);
        String value = prefs.get(name);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return defaultValue;
    }

    // Added in 4.0.0
    public boolean contains(String name) {
        return getString(name, null) != null;
    }

    public String getString(String name, String defaultValue) {
        name = name.toLowerCase(Locale.ENGLISH);
        String value = prefs.get(name);
        if (value != null) {
            return value;
        }
        return defaultValue;
    }

    public ILoadingView getLoadingView() {
        return loadingView;
    }

    public void setLoadingView(ILoadingView loadingView) {
        this.loadingView = loadingView;
    }

    public ILoadingErrorView getLoadingErrorView() {
        return loadingErrorView;
    }

    public void setLoadingErrorView(ILoadingErrorView loadingErrorView) {
        this.loadingErrorView = loadingErrorView;
    }
}
