package com.journeyapps.barcodescanner

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.android.skinex.R
import com.android.skinex.activity.GuideLine
import com.android.skinex.publicObject.Visiter
import com.android.skinex.qrscanner.QrScanner
import com.android.skinex.result_Consulting.ResultInfoActivity
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.user_info.*

//import com.google.zxing.client.android.R;
/**
 *
 */
open class CaptureActivity : Activity() {
    private var capture: CaptureManager? = null

    //바코드 뷰 객체
    private var barcodeScannerView: DecoratedBarcodeView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        barcodeScannerView = initializeContent()
        //중간 빨간선 지운다
        barcodeScannerView!!.viewFinder.setLaserVisibility(false)
        capture = CaptureManager(this, barcodeScannerView!!)
        capture!!.initializeFromIntent(intent, savedInstanceState)
        capture!!.decode()

        //버튼
        val imageButton = findViewById<View>(R.id.godetailcamera) as Button
        imageButton.setOnClickListener {
            val intent = Intent(
                applicationContext,
                ResultInfoActivity::class.java
            )
            startActivity(intent)
            finish()
        }
        val guide = findViewById<TextView>(R.id.mulm)
        guide.setOnClickListener {
            val intent = Intent(
                applicationContext,
                GuideLine::class.java
            )
            startActivity(intent)
        }
    }

    /**
     * Override to use a different layout.
     *
     * @return the DecoratedBarcodeView
     */
    protected fun initializeContent(): DecoratedBarcodeView {
        setContentView(R.layout.zxing_capture)
        return findViewById<View>(R.id.zxing_barcode_scanner) as DecoratedBarcodeView

    }

    override fun onResume() {
        super.onResume()
        capture!!.onResume()
        val str = Visiter.Visi.birth
        val birthjum = str.replace('-', '.')
        findViewById<TextView>(R.id.getgendermsg).setText("${Visiter.Visi.name} - ${Visiter.Visi.gender} - ${birthjum}")
        findViewById<Button>(R.id.recapture).setOnClickListener {
            finish()

            val integrator = IntentIntegrator(this)
            integrator.setBarcodeImageEnabled(true)
            integrator.setBeepEnabled(false)
            integrator.captureActivity = QrScanner::class.java
            integrator.setOrientationLocked(false) //세로모드
            integrator.initiateScan()

        }
    }

    override fun onPause() {
        super.onPause()
        capture!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture!!.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        capture!!.onSaveInstanceState(outState)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        capture!!.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return barcodeScannerView!!.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }
}