package com.android.skinexx.publicObject

import com.android.skinex.dataClass.UserInfo
//import com.phonegap.WPIAS.dataClass.UserInfo

object PubVarilable {

    var uid = ""
    var userInfo = UserInfo("", "", "", "", "", "", "", "", "", "", "")

    fun init(){
        uid = ""
        userInfo =
            UserInfo("", "", "", "", "", "", "", "", "", "", "")
    }

}