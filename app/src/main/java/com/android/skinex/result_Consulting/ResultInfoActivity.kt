package com.android.skinex.result_Consulting

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.android.skinex.R
import com.android.skinex.camera2Api.CameraX
import com.android.skinex.camera2Api.CameraXDetail
import com.android.skinex.camera2Api.CameraXReturn
import com.android.skinex.databinding.ResultInfoBinding
import com.android.skinex.databinding.UserInfoBinding
import com.android.skinex.publicObject.Validation
import com.android.skinex.publicObject.Visiter
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class ResultInfoActivity : AppCompatActivity() {

    private lateinit var binding: ResultInfoBinding

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
        binding.patientInputDay.setText("$MYyear-${MYmonth.toString().padStart(2,'0')}-${MYday.toString().padStart(2,'0')}")
        //binding.genderRadioGroup.check(Visiter.Visi.gender)

        var idBirth = Visiter.Visi.birth
        idBirth = idBirth.replace("-", "")
        var reMYyear = MYyear.toString()
        reMYyear = reMYyear.replace("20", "")
        reMYyear = reMYyear.replace("19", "")
        Log.d("xxoo년도 중 xx를 잘라서 받아옴 : " , reMYyear )
        //.toString().padStart(2,'0') 를 쓴 이유는 MYmonth 가 01인 경우에 저걸 안쓰면 1만 가져옴 01이아님
        binding.patientNum.setText(idBirth+"$reMYyear${MYmonth.toString().padStart(2,'0')}${MYday.toString().padStart(2,'0')}")
    }

    //촬영 클릭시 전체촬영 이벤트
    fun goguide(){
        binding.goguide.setOnClickListener {
            startActivity(Intent(this, GuideActivity::class.java))
        }
    }

    fun imageUp() {
        binding.shortDistanceShot2.setImageURI(Visiter.Visi.camerauri1.toUri())
        binding.longDistanceShot4.setImageURI((Visiter.Visi.camerauri2.toUri()))
    }

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
            val intent = Intent(this, ResultActivity::class.java)
            startActivity(intent)
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