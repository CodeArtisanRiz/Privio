package com.winkbr.browser.activity

import android.content.Context

class PreferenceProvider(context: Context) {
    private val sharedPreferences =  context.getSharedPreferences("myPreferences",0)
//    Save Boolean Values into shared preferences
    fun putBoolean(key: String, value: Boolean){
        sharedPreferences.edit().putBoolean(key,value).apply()
    }
//    Retrieve Boolean Value from shared preferences
    fun getBoolean(key: String):Boolean{
        return sharedPreferences.getBoolean(key,false)
    }
}