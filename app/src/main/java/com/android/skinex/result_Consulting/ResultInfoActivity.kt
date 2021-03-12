package com.android.skinex.result_Consulting

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.android.skinex.R
import com.android.skinex.activity.Guide
import com.android.skinex.camera2Api.CameraX
import com.android.skinex.databinding.ResultInfoBinding
import com.android.skinex.dataclass.AnalyInfo
import com.android.skinex.publicObject.Analy
import com.android.skinex.publicObject.Camera
import com.android.skinex.publicObject.Visiter
import com.android.skinex.qrscanner.QrScanner
import com.android.skinex.restApi.ApiUtill
import com.davemorrissey.labs.subscaleview.ImageSource
import com.google.firebase.storage.FirebaseStorage
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.guide.*
import kotlinx.android.synthetic.main.result_info.*
import kotlinx.android.synthetic.main.result_info.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class ResultInfoActivity : AppCompatActivity() {

    private lateinit var binding: ResultInfoBinding

    var storageUrl = "https://firebasestorage.googleapis.com/v0/b/wpias-94d18.appspot.com/o/data%2Fuser%2F0%2Fcom.android.skinex%2Fcache%2F" +
            "${Camera.cam.camerauri2}?alt=media"
    var storageUrl_galley= "https://firebasestorage.googleapis.com/v0/b/wpias-94d18.appspot.com/o/Skinex%2F" +
            "${Visiter.Visi.firebaseurl}?alt=media"
    var testUrl = "https://firebasestorage.googleapis.com/v0/b/wpias-94d18.appspot.com/o/Compare%2F%EC%A0%91%EC%B4%89%ED%99%94%EC%83%81%2F%E1%84%8B%E1%85%A3%E1%87%80%E1%84%8B%E1%85%B3%E1%86%AB2%E1%84%83%E1%85%A902.png?alt=media&token=76e9986b-95f4-4876-91d5-6932dff7b4a4"

    var MYyear = 0
    var MYmonth = 0
    var MYday = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ResultInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        info()
//        goguide()
        imageUp()
        recapture()
        resultsubmit()
//        sshConnect()
        guide()

        var r = Resources.getSystem()
        var config = r.getConfiguration()
        onConfigurationChanged(config)

    }



    fun getScreenShot(view: View): Bitmap {

        view.setDrawingCacheEnabled(true)
        val bmp: Bitmap = view.getDrawingCache()

        return bmp

    }

    fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? { // File name like "image.png"
        //create a file to write bitmap data
        var file: File? = null
        return try {
            file = File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + fileNameToSave
            )
            file.createNewFile()

            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bos) // YOU can also save it in JPEG
            val bitmapdata = bos.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }

    fun saveBitmaptoJpeg(bitmap: Bitmap, folder: String, name: String):String {
        val ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath()
        // Get Absolute Path in External Sdcard
        val folder_name = "/" + folder + "/"
        val string_path = ex_storage + folder_name
        val file_name = name + ".jpg"

        val file_path:File

        file_path = File(string_path + file_name)
        Log.d("filepath", string_path + file_name)
        try
        {
            file_path.createNewFile()
            //if (!file_path.isDirectory())
            //{
            //    file_path.mkdirs()
            //}
            val out = FileOutputStream(file_path)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out)
            out.flush()
            out.close()
        }
        catch (exception: FileNotFoundException) {
            Log.e("FileNotFoundException", exception.message)
        }
        catch (exception: IOException) {
            Log.e("IOException", exception.message)
        }
        return string_path + file_name
    }


