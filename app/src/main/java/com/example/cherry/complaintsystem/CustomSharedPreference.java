package com.example.cherry.complaintsystem;

import android.content.Context;
import android.content.SharedPreferences;

//Currently not in use properly -- WIll see later

public class CustomSharedPreference {

    SharedPreferences sharedPref;

    public SharedPreferences getSharedPref(String filename, Context context) {
        sharedPref = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return sharedPref;
    }
}
