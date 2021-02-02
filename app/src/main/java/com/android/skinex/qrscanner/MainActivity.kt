//package com.android.skinex.qrscanner
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.android.skinex.R
//import com.google.zxing.integration.android.IntentIntegrator
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var button : Button
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        button = findViewById(R.id.button)
//        button.setOnClickListener {
//            QrScanner()
//        }
//    }
//
//    //큐알 생성 액티비티로 이동
//    private fun QrScanner() {
//        val integrator = IntentIntegrator(this)
//        integrator.setBeepEnabled(false)
//        integrator.captureActivity = QrScanner::class.java
//        integrator.setOrientationLocked(false) //세로모드
//        integrator.initiateScan()
//    }
//
//    //큐알 코드 인식 액티비티에서 큐알코드를 인식한 결과 값을 받는 콜백 메서드
//    override fun onActivityResult(
//        requestCode: Int,
//        resultCode: Int,
//        data: Intent?
//    ) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        //큐알 코드 결과값
//        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
//
//        if(result!=null){
//            //결과 값이 존재한다면 토스트를 띄운다
//            if(result.contents!=null){
//                val toast = Toast.makeText(applicationContext, result.contents, Toast.LENGTH_SHORT)
//                toast.show()
//            }
//            else{
//                val toast = Toast.makeText(applicationContext, "값이 없습니다", Toast.LENGTH_SHORT)
//                toast.show()
//            }
//        }
//        else{
//            val toast = Toast.makeText(applicationContext, "값이 없습니다", Toast.LENGTH_SHORT)
//            toast.show()
//        }
//
//    }
//
//
//}