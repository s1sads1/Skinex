package com.android.skinex.result_Consulting

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.android.skinex.R
import com.android.skinex.camera2Api.CameraXDetail
import com.android.skinex.camera2Api.CameraXReturn
import com.android.skinex.databinding.ResultInfoBinding
import com.android.skinex.dataclass.AnalyInfo
import com.android.skinex.publicObject.Analy
import com.android.skinex.publicObject.Camera
import com.android.skinex.publicObject.Visiter
import com.android.skinex.qrscanner.QrScanner
import com.android.skinex.restApi.ApiUtill
import com.android.skinex.util.ScreenCapture
import com.davemorrissey.labs.subscaleview.ImageSource
import com.github.mikephil.charting.utils.Utils
import com.google.firebase.storage.FirebaseStorage
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.guide.*
import kotlinx.android.synthetic.main.result_info.*
import kotlinx.android.synthetic.main.result_info.view.*
import okio.ByteString.decodeBase64
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class ResultInfoActivity : AppCompatActivity() {

    private lateinit var binding: ResultInfoBinding

////    private lateinit var imageView: ImageView
//    private lateinit var touchMode : TOUCH_MODE
//    private lateinit var matrix: Matrix //기존 매트릭스
//    private lateinit var savedMatrix:Matrix //작업 후 이미지에 매핑할 매트릭스
//    private lateinit var startPoint: PointF //한손가락 터치 이동 포인트
//    private lateinit var midPoint:PointF //두손가락 터치 시 중신 포인트
//    private var oldDistance:Float = 0.toFloat() //터치 시 두손가락 사이의 거리
//    private var oldDegree = 0.0 // 두손가락의 각도
//
//    internal enum class TOUCH_MODE {
//        NONE, // 터치 안했을 때
//        SINGLE, // 한손가락 터치
//        MULTI //두손가락 터치
//    }
//
//    private val onTouch = object: View.OnTouchListener {
//       override fun onTouch(v:View, event:MotionEvent):Boolean {
//            if (v.equals(binding.longDistanceShot4))
//            {
//                val action = event.getAction()
//                when (event.getAction() and  MotionEvent.ACTION_MASK) {
//                    MotionEvent.ACTION_DOWN -> {
//                        touchMode = TOUCH_MODE.SINGLE
//                        donwSingleEvent(event)
//                    }
//                    MotionEvent.ACTION_POINTER_DOWN -> if (event.getPointerCount() === 2)
//                    { // 두손가락 터치를 했을 때
//                        touchMode = TOUCH_MODE.MULTI
//                        downMultiEvent(event)
//                    }
//                    MotionEvent.ACTION_MOVE -> if (touchMode == TOUCH_MODE.SINGLE)
//                    {
//                        moveSingleEvent(event)
//                    }
//                    else if (touchMode == TOUCH_MODE.MULTI)
//                    {
//                        moveMultiEvent(event)
//                    }
//                    MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> touchMode = TOUCH_MODE.NONE
//                }
//            }
//            return true
//        }
//    }

//        var firebaseString :String = "https://firebasestorage.googleapis.com/v0/b/wpias-94d18.appspot.com/o/storage%2Femulated%2F0%2FAndroid%2Fmedia%2Fcom.android.skinex%2FSkinex%2F2021-01-26-17-14-12-332.jpg?alt=media&token=b1c15a56-1e72-415c-a4eb-1e7870f2bdf8"

//    var storageUrl = "https://firebasestorage.googleapis.com/v0/b/wpias-94d18.appspot.com/o/storage%2Femulated%2F0%2FAndroid%2Fmedia%2Fcom.android.skinex%2FSkinex%2F${Visiter.Visi.firebaseurl}.jpg?alt=media"

    var storageUrl = "https://firebasestorage.googleapis.com/v0/b/wpias-94d18.appspot.com/o/data%2Fuser%2F0%2Fcom.android.skinex%2Fcache%2F" +
            "${Camera.cam.camerauri2}?alt=media"

    var MYyear = 0
    var MYmonth = 0
    var MYday = 0





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ResultInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)





