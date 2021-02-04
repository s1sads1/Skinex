package com.android.skinex.result_Consulting

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.android.skinex.camera2Api.CameraXDetail
import com.android.skinex.camera2Api.CameraXReturn
import com.android.skinex.databinding.ResultInfoBinding
import com.android.skinex.dataclass.AnalyInfo
import com.android.skinex.publicObject.Analy
import com.android.skinex.publicObject.Visiter
import com.android.skinex.restApi.ApiUtill
import com.davemorrissey.labs.subscaleview.ImageSource
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.guide.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.math.log


class ResultInfoActivity : AppCompatActivity() {

    private lateinit var binding: ResultInfoBinding

    var firebaseString :String = "https://firebasestorage.googleapis.com/v0/b/wpias-94d18.appspot.com/o/storage%2Femulated%2F0%2FAndroid%2Fmedia%2Fcom.android.skinex%2FSkinex%2F2021-01-26-17-14-12-332.jpg?alt=media&token=b1c15a56-1e72-415c-a4eb-1e7870f2bdf8"

    var storageUrl = "https://firebasestorage.googleapis.com/v0/b/wpias-94d18.appspot.com/o/storage%2Femulated%2F0%2FAndroid%2Fmedia%2Fcom.android.skinex%2FSkinex%2F${Visiter.Visi.firebaseurl}.jpg?alt=media"
    var MYyear = 0
    var MYmonth = 0
    var MYday = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ResultInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



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

    //촬영 클릭시 전체촬영 이벤트
    fun goguide(){
        binding.save.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java)
            startActivity(intent)
        }
    }

    fun imageUp() {
        binding.shortDistanceShot2.setImageURI(Visiter.Visi.camerauri1.toUri())
//        Glide.with(this).load(Visiter.Visi.camerauri2).into(findViewById<ImageView>(R.id.longDistanceShot4))
   binding.longDistanceShot4.setImageURI(Visiter.Visi.camerauri2.toUri())
    }

//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//
//        binding.longDistanceShot4.setImage(ImageSource.uri(Visiter.Visi.camerauri2))
//
//    }
    fun recapture() {
        binding.btnRecapture1.setOnClickListener{
            startActivity(Intent(this, CameraXReturn::class.java))
        }

        binding.btnRecapture2.setOnClickListener {
            startActivity(Intent(this, CameraXDetail::class.java))
        }
    }

    fun resultsubmit() {
        binding.resultsubmit.setOnClickListener {
            fileUpload()

            Toast.makeText(this@ResultInfoActivity, "잠시만 기다려주세요",
                Toast.LENGTH_LONG).show()

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
            Log.d("binding.longDistanceShot4.left",binding.longDistanceShot4.left.toString())
            Log.d("binding.longDistanceShot4.right",binding.longDistanceShot4.right.toString())
            Log.d("binding.longDistanceShot4.top",binding.longDistanceShot4.top.toString())
            Log.d("binding.longDistanceShot4.bottom",binding.longDistanceShot4.bottom.toString())
            Log.d("binding.text.x", binding.text.x.toString())
            Log.d("image.text.x", binding.longDistanceShot4.x.toString())
            Log.d("standard.text.x", binding.textstandard.x.toString())
            Log.d("YTL", YTL)
            Log.d("binding.text.y", binding.text.y.toString())
            Log.d("image.text.y", binding.longDistanceShot4.y.toString())
            Log.d("standard.text.y", binding.textstandard.y.toString())
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