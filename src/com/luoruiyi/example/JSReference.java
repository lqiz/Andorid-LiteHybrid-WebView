package com.luoruiyi.example;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author luoruiyi
 */
public class JSReference {
    public static String getJS(Context context)
    {
        SharedPreferences sharedPreferences= context.getSharedPreferences("lh_demo",
                Context.MODE_PRIVATE);
        String jsStr =sharedPreferences.getString("js", "");
        return jsStr;
    }

    public static void setJS(Context context,String jsStr)
    {
        SharedPreferences mySharedPreferences= context.getSharedPreferences("lh_demo",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("js", jsStr);
        editor.commit();
    }
}