//        matrix = Matrix()
//        savedMatrix = Matrix()
////        imageView = findViewById(R.id.imageView)
//        binding.longDistanceShot4.setOnTouchListener(onTouch)
//        binding.longDistanceShot4.setScaleType(ImageView.ScaleType.MATRIX)



        info()
        goguide()
        imageUp()
        recapture()
        resultsubmit()
        sshConnect()

        var r = Resources.getSystem()
        var config = r.getConfiguration()
        onConfigurationChanged(config)



    }

//    private fun getMidPoint(e:MotionEvent):PointF {
//        val x = (e.getX(0) + e.getX(1)) / 2
//        val y = (e.getY(0) + e.getY(1)) / 2
//        return PointF(x, y)
//    }
//    private fun getDistance(e:MotionEvent):Float {
//        val x = e.getX(0) - e.getX(1)
//        val y = e.getY(0) - e.getY(1)
//        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
//    }
//
//    private fun donwSingleEvent(event:MotionEvent) {
//        savedMatrix.set(matrix)
//        startPoint = PointF(event.getX(), event.getY())
//    }
//
//    private fun downMultiEvent(event:MotionEvent) {
//        oldDistance = getDistance(event)
//        if (oldDistance > 5f)
//        {
//            savedMatrix.set(matrix)
//            midPoint = getMidPoint(event)
//            val radian = Math.atan2(event.getY().toDouble() - midPoint.y, event.getX().toDouble() - midPoint.x)
//            oldDegree = (radian * 180) / Math.PI
//        }
//    }
//    private fun moveSingleEvent(event:MotionEvent) {
//        matrix.set(savedMatrix)
//        matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y)
//        binding.longDistanceShot4.setImageMatrix(matrix)
//    }
//    private fun moveMultiEvent(event:MotionEvent) {
//        val newDistance = getDistance(event)
//        if (newDistance > 5f)
//        {
//            matrix.set(savedMatrix)
//            val scale = newDistance / oldDistance
//            matrix.postScale(scale, scale, midPoint.x, midPoint.y)
//            val nowRadian = Math.atan2(event.getY().toDouble() - midPoint.y, event.getX().toDouble() - midPoint.x)
//            val nowDegress = (nowRadian * 180) / Math.PI
//            val degree = (nowDegress - oldDegree).toFloat()
//            matrix.postRotate(degree, midPoint.x, midPoint.y)
//            binding.longDistanceShot4.setImageMatrix(matrix)
//        }
//    }

    fun getScreenShot(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap( 1300,1200,
            Bitmap.Config.ARGB_8888)
        Log.d("getScreenShot: ", binding.longDistanceShot4.height.toString()+":" + binding.longDistanceShot4.width.toString() )
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) bgDrawable.draw(canvas)
        else canvas.drawColor(resources.getColor(R.color.screenshot_background))
        view.draw(canvas)

        return returnedBitmap

    }

    fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? { // File name like "image.png"
        //create a file to write bitmap data
        var file: File? = null
        return try {
            file = File(Environment.getExternalStorageDirectory().toString() + File.separator + fileNameToSave)
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


//    val bmp: Bitmap? = null
//    val stream = ByteArrayOutputStream()
//
//    var byteArray = stream.toByteArray()
//    fun screenShot() {
//
//
//        bmp!!.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//
//        var fileInputStream: FileInputStream? = null
//        val file = File("yourfile")
//        byteArray = ByteArray(file.length().toInt())
//        try {
//            //convert file into array of bytes
//            fileInputStream = FileInputStream(file)
//            fileInputStream.read(bFile)
//            fileInputStream.close()
//            //convert array of bytes into file
//            val fileOuputStream = FileOutputStream("C:\\testing2.txt")
//            fileOuputStream.write(bFile)
//            fileOuputStream.close()
//            println("Done")
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }



    //촬영 클릭시 전체촬영 이벤트
    fun goguide(){


        binding.save.setOnClickListener {

            var bitmap: Bitmap = getScreenShot(binding.longDistanceShot4)
            var fileToBitmap :File = bitmapToFile(bitmap, "screenShot")!!

            Visiter.Visi.screenshot = bitmap

            val intent = Intent(this, ResultActivity::class.java)
            startActivity(intent)
//            ScreenCapture.captureView(findViewById(com.android.skinex.R.id.longDistanceShot4))
        }
    }

    fun imageUp() {
//        binding.shortDistanceShot2.setImageURI(Visiter.Visi.camerauri1.toUri())
//        Glide.with(this).load(Visiter.Visi.camerauri2).into(findViewById<ImageView>(R.id.longDistanceShot4))
//   binding.longDistanceShot4.setImageURI(Visiter.Visi.camerauri2.toUri())

        binding.longDistanceShot4.setImage(ImageSource.uri(Camera.cam.camerauri1))
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
            fileUpload()

            Toast.makeText(
                this@ResultInfoActivity, "잠시만 기다려주세요",
                Toast.LENGTH_LONG
            ).show()

        }
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

        binding.btnSsh.setOnClickListener {


//            var XTL  = (binding.text.x - binding.longDistanceShot4.x).toString()
            var XTL  = "150"
//            var YTL  = (binding.text.y - binding.longDistanceShot4.y).toString()
           var YTL = "260"
//            var XBR  = (binding.textstandard.x - binding.longDistanceShot4.x).toString()
            var XBR = "410"
//            var YBR  = (binding.textstandard.y - binding.longDistanceShot4.y).toString()
            var YBR ="490"
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
            ApiUtill().getSshConnection().sshConnect(storageUrl, XTL, YTL, XBR, YBR)
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
        }
    }

    fun fileUpload() {
//Log.d("Basic", savedUri.toString() + photoFile.toString())
//        val stream = FileInputStream(File(Visiter.Visi.camerauri1))
        val stream2 = FileInputStream(File(Camera.cam.camerauri1))

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
//        // Points to the root reference
//       val storageRef = storage.reference
//
//// Points to "images"
//       val imagesRef = storageRef.child("images")
//
//// Points to "images/space.jpg"
//// Note that you can use variables to create child values
//        val fileName = "space.jpg"
//       val spaceRef = imagesRef.child(fileName)
//
//// File path is "images/space.jpg"
//        val path = spaceRef.path
//
//// File name is "space.jpg"
//        val name = spaceRef.name
//
//// Points to "images"
////        imagesRef = spaceRef.parent
//
//        // File or Blob
//        var file = Uri.fromFile(File("path/to/mountains.jpg"))
//
//// Create the file metadata
//        var metadata = storageMetadata {
//            contentType = "image/jpeg"
//        }
//
//// Upload file and metadata to the path 'images/mountains.jpg'
//        var uploadTask = storageRef.child("images/${file.lastPathSegment}").putFile(file, metadata)

// Listen for state changes, errors, and completion of the upload.
// You'll need to import com.google.firebase.storage.ktx.component1 and
// com.google.firebase.storage.ktx.component2
//        uploadTask.addOnProgressListener { (uploadTask.snapshot.bytesTransferred, uploadTask.snapshot.totalByteCount) ->
//            val progress = (100.0 * bytesTransferred) / totalByteCount
//            Log.d(TAG, "Upload is $progress% done")
//        }.addOnPausedListener {
//            Log.d(TAG, "Upload is paused")
//        }.addOnFailureListener {
//            // Handle unsuccessful uploads
//        }.addOnSuccessListener {
//            // Handle successful uploads on complete
//            // ...
//        }
    }


}

//    fun SecondDate() {
//        var sd = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            LocalDate.now().toString()
//        } else {
//            SimpleDateFormat("yyyyMMddkkmmss").format(Calendar.getInstance().time)
//        }
//
//        var year2 = sd.split("-")[0]
//        var month2 = (sd.split("-")[1]).padStart(2, '0')
//        var day2 = sd.split("-")[2].padStart(2, '0')
//        var hour2 = sd.split("-")[3].padStart(2,'0')
//        var minute2 = sd.split("-")[4].padStart(2,'0')
//        var second2 = sd.split("-")[5].padStart(2,'0')
//
//        MYyear2 = year2.toInt()
//        MYmonth2 = month2.toInt()
//        MYday2 = day2.toInt()
//        MYhour2 = hour2.toInt()
//        MYminute2 = minute2.toInt()
//        MYsecond2 = second2.toInt()
// 어 ? 왜 안돼는거야 어?
//        binding.patientNum.setText("$MYyear2${MYmonth2.toString().padStart(2,'0')}${MYday2.toString().padStart(2,'0')}${MYhour2.toString().padStart(2,'0')}${MYminute2.toString().padStart(2,'0')}${MYsecond2.toString().padStart(2,'0')}")
//
//    }