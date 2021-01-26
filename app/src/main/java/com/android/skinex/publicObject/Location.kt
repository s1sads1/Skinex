package com.android.skinex.publicObject

import com.android.skinex.dataClass.CityInfo
import com.android.skinex.dataClass.DistrictInfo


object Location {

    var cityInfo = ArrayList<CityInfo>()
    var districtInfo = ArrayList<DistrictInfo>()

    fun init(){

        cityInfo = ArrayList()
        districtInfo = ArrayList()

    }

}