//    override fun onBackPressed() {
////        super.onBackPressed()
//    }

    fun info() {
        var now = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now().toString()
        } else {
            SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
        }

        var year = now.split("-")[0]
        var month = (now.split("-")[1]).padStart(2, '0')
        var day = now.split("-")[2].padStart(2, '0')

        MYyear = year.toInt()
        MYmonth = month.toInt()
        MYday = day.toInt()

        binding.patientName.setText(Visiter.Visi.name)
        binding.patientGender.setText(Visiter.Visi.gender)
        binding.patientBirth.setText(Visiter.Visi.birth)
        binding.patientBurnday.setText(Visiter.Visi.burndate)
        //binding.patientInputDay.setText("${year}-${(month).toString().padStart(2,'0')}-${day.toString().padStart(2, '0')}")
        binding.patientInputDay.setText(
            "$MYyear-${MYmonth.toString().padStart(2, '0')}-${
                MYday.toString().padStart(
                    2,
                    '0'
                )
            }"
        )
        //binding.genderRadioGroup.check(Visiter.Visi.gender)

        var idBirth = Visiter.Visi.birth
        idBirth = idBirth.replace("-", "")
        var reMYyear = MYyear.toString()
        reMYyear = reMYyear.replace("20", "")
        reMYyear = reMYyear.replace("19", "")
        Log.d("xxoo년도 중 xx를 잘라서 받아옴 : ", reMYyear)
        //.toString().padStart(2,'0') 를 쓴 이유는 MYmonth 가 01인 경우에 저걸 안쓰면 1만 가져옴 01이아님
        binding.patientNum.setText(
            idBirth + "$reMYyear${MYmonth.toString().padStart(2, '0')}${
                MYday.toString().padStart(
                    2,
                    '0'
                )
            }"
        )
    }

    fun imageUp() {
//        binding.shortDistanceShot2.setImageURI(Visiter.Visi.camerauri1.toUri())
//        Glide.with(this).load(Visiter.Visi.camerauri2).into(findViewById<ImageView>(R.id.longDistanceShot4))
//   binding.longDistanceShot4.setImageURI(Visiter.Visi.camerauri2.toUri())

//        binding.longDistanceShot4.setImage(ImageSource.uri(Camera.cam.camerauri1))\
        var secondIntent = getIntent()
        var gallary = secondIntent.getStringExtra("gallary")
        if(gallary =="gallary") {
            binding.longDistanceShot4.setImage(ImageSource.bitmap(Visiter.Visi.camerauri))

//            binding.longDistanceShot4.setImageBitmap(Visiter.Visi.camerauri)

        }else {
//            val photoView = binding.longDistanceShot4
//            photoView.setImageURI(Camera.cam.camerauri1.toUri())
            binding.longDistanceShot4.setImage(ImageSource.uri(Camera.cam.camerauri1))
        }
    }

//  override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)


//    }

    fun screenShot() {
        val v1: View = binding.longDistanceShot4.rootView
        v1.buildDrawingCache()
        v1.setDrawingCacheEnabled(true)

        val saveBitmap: Bitmap = v1.getDrawingCache()

    }
//    private fun takeScreenshot() {
//        val now = Date()
//        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)
//        try
//        {
//            // image naming and path to include sd card appending name you choose for file
//            val mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg"
//            // create bitmap screen capture
//            val v1 = getWindow().getDecorView().getRootView()
//            v1.setDrawingCacheEnabled(true)
//            val bitmap = Bitmap.createBitmap(v1.getDrawingCache())
//            v1.setDrawingCacheEnabled(false)
//            val imageFile = File(mPath)
//            val outputStream = FileOutputStream(imageFile)
//            val quality = 100
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
//            outputStream.flush()
//            outputStream.close()
//            openScreenshot(imageFile)
//        }
//        catch (e: Throwable) {
//            // Several error may come out with file handling or DOM
//            e.printStackTrace()
//        }
//    }
//
//    private fun openScreenshot(imageFile: File) {
//        val intentShot = Intent()
//        intentShot.action = Intent.ACTION_VIEW
//        val uri: Uri = Uri.fromFile(imageFile)
//        intentShot.setDataAndType(uri, "image/*")
//        startActivity(intentShot)
//    }



    //    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//
//        binding.longDistanceShot4.setImage(ImageSource.uri(Visiter.Visi.camerauri2))
//
//    }
    fun recapture() {
//        binding.btnRecapture1.setOnClickListener {
//
//            binding.shortDistanceShot2.setImageBitmap(getScreenShot(findViewById(R.id.screenShotLayout)))
//        }
//        binding.btnRecapture1.setOnClickListener{
//            startActivity(Intent(this, CameraXReturn::class.java))
//        }

        binding.btnRecapture2.setOnClickListener {

            val integrator = IntentIntegrator(this)
            integrator.setBarcodeImageEnabled(true)
            integrator.setBeepEnabled(false)
            integrator.captureActivity = QrScanner::class.java
            integrator.setOrientationLocked(false) //세로모드
            integrator.initiateScan()
        }
    }



    fun resultsubmit() {
        binding.resultsubmit.setOnClickListener {
            bitmap()
//Log.d("bitmaptupload", Visiter.Visi.gallary)
            fileUpload()

            Toast.makeText(
                this@ResultInfoActivity, "잠시만 기다려주세요",
                Toast.LENGTH_LONG
            ).show()


        }
    }
    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
    //촬영 클릭시 전체촬영 이벤트
    fun bitmap() {
        var bitmap: Bitmap = getScreenShot(binding.longDistanceShot4)
//        var fileToBitmap :File = bitmapToFile(bitmap, "screenShot")!!
        var time = SimpleDateFormat(
            FILENAME_FORMAT, Locale.US
        ).format(System.currentTimeMillis())
        var btf = saveBitmaptoJpeg(bitmap, "screenshot", time)
        Visiter.Visi.screenshot = bitmap
        Visiter.Visi.capture = btf
        binding.screenShot.setImageURI(btf.toUri())
        Log.d("bitmap: ", btf)
    }
    fun goguide(){

//        binding.save.setOnClickListener {

        val intent = Intent(this, ResultActivity::class.java)
        startActivity(intent)
//            ScreenCapture.captureView(findViewById(com.android.skinex.R.id.longDistanceShot4))
//        }
    }

