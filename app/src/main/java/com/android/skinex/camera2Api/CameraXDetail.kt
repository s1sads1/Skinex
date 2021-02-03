package com.android.skinex.camera2Api

//import kotlinx.android.synthetic.main.camera.*
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.skinex.databinding.ActivityDetailcameraBinding
import com.android.skinex.publicObject.Visiter
import com.android.skinex.result_Consulting.ResultInfoActivity
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraXDetail :AppCompatActivity(){

//   private lateinit var storage: FirebaseStorage

    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var photoFile: File
    private lateinit var savedUri :Uri

    private lateinit var binding: ActivityDetailcameraBinding

    var REQUEST_TAKE_PHOTO_10 =1
    var REQUEST_TAKE_PHOTO_20 =2



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDetailcameraBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


//        Log.d("onCreate:", "onCreate")
//        // 카메라 권한 요청
//        if (allPermissionsGranted()) {
//            startCamera()
//        } else {
//            ActivityCompat.requestPermissions(
//                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
//            )
//        }
//
//        // 사진 찍기 버튼에 대한 리스너 설정
//        binding.cameraCaptureButton.setOnClickListener { takePhoto() }
//
//        outputDirectory = getOutputDirectory()
//
//        cameraExecutor = Executors.newSingleThreadExecutor()
//        goResultInfo()

    }

//     fun onStart() {
//         super.onStart()
//
//     }

    override fun onStart() {
        super.onStart()

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        // 사진 찍기 버튼에 대한 리스너 설정
        binding.cameraCaptureButton.setOnClickListener { takePhoto() }

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()
        goResultInfo()
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show()
    }

    private fun takePhoto() {


        // 수정 가능한 이미지 캡처 사용 사례에 대한 안정적인 참조 얻기
        val imageCapture = imageCapture ?: return

        // 이미지를 보관할 타임 스탬프 출력 파일 만들기
        var time = SimpleDateFormat(FILENAME_FORMAT, Locale.US
        ).format(System.currentTimeMillis())
        photoFile = File(
            outputDirectory,
            time + ".jpg")
        Log.d(TAG, "${time}")
        Visiter.Visi.firebaseurl = time.toString()

        // 파일 + 메타 데이터를 포함하는 출력 옵션 개체 만들기
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // 사진 촬영 후 트리거되는 이미지 캡처 리스너를 설정합니다.
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "사진 촬영이 실패했습니다: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    savedUri = Uri.fromFile(photoFile)
                    val msg = "사진 촬영 성공: $savedUri"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                    Visiter.Visi.camerauri2 = photoFile.toString()
                }
            })
//        includesForUploadFiles()

    }

    private fun startCamera() {
Log.d("startCamera :", "startCamera")
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // 카메라의 수명주기를 수명주기 소유자에게 연결하는 데 사용됩니다.
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.createSurfaceProvider())
                }

            // 기본으로 후면 카메라 선택
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // 리바인딩하기 전에 사용 사례 바인딩 해제
                cameraProvider.unbindAll()

                // 사용 사례를 카메라에 바인딩
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview
                )

                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))

        imageCapture = ImageCapture.Builder()
            .build()

    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(com.android.skinex.R.string.app_name)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 20
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "카메라 접근 권한이 부여되지 않았습니다",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    //촬영 클릭시 전체촬영 이벤트
    fun goResultInfo(){
        binding.godetailcamera.setOnClickListener {
//           var NAME = intent.getStringExtra("NAME")
//            Intent(this, CameraDetailActivity::class.java).putExtra("NAME", NAME)
            val intent = Intent(this, ResultInfoActivity::class.java)
            startActivity(intent)
//            startActivityForResult(Intent(this, ResultInfoActivity::class.java), REQUEST_TAKE_PHOTO_10)
        }
    }

}