package com.samm.estalem.Util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class SharedpreferencesData extends AppCompatActivity {


    public static void setValuePreferences(Context context,String name, Integer value)
    {
        SharedPreferences preferences= context.getSharedPreferences("ShareData",0);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt(name,value);
        editor.commit();
    }

    public static void setValuePreferences(Context context,String name, String value)
    {
        SharedPreferences preferences= context.getSharedPreferences("ShareData",0);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(name,value);
        editor.commit();
    }



    public static int getValuePreferences(Context context,String name,Integer defaultValue){

        final SharedPreferences preferences=context.getSharedPreferences("ShareData",0);
        return   preferences.getInt(name,defaultValue);
    }

    public static String getValuePreferences(Context context,String name,String defaultValue){

        final SharedPreferences preferences=context.getSharedPreferences("ShareData",0);
        return   preferences.getString(name,defaultValue);
    }



    public static void clearSharePreferences(Context context,String name){

        SharedPreferences preferences =context.getSharedPreferences("ShareData", 0);
        preferences.edit().remove(name).commit();
    }

}

