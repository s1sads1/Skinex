package com.android.skinex.result_Consulting

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import com.android.skinex.camera2Api.CameraXDetailReturn
import com.android.skinex.databinding.ResultImage2Binding
import com.android.skinex.dataclass.AnalyInfo
import com.android.skinex.publicObject.Analy
import com.android.skinex.publicObject.Visiter
import com.android.skinex.restApi.ApiUtill
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import kotlin.math.abs


class ResultDetailImage : AppCompatActivity() {
    private lateinit var binding: ResultImage2Binding

    private lateinit var touchMode : TOUCH_MODE
    private lateinit var matrix: Matrix //기존 매트릭스
    private lateinit var savedMatrix:Matrix //작업 후 이미지에 매핑할 매트릭스
    private lateinit var startPoint: PointF //한손가락 터치 이동 포인트
    private lateinit var midPoint:PointF //두손가락 터치 시 중신 포인트
    private var oldDistance:Float = 0.toFloat() //터치 시 두손가락 사이의 거리
    private var oldDegree = 0.0 // 두손가락의 각도
    private var mScaleFactor = 1f

    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.scaleFactor

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f))


            return true
        }
    }

    private var mScaleDetector = ScaleGestureDetector(this@ResultDetailImage, scaleListener)

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev)
        return true
    }

//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//
//        canvas.apply {
//            save()
//            scale(mScaleFactor, mScaleFactor)
//            // onDraw() code goes here
//            restore()
//        }
//    }


    internal enum class TOUCH_MODE {
        NONE, // 터치 안했을 때
        SINGLE, // 한손가락 터치
        MULTI //두손가락 터치
    }

    var storageUrl =
        "https://firebasestorage.googleapis.com/v0/b/wpias-94d18.appspot.com/o/storage%2Femulated%2F0%2FAndroid%2Fmedia%2Fcom.android.skinex%2FSkinex%2F${Visiter.Visi.firebaseurl}.jpg?alt=media"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ResultImage2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        matrix = Matrix()
        savedMatrix = Matrix()
//        imageView = findViewById(R.id.imageView)
        binding.text.setOnTouchListener(onTouch)
        binding.text.setScaleType(ImageView.ScaleType.MATRIX)



        imageUp()
        recapture()
        resultBtn()
        sshConnect()
        resultsubmit()

    }

    //    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        return super.onTouchEvent(event)
