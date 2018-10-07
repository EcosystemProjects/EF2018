package com.mentornity.ecosytemfeed;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class LanguageChanger extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
    }
    //It is just to change languange
    public static void setLocaleEN(Context context){
        Locale locale = new Locale("en","US");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);
    }

    public static void setLocaleTR (Context context){
        Locale locale = new Locale("tr","TR");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);
    }
}
