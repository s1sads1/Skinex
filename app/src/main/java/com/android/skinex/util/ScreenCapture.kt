package com.android.skinex.util

import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import android.view.View
import com.android.skinex.publicObject.Visiter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

object ScreenCapture {
    // 캡쳐가 저장될 외부 저장소
    private val CAPTURE_PATH = "/CAPTURE_TEST"
    /**
     * 특정 뷰만 캡쳐
     * @param View
     */
    fun captureView(View: View) {
        View.buildDrawingCache()
        val captureView = View.getDrawingCache()
        val fos: FileOutputStream
        val strFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + CAPTURE_PATH
        val folder = File(strFolderPath)
        if (!folder.exists())
        {
            folder.mkdirs()
        }
        val strFilePath = strFolderPath + "/" + System.currentTimeMillis() + ".png"
        val fileCacheItem = File(strFilePath)
        Log.d("ScreenCapture", "strFilePath: "+ strFilePath)
        Log.d("ScreenCapture", "fileCacheItem: " +fileCacheItem)
        try
        {
            fos = FileOutputStream(fileCacheItem)
            Log.d("ScreenCapture", "fos: "+fos.toString())
            captureView.compress(Bitmap.CompressFormat.PNG, 100, fos)

            Log.d("ScreenCapture", "captureVeiw: "+captureView.toString())
        }
        catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }
}