//    }
    private val onTouch = object: View.OnTouchListener {
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            val parentWidth = (v.getParent() as ViewGroup).getWidth() // 부모 View 의 Width
            val parentHeight = (v.getParent() as ViewGroup).getHeight() // 부모 View 의 Height
            if (v.equals(v))
            {
                val action = event.getAction()
                when (action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_DOWN -> {
                        touchMode = TOUCH_MODE.SINGLE
//                        donwSingleEvent(event)
                        var oldXvalue = event.x
                        var oldYvalue = event.y
                    }
                    MotionEvent.ACTION_POINTER_DOWN -> if (event.getPointerCount() === 2) { // 두손가락 터치를 했을 때
                        touchMode = TOUCH_MODE.MULTI
//                        downMultiEvent(event)
                        Log.d("Multi", "Multi")
                    }
                    MotionEvent.ACTION_MOVE -> if (touchMode == TOUCH_MODE.SINGLE) {
                        v.x = (v.x + (event.x) - (v.width / 2))
                        v.y = (v.y + (event.y) - (v.height / 2))
                    } else if (event.getPointerCount() === 2) {

                        Log.d("Multi.move", "Multi.move")


//                       v.width =(v.width + Math.abs(event.getX(0) - (v.width / 2)) + Math.abs(event.getX(1) - (v.width/2))).toInt()
                         var x = (v.width + (event.getX(0)-v.x - (v.width / 2)) + (event.getX(1)-v.x - (v.width/2))).toInt()
                      var y = (v.height + (event.getY(0)-v.y - (v.height / 2)) + (event.getY(1)-v.y - (v.height/2))).toInt()
                        Log.d("event.getX(0)",event.getX(0).toString())

                        var x2 = (v.width + (event.x) - (v.width / 2))
                        var y2 = (v.height + (event.y) - (v.height / 2))
//                        binding.text.layoutParams.width = 100000
                        binding.text.setLayoutParams(ConstraintLayout.LayoutParams(x2.toInt(),y2.toInt()
                        ))
   //
                    //                        editText.setLayoutParams(LinearLayout.LayoutParams(300, 300))

//                        binding.text.layoutParams.height = (v.height + Math.abs(event.getY(0) - (v.height / 2)) + abs(event.getY(1) - (v.height/2))).toInt()
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP ->
                    if (touchMode == TOUCH_MODE.SINGLE) {
                        if (v.x < 0) {
                            v.x = (0.toFloat())
                        } else if ((v.x + v.width) > parentWidth) {
                            v.x = (parentWidth.toFloat() - v.width.toFloat())
                        }
                        if (v.y < 0) {
                            v.y = (0.toFloat())
                        } else if ((v.y + v.height) > parentHeight) {
                            v.y = (parentHeight.toFloat() - v.height.toFloat())
                        }
                    } else if(touchMode == TOUCH_MODE.MULTI) {
                        touchMode =
                            TOUCH_MODE.NONE
                    }
                }
            }
//            var pointerCount = event.pointerCount
//
//            if (pointerCount > 1) {
//                if (event.action === MotionEvent.ACTION_POINTER_DOWN) {
//                    Log.d("Click","Click")
//
//                    // 뷰 누름
//                    var X1 = event.getX(0)
//                    var X2 = event.getX(1)
//
//                    var Y1 = event.getY(0)
//                    var Y2 = event.getY(1)
//                    Log.d(
//                        "viewTest2",
//                        "X1 : " + X1 + " X2 : " +X2 + "Y1 : " + Y1 + " Y2 : " +Y2
//                    ) // View 내부에서 터치한 지점의 상대 좌표값.
//                    Log.d("viewTest", "v.getX() : " + v.getX()) // View 의 좌측 상단이 되는 지점의 절대 좌표값.
//                    Log.d(
//                        "viewTest2",
//                        "RawX : " + event.getRawX() + " RawY : " + event.getRawY()
//                    ) // View 를 터치한 지점의 절대 좌표값.
//                    Log.d(
//                        "viewTest2",
//                        "v.getHeight : " + v.getHeight() + " v.getWidth : " + v.getWidth()
//                    ) // View 의 Width, Height
//                } else if (event.action === MotionEvent.ACTION_MOVE) {
//                    // 뷰 이동 중
//                    v.x = (v.x + (event.x) - (v.width / 2))
//                    v.y = (v.y + (event.y) - (v.height / 2))
//                } else if (event.action === MotionEvent.ACTION_UP) {
//                    // 뷰에서 손을 뗌
//                    if (v.x < 0) {
//                        v.x = (0.toFloat())
//                    } else if ((v.x + v.width) > parentWidth) {
//                        v.x = (parentWidth.toFloat() - v.width.toFloat())
//                    }
//                    if (v.y < 0) {
//                        v.y = (0.toFloat())
//                    } else if ((v.y + v.height) > parentHeight) {
//                        v.y = (parentHeight.toFloat() - v.height.toFloat())
//                    }
//                    Log.d("X.move2", v.x.toString())
//                    Log.d("Y.move2", v.y.toString())
//
//                }
//            }else {
//                if (event.action === MotionEvent.ACTION_DOWN) {
//
//                    // 뷰 누름
//                    var oldXvalue = event.x
//                    var oldYvalue = event.y
//                    Log.d(
//                        "viewTest",
//                        "oldXvalue : " + oldXvalue + " oldYvalue : " + oldYvalue
//                    ) // View 내부에서 터치한 지점의 상대 좌표값.
//                    Log.d("viewTest", "v.getX() : " + v.getX()) // View 의 좌측 상단이 되는 지점의 절대 좌표값.
//                    Log.d(
//                        "viewTest",
//                        "RawX : " + event.getRawX() + " RawY : " + event.getRawY()
//                    ) // View 를 터치한 지점의 절대 좌표값.
//                    Log.d(
//                        "viewTest",
//                        "v.getHeight : " + v.getHeight() + " v.getWidth : " + v.getWidth()
//                    ) // View 의 Width, Height
//                } else if (event.action === MotionEvent.ACTION_MOVE) {
//                    // 뷰 이동 중
//                    v.x = (v.x + (event.x) - (v.width / 2))
//                    v.y = (v.y + (event.y) - (v.height / 2))
//                } else if (event.action === MotionEvent.ACTION_UP) {
//                    // 뷰에서 손을 뗌
//                    if (v.x < 0) {
//                        v.x = (0.toFloat())
//                    } else if ((v.x + v.width) > parentWidth) {
//                        v.x = (parentWidth.toFloat() - v.width.toFloat())
//                    }
//                    if (v.y < 0) {
//                        v.y = (0.toFloat())
//                    } else if ((v.y + v.height) > parentHeight) {
//                        v.y = (parentHeight.toFloat() - v.height.toFloat())
//                    }
//                    Log.d("X.move", v.x.toString())
//                    Log.d("Y.move", v.y.toString())
//
//                }
//            }
                return true

        }
    }

    private fun getMidPoint(e: MotionEvent):PointF {
        val x = (e.getX(0) + e.getX(1)) / 2
        val y = (e.getY(0) + e.getY(1)) / 2
        return PointF(x, y)
    }
    private fun getDistance(e: MotionEvent):Float {
        val x = e.getX(0) - e.getX(1)
        val y = e.getY(0) - e.getY(1)
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }

    private fun moveMultiEvent(event: MotionEvent) {
        val newDistance = getDistance(event)
        if (newDistance > 5f)
        {
            matrix.set(savedMatrix)
            val scale = newDistance / oldDistance
            matrix.postScale(scale, scale, midPoint.x, midPoint.y)
//            val nowRadian = Math.atan2(event.getY().toDouble() - midPoint.y, event.getX().toDouble() - midPoint.x)
//            val nowDegress = (nowRadian * 180) / Math.PI
//            val degree = (nowDegress - oldDegree).toFloat()
//            matrix.postRotate(degree, midPoint.x, midPoint.y)
            binding.text.setImageMatrix(matrix)
        }
    }

    private fun downMultiEvent(event: MotionEvent) {
        oldDistance = getDistance(event)
        if (oldDistance > 5f)
        {
            savedMatrix.set(matrix)
            midPoint = getMidPoint(event)
            val radian = Math.atan2(
                event.getY().toDouble() - midPoint.y,
                event.getX().toDouble() - midPoint.x
            )
            oldDegree = (radian * 180) / Math.PI
        }
    }

    fun imageUp() {
        binding.longDistanceShot4.setImageURI(Visiter.Visi.camerauri2.toUri())
    }

    fun recapture() {
        binding.btnRecapture.setOnClickListener {
            startActivity(Intent(this, CameraXDetailReturn::class.java))
        }
    }

    fun resultBtn() {
        binding.resultBtn.setOnClickListener {
            startActivity(Intent(this, ResultActivity::class.java))
        }
    }

    fun resultsubmit() {
        binding.saveBtn.setOnClickListener {
            fileUpload()

            Toast.makeText(
                this@ResultDetailImage, "잠시만 기다려주세요",
                Toast.LENGTH_LONG
            ).show()

        }
    }

    fun sshConnect() {

        binding.analysisBtn.setOnClickListener {


//            var XTL  = (binding.text.x - binding.longDistanceShot4.x).toString()
            var XTL = "150"
//            var YTL  = (binding.text.y - binding.longDistanceShot4.y).toString()
            var YTL = "260"
//            var XBR  = (binding.textstandard.x - binding.longDistanceShot4.x).toString()
            var XBR = "410"
//            var YBR  = (binding.textstandard.y - binding.longDistanceShot4.y).toString()
            var YBR = "490"
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
                            Toast.makeText(this@ResultDetailImage, "성공!!", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(this@ResultDetailImage, "실패", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<AnalyInfo>, t: Throwable) {
                        Log.d("sshConnect()", "sshConnect()")

                        Toast.makeText(
                            this@ResultDetailImage,
                            t.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }

    fun fileUpload() {
//Log.d("Basic", savedUri.toString() + photoFile.toString())
        val stream = FileInputStream(File(Visiter.Visi.camerauri1))
        val stream2 = FileInputStream(File(Visiter.Visi.camerauri2))

        val storageRef = FirebaseStorage.getInstance().reference
        val storageRef2 = FirebaseStorage.getInstance().reference

        val mountainsRef = storageRef.child(Visiter.Visi.camerauri1)
        val mountainsRef2 = storageRef2.child(Visiter.Visi.camerauri2)

        val uploadTask = mountainsRef.putStream(stream)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }

        val uploadTask2 = mountainsRef2.putStream(stream2)
        uploadTask2.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot2 ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }


}