package com.android.skinex.camera2Api

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.android.skinex.activity.SeclectVisiter
import com.android.skinex.databinding.ActivityCameraBinding
import com.android.skinex.databinding.ActivityDetailcameraBinding
import com.android.skinex.databinding.UserInfoBinding
import com.android.skinex.publicObject.Visiter
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
//import kotlinx.android.synthetic.main.camera.*
import kotlinx.android.synthetic.main.camera.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraX :AppCompatActivity(){

//   private lateinit var storage: FirebaseStorage
    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var photoFile: File
    private lateinit var savedUri :Uri
    private lateinit var binding: ActivityCameraBinding

    var REQUEST_TAKE_PHOTO_10 =1
    var REQUEST_TAKE_PHOTO_20 =2

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        // 카메라 권한 요청
//        if (allPermissionsGranted()) {
//            startCamera()
//        } else {
//            ActivityCompat.requestPermissions(
//                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
//        }
//
//        // 사진 찍기 버튼에 대한 리스너 설정
//       binding.cameraCaptureButton.setOnClickListener { takePhoto() }
//
//        outputDirectory = getOutputDirectory()
//
//        cameraExecutor = Executors.newSingleThreadExecutor()
//        goDetailCamera()
        info()
    }
    fun info() {
        binding.getnamemsg.setText(Visiter.Visi.name)
        binding.getgendermsg.setText(Visiter.Visi.gender)
        binding.getbirthmsg.setText(Visiter.Visi.birth)
    }

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
        goDetailCamera()
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show()
    }

    private fun takePhoto() {


        // 수정 가능한 이미지 캡처 사용 사례에 대한 안정적인 참조 얻기
        val imageCapture = imageCapture ?: return

        // 이미지를 보관할 타임 스탬프 출력 파일 만들기
        photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg")

        // 파일 + 메타 데이터를 포함하는 출력 옵션 개체 만들기
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // 사진 촬영 후 트리거되는 이미지 캡처 리스너를 설정합니다.
        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "사진 촬영이 실패했습니다: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    savedUri = Uri.fromFile(photoFile)
                    val msg = "사진 촬영 성공: $savedUri"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                    Visiter.Visi.camerauri1 = photoFile.toString()
                }
            })

//        includesForUploadFiles()

    }

    private fun startCamera() {

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
                    this, cameraSelector, preview)

                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))

        imageCapture = ImageCapture.Builder()
            .build()

    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
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
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "카메라 접근 권한이 부여되지 않았습니다",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


    //촬영 클릭시 전체촬영 이벤트
    fun goDetailCamera(){
        binding.godetailcamera.setOnClickListener {
//           var NAME = intent.getStringExtra("NAME")
//            Intent(this, CameraDetailActivity::class.java).putExtra("NAME", NAME)
            val intent = Intent(this, CameraXDetail::class.java)
            startActivity(intent)
//            startActivityForResult(Intent(this, CameraXDetail::class.java), REQUEST_TAKE_PHOTO_20)
        }
    }

}


//          /**firebase Storage Reference*/
//    private fun includesForCreateReference() {
//        val storage = Firebase.storage
//
//        // ## Create a Reference
//
//        // [START create_storage_reference]
//        // Create a storage reference from our app
//        var storageRef = storage.reference
//        // [END create_storage_reference]
//
//        // [START create_child_reference]
//        // Create a child reference
//        // imagesRef now points to "images"
//        var imagesRef: StorageReference? = storageRef.child("images")
//
//        // Child references can also take paths
//        // spaceRef now points to "images/space.jpg
//        // imagesRef still points to "images"
//        var spaceRef = storageRef.child("storage/emulated/0/Android/media/com.android.skinex/Skinex/")
//        // [END create_child_reference]
//
//        // ## Navigate with References
//
//        // [START navigate_references]
//        // parent allows us to move our reference to a parent node
//        // imagesRef now points to 'images'
//        imagesRef = spaceRef.parent
//
//        // root allows us to move all the way back to the top of our bucket
//        // rootRef now points to the root
//        val rootRef = spaceRef.root
//        // [END navigate_references]
//
//        // [START chain_navigation]
//        // References can be chained together multiple times
//        // earthRef points to 'images/earth.jpg'
//        val earthRef = spaceRef.parent?.child("earth.jpg")
//
//        // nullRef is null, since the parent of root is null
//        val nullRef = spaceRef.root.parent
//        // [END chain_navigation]
//
//        // ## Reference Properties
//
//        // [START reference_properties]
//        // Reference's path is: "images/space.jpg"
//        // This is analogous to a file path on disk
//        spaceRef.path
//
//        // Reference's name is the last segment of the full path: "space.jpg"
//        // This is analogous to the file name
//        spaceRef.name
//
//        // Reference's bucket is the name of the storage bucket that the files are stored in
//        spaceRef.bucket
//        // [END reference_properties]
//
//        // ## Full Example
//
//        // [START reference_full_example]
//        // Points to the root reference
//        storageRef = storage.reference
//
//        // Points to "images"
//        imagesRef = storageRef.child("images")
//
//        // Points to "images/space.jpg"
//        // Note that you can use variables to create child values
//        val fileName = "space.jpg"
//        spaceRef = imagesRef.child(fileName)
//
//        // File path is "images/space.jpg"
//        val path = spaceRef.path
//
//        // File name is "space.jpg"
//        val name = spaceRef.name
//
//        // Points to "images"
//        imagesRef = spaceRef.parent
//        // [END reference_full_example]
//        includesForUploadFiles()e
//    }


//    fun includesForUploadFiles() {
//        val storage = Firebase.storage
//
//        // [START upload_create_reference]
//        // Create a storage reference from our app
//        val storageRef = storage.reference
//
//        // Create a reference to "mountains.jpg"
//        val mountainsRef = storageRef.child("2021-01-26-09-17-24-314.jpg")
//
//        // Create a reference to 'images/mountains.jpg'
//        val mountainImagesRef = storageRef.child("storage/Android/media/com.android.skinex/Skinex/2021-01-26-09-17-24-314.jpg")
//
//        // While the file names are the same, the references point to different files
//        mountainsRef.name == mountainImagesRef.name // true
//        mountainsRef.path == mountainImagesRef.path // false
//        // [END upload_create_reference]
//
//
//
//        // [START upload_stream]
//        val baos = ByteArrayOutputStream()
//        val data = baos.toByteArray()
//        var uploadTask = mountainsRef.putBytes(data)
//        val stream = FileInputStream(File("2021-01-26-09-17-24-314.jpg"))
//
//        uploadTask = mountainsRef.putStream(stream)
//        uploadTask.addOnFailureListener {
//            // Handle unsuccessful uploads
//        }.addOnSuccessListener { taskSnapshot ->
//            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
//            // ...
//        }
//        // [END upload_stream]
//
//
//
//
//    }