package com.luoruiyi.example;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author luoruiyi
 */
public class UrlPreference {
        public static String getUrl(Context context)
        {
            SharedPreferences sharedPreferences= context.getSharedPreferences("lh_demo",
                    Context.MODE_PRIVATE);
            String bduss =sharedPreferences.getString("url", "");
            return bduss;
        }

        public static void setUrl(Context context,String url)
        {
            SharedPreferences mySharedPreferences= context.getSharedPreferences("lh_demo",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString("url", url);
            editor.commit();
        }

}
