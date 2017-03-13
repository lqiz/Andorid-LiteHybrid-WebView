package com.haoshiditu.litehybird.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class NetworkUtil {

    private NetworkUtil() {
    }

    /**
     * 匹配是否为有效URL
     * @param url
     * @return true 合法， false 非法
     */
    public static boolean isUrlValid(String url) {
        Pattern patt = Pattern.compile("(http://|ftp://|https://|www){0,1}[^\u4e00-\u9fa5\\s]*?\\.(com|net|cn|me|tw|fr)[^\u4e00-\u9fa5\\s]*");
        Matcher matcher = patt.matcher(url);
        boolean isMatch = matcher.matches();

        if (isMatch == false || url.toLowerCase().contains("javascript")) {
            return false;
        }

        return true;
    }

    /**
     * 检测网络是否可用 wifi和4G.
     *
     * @param context 对象上下文，此处请使用application的context.
     * @return true 可用， false 不可用.
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        return isNetworkActive(info);
    }

    /**
     * 指定类型的连接是否可用.
     *
     * @param info networkinfo.
     * @return true可用, false 不可用.
     */
    private static boolean isNetworkActive(NetworkInfo info) {
        if ((info != null) && info.isConnected() && info.isAvailable()) {
            return true;
        }
        return false;
    }
}
