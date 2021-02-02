package com.android.skinex.dataclass

import android.renderscript.Sampler
import android.util.ArrayMap
import com.google.android.gms.dynamic.ObjectWrapper
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
data class AnalyInfo(@SerializedName("degree_output") @Expose var degree_output: ArrayList<Map<String, Double>>,
                      @SerializedName("status") @Expose var status : String,
                     @SerializedName("중간2도") @Expose var result3 : String,
                     @SerializedName("깊은2도") @Expose var result4 : String,
                     @SerializedName("3도이상") @Expose var result5 : String,
                      @SerializedName("물집") @Expose var result6 : String) : Serializable
