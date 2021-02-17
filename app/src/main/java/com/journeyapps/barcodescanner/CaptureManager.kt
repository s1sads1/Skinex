package com.journeyapps.barcodescanner

import android.Manifest
import com.android.skinex.publicObject.Camera.cam
import android.app.Activity
import android.content.pm.ActivityInfo
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.R
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.view.Display
import android.os.Build
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.DialogInterface
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.core.app.ActivityCompat
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Handler
import android.util.Log
import android.view.Surface
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.skinex.activity.GuideLine
import com.android.skinex.publicObject.Camera
import com.android.skinex.qrscanner.QrScanner
import com.android.skinex.user_Consulting.UserInfoActivity
import com.google.zxing.ResultMetadataType
import com.google.zxing.client.android.BeepManager
import com.google.zxing.client.android.InactivityTimer
import com.google.zxing.client.android.Intents
import kotlinx.android.synthetic.main.camera.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import kotlin.coroutines.coroutineContext

/**
 * Manages barcode scanning for a CaptureActivity. This class may be used to have a custom Activity
 * (e.g. with a customized look and feel, or a different superclass), but not the barcode scanning
 * process itself.
 *
 * This is intended for an Activity that is dedicated to capturing a single barcode and returning
 * it via setResult(). For other use cases, use DefaultBarcodeScannerView or BarcodeView directly.
 *
 * The following is managed by this class:
 * - Orientation lock
 * - InactivityTimer
 * - BeepManager
 * - Initializing from an Intent (via IntentIntegrator)
 * - Setting the result and finishing the Activity when a barcode is scanned
 * - Displaying camera errors
 */
