package com.example.bullet_journal.helpClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.bullet_journal.R;

import java.util.HashSet;
import java.util.Set;

public class PreferencesHelper {

    public static final String prefs_name= "bujo_prefs";

    public static String getLanguage(Context context){
//        SharedPreferences prefs= context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
//        return prefs.getString("lang", "en");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(context.getString(R.string.language_setting_key), "dsgfgh");
    }

    public static void saveLanguage(Context context, String val){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        String language = sharedPreferences.getString(context.getString(R.string.language_setting_key), "dsgfgh");
//        SharedPreferences prefs= context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(context.getString(R.string.language_setting_key), val);
        editor.commit();
    }

    public static Set<String> getSelectedOptions(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences prefs= context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
        Set<String> selections= sharedPreferences.getStringSet(context.getString(R.string.options_setting_key), null);
//        String[] selected= selections.toArray(new String[] {});
        return selections;
    }

    public static void saveSelectedOptions(Context context, Set<String> vals){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences prefs= context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putStringSet(context.getString(R.string.options_setting_key), vals);
        editor.commit();
    }

    public static boolean getTheme(Context context){
//        SharedPreferences prefs= context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(context.getString(R.string.theme_key), false);
    }

    public static void saveTheme(Context context, boolean val){
//        SharedPreferences prefs= context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.theme_key), val);
        editor.commit();
    }

    public static Set<String> getMenuItems(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> emptySet = new HashSet<>();
        return sharedPreferences.getStringSet(context.getString(R.string.options_setting_key), emptySet);
    }

    public static void saveMenuItems(Context context, Set<String> val){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putStringSet(context.getString(R.string.options_setting_key), val);
        editor.commit();
    }
}
