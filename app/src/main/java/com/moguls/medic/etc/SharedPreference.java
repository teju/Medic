

package com.moguls.medic.etc;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {


    public static final String isLoggedIn = "isLoggedIn";
    public static final String isDOCTOR = "isDOCTOR";
    public static final String AccountID = "AccountID";


    private static SharedPreferences.Editor getEditor(Context context) {
        return SharedPreference.getSharedPref(context).edit();
    }
    public static SharedPreferences getSharedPref(Context context) {
        return context.getSharedPreferences("MOGULS_MEDIC", Context.MODE_PRIVATE);
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences.Editor editor = SharedPreference.getEditor(context);
        editor.putString(key, value);
        editor.apply();
    }
    public static String getString(Context context, String key) {
        try {
            return SharedPreference.getSharedPref(context).getString(key, "");
        }
        catch(Exception ex) {
        }
        return "";
    }
    public static void setInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = SharedPreference.getEditor(context);
        editor.putInt(key, value);
        editor.apply();
    }
    public static int getInt(Context context, String key) {
        try {
            return SharedPreference.getSharedPref(context).getInt(key, 0);
        }
        catch(Exception ex) {
        }
        return 0;
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = SharedPreference.getEditor(context);
        editor.putBoolean(key, value);
        editor.apply();
    }
    public static boolean getBoolean(Context context, String key) {
        try {
            return SharedPreference.getSharedPref(context).getBoolean(key, false);
        }
        catch(Exception ex) {
        }
        return false;
    }
    public static void clear(Context context) {
        SharedPreferences.Editor editor = SharedPreference.getEditor(context);
        editor.clear();
        editor.apply();
    }
}
