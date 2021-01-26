package com.android.skinex.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "patient_num")val patientNum: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "gender")val gender: String?,
    @ColumnInfo(name = "birth_day")val birthDay: String?,
    @ColumnInfo(name = "insert_day")val insertDay: String?,
    @ColumnInfo(name = "burn_date")val burnDate: String?,
    @ColumnInfo(name = "body_style")val bodyStyle: String?,
    @ColumnInfo(name = "body_detail")val bodyDetail: String?,
    @ColumnInfo(name = "burn_style")val burnStyle: String?,
    @ColumnInfo(name = "camera_url1")val cameraUrl1: String?,
    @ColumnInfo(name = "camera_url2")val cameraUrl2: String?,
    @ColumnInfo(name = "result")val Result: String?
)