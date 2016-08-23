package com.luoruiyi.litehybird.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class NetworkUtil {

    private NetworkUtil() {
    }

    public static boolean isUrlValid(String url) {
        // 匹配是否为有效URL
        Pattern patt = Pattern.compile("(http://|ftp://|https://|www){0,1}[^\u4e00-\u9fa5\\s]*?\\.(com|net|cn|me|tw|fr)[^\u4e00-\u9fa5\\s]*");
        Matcher matcher = patt.matcher(url);
        boolean isMatch = matcher.matches();

        if (isMatch == false || url.toLowerCase().contains("javascript")) {
            return false;
        }

        return true;
    }

    /**
     * 检测网络是否可用.wifi和手機有一種可用即爲可用.
     *
     * @param context 上下文对象.此处请使用application的context.
     * @return true可用.false不可用.
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
     * @return 是否可用.true可用, false不可用.
     */
    private static boolean isNetworkActive(NetworkInfo info) {
        if ((info != null) && info.isConnected() && info.isAvailable()) {
            return true;
        }
        return false;
    }

    /**
     * 判断网络是wifi、2、3、4g
     *
     * @param context
     * @return
     */
    public static String getNetWorkTypeForWifi234(Context context) {
        String type = "";
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        int networkType = tm.getNetworkType();

        if (activeNetInfo == null) {
            type = "OFFLINE";
        } else if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            type = "WIFI";
        } else {
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g 114kbps
                case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g 384kbps
                case TelephonyManager.NETWORK_TYPE_1xRTT: // 144kbps 2G的过渡
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    type = "2G";
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_0: // 153.6kps - 2.4mbps 属于3G
                case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g 1.8mbps - 3.1mbps 属于3G过渡，3.5G
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // 14.7Mbps 下行 3.5G
                case TelephonyManager.NETWORK_TYPE_HSDPA: // 3.5G 高速下行分组接入 14.4mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA: // 3.5G 高速上行链路分组接入 1.4 - 5.8 mbps
                case TelephonyManager.NETWORK_TYPE_UMTS: // 联通3g 标准
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                case TelephonyManager.NETWORK_TYPE_EHRPD: // CDMA2000向LTE 4G的中间产物 HRPD的升级
                    type = "3G";
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE: // 3G到4G的过渡，是3.9G的国际标准
                    type = "4G";
                    break;
                default:
                    type = "OTHER";
                    break;
            }
        }
        return type;
    }
}