//    fun onTouchXY() {
//        binding.scrollLayout.setOnTouchListener { v, event ->
//            val action = event.getAction()
//            val curX = event.getX() //눌린 곳의 X좌표
//            val curY = event.getY() //눌린 곳의 Y좌표
//            if (action == MotionEvent.ACTION_DOWN) { //처음 눌렸을 때
//                Log.d("Touch", "손가락 눌림 : " + curX + ", " + curY)
//            } else if (action == MotionEvent.ACTION_MOVE) { //누르고 움직였을 때
//                Log.d("Touch", "손가락 움직임 : " + curX + ", " + curY)
//            } else if (action == MotionEvent.ACTION_UP) { //누른걸 뗐을 때
//                Log.d("Touch", "손가락 뗌 : " + curX + ", " + curY)
//            }
//            true
//        }
//    }

    fun sshConnect() {

//        binding.btnSsh.setOnClickListener {


//            var XTL  = (binding.text.x - binding.longDistanceShot4.x).toString()
        var XTL  = "172"
//            var YTL  = (binding.text.y - binding.longDistanceShot4.y).toString()
        var YTL = "135"
//            var XBR  = (binding.textstandard.x - binding.longDistanceShot4.x).toString()
        var XBR = "352"
//            var YBR  = (binding.textstandard.y - binding.longDistanceShot4.y).toString()
        var YBR ="265"
        Log.d("XTL", XTL)
        Log.d("binding.longDistanceShot4.left", binding.longDistanceShot4.left.toString())
        Log.d("binding.longDistanceShot4.right", binding.longDistanceShot4.right.toString())
        Log.d("binding.longDistanceShot4.top", binding.longDistanceShot4.top.toString())
        Log.d("binding.longDistanceShot4.bottom", binding.longDistanceShot4.bottom.toString())
        Log.d("binding.text.x", binding.text.x.toString())
        Log.d("image.text.x", binding.longDistanceShot4.x.toString())
//            Log.d("standard.text.x", binding.textstandard.x.toString())
        Log.d("YTL", YTL)
        Log.d("binding.text.y", binding.text.y.toString())
        Log.d("image.text.y", binding.longDistanceShot4.y.toString())
//            Log.d("standard.text.y", binding.textstandard.y.toString())
        ApiUtill().getSshConnection().sshConnect(testUrl, XTL, YTL, XBR, YBR)
            .enqueue(object : Callback<AnalyInfo> {

                override fun onResponse(call: Call<AnalyInfo>, response: Response<AnalyInfo>) {

                    Log.d("response.body", response.body().toString())
                    if (response.isSuccessful) {
                        var sshresponse = response.body()
                        var degree_output_key = sshresponse!!.degree_output[0].keys.toString()
                        var degree_output_key2 = sshresponse!!.degree_output[1].keys.toString()
                        var degree_output_key3 = sshresponse!!.degree_output[2].keys.toString()
                        var degree_output_key4 = sshresponse!!.degree_output[3].keys.toString()
                        var degree_output_key5 = sshresponse!!.degree_output[4].keys.toString()
                        var degree_output_key6 = sshresponse!!.degree_output[5].keys.toString()
                        var degree_split = degree_output_key.substring(
                            1,
                            degree_output_key.length - 1
                        )
                        var degree_split2 = degree_output_key2.substring(
                            1,
                            degree_output_key2.length - 1
                        )
                        var degree_split3 = degree_output_key3.substring(
                            1,
                            degree_output_key3.length - 1
                        )
                        var degree_split4 = degree_output_key4.substring(
                            1,
                            degree_output_key4.length - 1
                        )
                        var degree_split5 = degree_output_key5.substring(
                            1,
                            degree_output_key5.length - 1
                        )
                        var degree_split6 = degree_output_key6.substring(
                            1,
                            degree_output_key6.length - 1
                        )
                        var degree_output =
                            sshresponse!!.degree_output[0].getValue(degree_split).toString()
                        var degree_output2 = sshresponse!!.degree_output[1].getValue(
                            degree_split2
                        ).toString()
                        var degree_output3 = sshresponse!!.degree_output[2].getValue(
                            degree_split3
                        ).toString()
                        var degree_output4 = sshresponse!!.degree_output[3].getValue(
                            degree_split4
                        ).toString()
                        var degree_output5 = sshresponse!!.degree_output[4].getValue(
                            degree_split5
                        ).toString()
                        var degree_output6 = sshresponse!!.degree_output[5].getValue(
                            degree_split6
                        ).toString()
                        Analy.Analy.degree_key = degree_split
                        Analy.Analy.degree_key2 = degree_split2
                        Analy.Analy.degree_key3 = degree_split3
                        Analy.Analy.degree_key4 = degree_split4
                        Analy.Analy.degree_key5 = degree_split5
                        Analy.Analy.degree_key6 = degree_split6
                        Analy.Analy.degree_value = degree_output
                        Analy.Analy.degree_value2 = degree_output2
                        Analy.Analy.degree_value3 = degree_output3
                        Analy.Analy.degree_value4 = degree_output4
                        Analy.Analy.degree_value5 = degree_output5
                        Analy.Analy.degree_value6 = degree_output6
                        Log.d("Ssh", sshresponse.toString())
//                            var degreeToString = Objects.toString(degree_output)

                        Log.d("degree_output", degree_output.toString())
//                            Log.d("degreeToString",degreeToString.toString())
//                            Log.d("degree_정상", degree_output.toString())
                        Log.d("degree: ", degree_output_key2 + degree_output2)
                        Toast.makeText(this@ResultInfoActivity, "성공!!", Toast.LENGTH_SHORT)
                            .show()
                        goguide()
                    } else {
                        Toast.makeText(this@ResultInfoActivity, "실패", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<AnalyInfo>, t: Throwable) {
                    Log.d("sshConnect()", "sshConnect()")

                    Toast.makeText(
                        this@ResultInfoActivity,
                        t.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
//        }
    }

    fun getImageUri(inContext: Context, inImage:Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
        return Uri.parse(path)
    }

    fun guide() {
        binding.goguide.setOnClickListener {
            val intent = Intent(this, Guide::class.java)
            startActivity(intent)
        }
    }

    fun fileUpload() {

        var secondIntent = getIntent()
        var gallary = secondIntent.getStringExtra("gallary")
        if(gallary =="gallary") {
            // Create a storage reference from our app
            val storageRef = FirebaseStorage.getInstance().reference

// Create a reference to "mountains.jpg"
            var time = SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis())
            Visiter.Visi.firebaseurl = time
            val mountainsRef = storageRef.child("Skinex/${Visiter.Visi.firebaseurl}")

// Create a reference to 'images/mountains.jpg'
//            val mountainImagesRef = storageRef.child("images/mountains.jpg")

            binding.longDistanceShot4.isDrawingCacheEnabled = true
            binding.longDistanceShot4.buildDrawingCache()
//            val bitmap = (binding.longDistanceShot4.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            Visiter.Visi.camerauri.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            var uploadTask = mountainsRef.putBytes(data)
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
            }
//            val stream3 = FileInputStream(File(Visiter.Visi.gallary))
//            val storageRef3 = FirebaseStorage.getInstance().reference
//            val mountainsRef3 = storageRef3.child(Visiter.Visi.gallary)
//            val uploadTask3 = mountainsRef3.putStream(stream3)
//            uploadTask3.addOnFailureListener {
//                // Handle unsuccessful uploads
//            }.addOnSuccessListener { taskSnapshot3 ->
//                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
//                // ...
//            }
            sshConnect()
        }else {
//Log.d("Basic", savedUri.toString() + photoFile.toString())
//        val stream = FileInputStream(File(Visiter.Visi.camerauri1))
            //val stream2 = FileInputStream(File(Visiter.Visi.gallary))
            var test = "/storage/emulated/0/test/Screenshot_20190514-223031_Agreement.jpg"
            val stream2 = FileInputStream(File(Camera.cam.camerauri1))
            Log.d("file", Camera.cam.camerauri1)
//            Log.d("uri: ",Camera.cam.camerauri1)

//        val storageRef = FirebaseStorage.getInstance().reference
            val storageRef2 = FirebaseStorage.getInstance().reference

//        val mountainsRef = storageRef.child(Visiter.Visi.camerauri1)
            val mountainsRef2 = storageRef2.child(Camera.cam.camerauri1)


//        val uploadTask = mountainsRef.putStream(stream)
//        uploadTask.addOnFailureListener {
//            // Handle unsuccessful uploads
//        }.addOnSuccessListener { taskSnapshot ->
//            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
//            // ...
//        }
            val uploadTask2 = mountainsRef2.putStream(stream2)
            uploadTask2.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot2 ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
            }
            sshConnect()
        }

    }


}