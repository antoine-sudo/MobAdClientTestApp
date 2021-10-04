package com.example.mobadclienttestapp_java.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Save_Languages {

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void set_language(Context ctx, String language, boolean is_selected)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(language, is_selected);
        editor.apply();
    }


    public static boolean get_language(Context ctx, String language)
    {
        return getSharedPreferences(ctx).getBoolean(language, true);
    }
}