package com.android.skinex.dataclass

import android.graphics.Bitmap
import java.io.File

data class VisiterInfo(
    var name : String,
    var gender : String,
    var birth : String,
    var burndate : String,
    var camerauri1 : String,
    var camerauri2 : String,
    var firebaseurl : String,
    var screenshot : Bitmap,
    var camerauri : Bitmap,
    var gallary : String,
    var capture : String
)