class CaptureManager(
    private val activity: Activity,
    private val barcodeView: DecoratedBarcodeView
)  {
    private var orientationLock = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    private var returnBarcodeImagePath = false
    private var showDialogIfMissingCameraPermission = true
    private var missingCameraPermissionDialogMessage = ""
    private var destroyed = false
    private val inactivityTimer: InactivityTimer
    private lateinit var beepManager: BeepManager
    private lateinit var handler: Handler
    private var finishWhenClosed = false
    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            barcodeView.pause()
            beepManager.playBeepSoundAndVibrate()
            handler.post { getBarcodeImagePath(result) }
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }
    private val stateListener: CameraPreview.StateListener = object : CameraPreview.StateListener {
        override fun previewSized() {}
        override fun previewStarted() {}
        override fun previewStopped() {}
        override fun cameraError(error: Exception) {
            displayFrameworkBugMessageAndExit(
                activity.getString(R.string.zxing_msg_camera_framework_bug)
            )
        }

        override fun cameraClosed() {
            if (finishWhenClosed) {
                Log.d(TAG, "Camera closed; finishing activity")
                finish()
            }
        }
    }

    /**
     * Perform initialization, according to preferences set in the intent.
     *
     * @param intent the intent containing the scanning preferences
     * @param savedInstanceState saved state, containing orientation lock
     */
    fun initializeFromIntent(intent: Intent?, savedInstanceState: Bundle?) {
        val window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (savedInstanceState != null) {
            // If the screen was locked and unlocked again, we may start in a different orientation
            // (even one not allowed by the manifest). In this case we restore the orientation we were
            // previously locked to.
            orientationLock = savedInstanceState.getInt(
                SAVED_ORIENTATION_LOCK,
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            )
        }
        if (intent != null) {
            // Only lock the orientation if it's not locked to something else yet
            val orientationLocked = intent.getBooleanExtra(Intents.Scan.ORIENTATION_LOCKED, true)
            if (orientationLocked) {
                lockOrientation()
            }
            if (Intents.Scan.ACTION == intent.action) {
                barcodeView.initializeFromIntent(intent)
            }
            if (!intent.getBooleanExtra(Intents.Scan.BEEP_ENABLED, true)) {
                beepManager.isBeepEnabled = false
            }
            if (intent.hasExtra(Intents.Scan.SHOW_MISSING_CAMERA_PERMISSION_DIALOG)) {
                setShowMissingCameraPermissionDialog(
                    intent.getBooleanExtra(
                        Intents.Scan.SHOW_MISSING_CAMERA_PERMISSION_DIALOG,
                        true
                    ),
                    intent.getStringExtra(Intents.Scan.MISSING_CAMERA_PERMISSION_DIALOG_MESSAGE)
                )
            }
            if (intent.hasExtra(Intents.Scan.TIMEOUT)) {
                handler.postDelayed(
                    { returnResultTimeout() },
                    intent.getLongExtra(Intents.Scan.TIMEOUT, 0L)
                )
            }
            if (intent.getBooleanExtra(Intents.Scan.BARCODE_IMAGE_ENABLED, false)) {
                returnBarcodeImagePath = true
            }
        }
    }

    /**
     * Lock display to current orientation.
     */
    protected fun lockOrientation() {
        // Only get the orientation if it's not locked to one yet.
        if (orientationLock == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            // Adapted from http://stackoverflow.com/a/14565436
            val display = activity.windowManager.defaultDisplay
            val rotation = display.rotation
            val baseOrientation = activity.resources.configuration.orientation
            var orientation = 0
            if (baseOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                orientation =
                    if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    } else {
                        ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                    }
            } else if (baseOrientation == Configuration.ORIENTATION_PORTRAIT) {
                orientation =
                    if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_270) {
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    } else {
                        ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                    }
            }
            orientationLock = orientation
        }
        activity.requestedOrientation = orientationLock
    }

    /**
     * Start decoding.
     */
    fun decode() {
        barcodeView.decodeSingle(callback)
    }

    /**
     * Call from Activity#onResume().
     */
    fun onResume() {
        if (Build.VERSION.SDK_INT >= 23) {
            openCameraWithPermission()
        } else {
            barcodeView.resume()
        }
        inactivityTimer.start()
    }

    private var askedPermission = false
    @TargetApi(23)
    private fun openCameraWithPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            barcodeView.resume()
        } else if (!askedPermission) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.CAMERA),
                cameraPermissionReqCode
            )
            askedPermission = true
        } // else wait for permission result
    }

    /**
     * Call from Activity#onRequestPermissionsResult
     * @param requestCode The request code passed in [ActivityCompat.requestPermissions].
     * @param permissions The requested permissions.
     * @param grantResults The grant results for the corresponding permissions
     * which is either [PackageManager.PERMISSION_GRANTED]
     * or [PackageManager.PERMISSION_DENIED]. Never null.
     */
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>?,
        grantResults: IntArray
    ) {
        if (requestCode == cameraPermissionReqCode) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
                barcodeView.resume()
            } else {
                setMissingCameraPermissionResult()
                if (showDialogIfMissingCameraPermission) {
                    displayFrameworkBugMessageAndExit(missingCameraPermissionDialogMessage)
                } else {
                    closeAndFinish()
                }
            }
        }
    }

    /**
     * Call from Activity#onPause().
     */
    fun onPause() {
        inactivityTimer.cancel()
        barcodeView.pauseAndWait()
    }

    /**
     * Call from Activity#onDestroy().
     */
    fun onDestroy() {
        destroyed = true
        inactivityTimer.cancel()
        handler.removeCallbacksAndMessages(null)
    }

    /**
     * Call from Activity#onSaveInstanceState().
     */
    fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SAVED_ORIENTATION_LOCK, orientationLock)
    }

    /**
     * Save the barcode image to a temporary file stored in the application's cache, and return its path.
     * Only does so if returnBarcodeImagePath is enabled.
     *
     * @param rawResult the BarcodeResult, must not be null
     * @return the path or null
     */
    private fun getBarcodeImagePath(rawResult: BarcodeResult): String? {
        var barcodeImagePath: String? = null
        if (returnBarcodeImagePath) {
            val bmp = rawResult.bitmap
            try {
                val bitmapFile = File.createTempFile("barcodeimage", ".jpg", activity.cacheDir)
                val outputStream = FileOutputStream(bitmapFile)
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
                barcodeImagePath = bitmapFile.absolutePath
                cam.camerauri1 = bitmapFile.toString()
                Log.d("TAG", "cameraurl1: " + cam.camerauri1)
            } catch (e: IOException) {
                Log.w(TAG, "Unable to create temporary file and store bitmap! $e")
            }
        }
        Log.w(TAG, "가져오냐?$barcodeImagePath")
        cam.barcodeImagePath = barcodeImagePath!!
        val str = barcodeImagePath
        val qrImage = str.split("/".toRegex()).toTypedArray()
        val qr1 = qrImage[0]
        val qr2 = qrImage[1]
        val qr3 = qrImage[2]
        val qr4 = qrImage[3]
        val qr5 = qrImage[4]
        val qr6 = qrImage[5]
        val qr7 = qrImage[6]
        Log.d(TAG, "QR1 : $qr1")
        Log.d(TAG, "QR2 : $qr2")
        Log.d(TAG, "QR3 : $qr3")
        Log.d(TAG, "QR4 : $qr4")
        Log.d(TAG, "QR5 : $qr5")
        Log.d(TAG, "QR6 : $qr6")
        Log.d(TAG, "QR7:$qr7")
        cam.camerauri2 = qr7
        Log.d("TAG", "qr7가져오냐?:" + cam.camerauri2)


        (activity as CaptureActivity).findViewById<TextView>(com.android.skinex.R.id.test)
            .setTextColor(Color.GREEN)
        (activity as CaptureActivity).findViewById<TextView>(com.android.skinex.R.id.guidetext)
            .setText("사진 촬영이 완료되었습니다")
        (activity as CaptureActivity).findViewById<TextView>(com.android.skinex.R.id.guidetext)
            .setTextColor(Color.GREEN)
        (activity as CaptureActivity).findViewById<Button>(com.android.skinex.R.id.recapture).visibility = View.VISIBLE



        return barcodeImagePath
    }




    private fun finish() {
        activity.finish()
    }

    protected fun closeAndFinish() {
        if (barcodeView.barcodeView.isCameraClosed) {
            finish()
        } else {
            finishWhenClosed = true
        }
        barcodeView.pause()
        inactivityTimer.cancel()
    }

    private fun setMissingCameraPermissionResult() {
        val intent = Intent(Intents.Scan.ACTION)
        intent.putExtra(Intents.Scan.MISSING_CAMERA_PERMISSION, true)
        activity.setResult(Activity.RESULT_CANCELED, intent)
    }

    protected fun returnResultTimeout() {
        val intent = Intent(Intents.Scan.ACTION)
        intent.putExtra(Intents.Scan.TIMEOUT, true)
        activity.setResult(Activity.RESULT_CANCELED, intent)
        closeAndFinish()
    }

    protected fun returnResult(rawResult: BarcodeResult) {
        val intent = resultIntent(rawResult, getBarcodeImagePath(rawResult))
        activity.setResult(Activity.RESULT_OK, intent)
        closeAndFinish()
    }

    protected fun displayFrameworkBugMessageAndExit(message: String) {
        var message = message
        if (activity.isFinishing || destroyed || finishWhenClosed) {
            return
        }
        if (message.isEmpty()) {
            message = activity.getString(R.string.zxing_msg_camera_framework_bug)
        }
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(activity.getString(R.string.zxing_app_name))
        builder.setMessage(message)
        builder.setPositiveButton(R.string.zxing_button_ok) { dialog: DialogInterface?, which: Int -> finish() }
        builder.setOnCancelListener { dialog: DialogInterface? -> finish() }
        builder.show()
    }

    /**
     * If set to true, shows the default error dialog if camera permission is missing.
     *
     *
     * If set to false, instead the capture manager just finishes.
     *
     *
     * In both cases, the activity result is set to [Intents.Scan.MISSING_CAMERA_PERMISSION]
     * and cancelled
     */
    fun setShowMissingCameraPermissionDialog(visible: Boolean) {
        setShowMissingCameraPermissionDialog(visible, "")
    }

    /**
     * If set to true, shows the specified error dialog message if camera permission is missing.
     *
     *
     * If set to false, instead the capture manager just finishes.
     *
     *
     * In both cases, the activity result is set to [Intents.Scan.MISSING_CAMERA_PERMISSION]
     * and cancelled
     */
    fun setShowMissingCameraPermissionDialog(visible: Boolean, message: String?) {
        showDialogIfMissingCameraPermission = visible
        missingCameraPermissionDialogMessage = message ?: ""
    }

    companion object {
        private val TAG = CaptureManager::class.java.simpleName
        var cameraPermissionReqCode = 250
        private const val SAVED_ORIENTATION_LOCK = "SAVED_ORIENTATION_LOCK"

        /**
         * Create a intent to return as the Activity result.
         *
         * @param rawResult the BarcodeResult, must not be null.
         * @param barcodeImagePath a path to an exported file of the Barcode Image, can be null.
         * @return the Intent
         */
        fun resultIntent(rawResult: BarcodeResult, barcodeImagePath: String?): Intent {
            val intent = Intent(Intents.Scan.ACTION)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
            intent.putExtra(Intents.Scan.RESULT, rawResult.toString())
            intent.putExtra(Intents.Scan.RESULT_FORMAT, rawResult.barcodeFormat.toString())
            val rawBytes = rawResult.rawBytes
            if (rawBytes != null && rawBytes.size > 0) {
                intent.putExtra(Intents.Scan.RESULT_BYTES, rawBytes)
            }
            val metadata = rawResult.resultMetadata
            if (metadata != null) {
                if (metadata.containsKey(ResultMetadataType.UPC_EAN_EXTENSION)) {
                    intent.putExtra(
                        Intents.Scan.RESULT_UPC_EAN_EXTENSION,
                        metadata[ResultMetadataType.UPC_EAN_EXTENSION].toString()
                    )
                }
                val orientation = metadata[ResultMetadataType.ORIENTATION] as Number?
                if (orientation != null) {
                    intent.putExtra(Intents.Scan.RESULT_ORIENTATION, orientation.toInt())
                }
                val ecLevel = metadata[ResultMetadataType.ERROR_CORRECTION_LEVEL] as String?
                if (ecLevel != null) {
                    intent.putExtra(Intents.Scan.RESULT_ERROR_CORRECTION_LEVEL, ecLevel)
                }
                val byteSegments =
                    metadata[ResultMetadataType.BYTE_SEGMENTS] as Iterable<ByteArray>?
                if (byteSegments != null) {
                    var i = 0
                    for (byteSegment in byteSegments) {
                        intent.putExtra(Intents.Scan.RESULT_BYTE_SEGMENTS_PREFIX + i, byteSegment)
                        i++
                    }
                }
            }
            if (barcodeImagePath != null) {
                intent.putExtra(Intents.Scan.RESULT_BARCODE_IMAGE_PATH, barcodeImagePath)
            }
            return intent
        }
    }

    init {
        barcodeView.barcodeView.addStateListener(stateListener)
        handler = Handler()
        inactivityTimer = InactivityTimer(activity) {
            Log.d(TAG, "Finishing due to inactivity")
            finish()
        }
        beepManager = BeepManager(activity)
    }
}