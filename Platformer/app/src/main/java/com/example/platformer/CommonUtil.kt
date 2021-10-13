package com.example.platformer

import android.util.Log

object CommonUtil {

    fun printLog(tag:String = "LY==",msg:Any){
        if(BuildConfig.DEBUG){
            Log.e(tag,msg.toString())
        }
    }

}