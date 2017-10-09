package com.nellions.nellionscanvas.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Chris Muiruri on 11/9/2015.
 */
public class Preferences {


    /* create the preferences */
    /* param
    *
    * Application/Activity context
    * Name of the preference (String)
    * Preference key (String)
    * Returns void
    * for string
    * */
    public void storePreferences(Context context, String pref_name, String pref_key, String pref_value) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE); //1
        editor = settings.edit();

        editor.putString(pref_key, pref_value);
        editor.apply();
    }

    /* create the preferences */
    /* param
    * for int
    * Application/Activity context
    * Name of the preference (String)
    * Preference key (String)
    * Returns void
    *
    * */
    public void storePreferences(Context context, String pref_name, String pref_key, int pref_value) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE); //1
        editor = settings.edit();

        editor.putInt(pref_key, pref_value);
        editor.apply();

    }

    /* get the stored preferences */
    /* param
    *
    * Application/Activity context
    * Name of the preference (String)
    * Preference key (String)
    * Returns the preference key value or null if the key is not set
    * for int
    * */

    public int getIntPreferences(Context context, String pref_name, String pref_key) {
        SharedPreferences settings;
        int txt;
        settings = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE); //1
        txt = settings.getInt(pref_key, Integer.MIN_VALUE);
        return txt;
    }


    /* get the stored preferences */
    /* param
    *
    * Application/Activity context
    * Name of the preference (String)
    * Preference key (String)
    * Returns the preference key value or null if the key is not set
    * for string
    * */

    public String getPreferences(Context context, String pref_name, String pref_key) {
        SharedPreferences settings;
        String txt;
        settings = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE); //1
        txt = settings.getString(pref_key, null);
        return txt;
    }

    /* clear all stored preferences */
    /* param
    *
    * Application/Activity context
    * Name of the preference (String)
    * returns void
    * clears the preference under name of the preference
    *
    * */
    public void clearPreferences(Context context, String pref_name) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.apply();
    }

}
