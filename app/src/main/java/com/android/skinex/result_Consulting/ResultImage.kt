package com.android.skinex.result_Consulting

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.android.skinex.camera2Api.CameraXDetail
import com.android.skinex.camera2Api.CameraXReturn
import com.android.skinex.databinding.ResultImageBinding
import com.android.skinex.databinding.ResultInfoBinding
import com.android.skinex.publicObject.Visiter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class ResultImage :AppCompatActivity() {

    private lateinit var binding: ResultImageBinding

    var MYyear = 0
    var MYmonth = 0
    var MYday = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ResultImageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        info()
        imageUp()
        recapture()
        nextBtn()
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
        binding.patientName.setTextColor(Color.WHITE)
        binding.patientGender.setText(Visiter.Visi.gender)
        binding.patientGender.setTextColor(Color.WHITE)
        binding.patientBirth.setText(Visiter.Visi.birth)
        binding.patientBirth.setTextColor(Color.WHITE)
        binding.patientBurnday.setText(Visiter.Visi.burndate)
        binding.patientBurnday.setTextColor(Color.WHITE)
        //binding.patientInputDay.setText("${year}-${(month).toString().padStart(2,'0')}-${day.toString().padStart(2, '0')}")
        binding.patientInputDay.setText(
            "$MYyear-${MYmonth.toString().padStart(2, '0')}-${
                MYday.toString().padStart(
                    2,
                    '0'
                )
            }"
        )
        binding.patientInputDay.setTextColor(Color.WHITE)
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
    binding.patientNum.setTextColor(Color.WHITE)
    }


    fun imageUp() {
        binding.shortDistanceShot2.setImageURI(Visiter.Visi.camerauri1.toUri())
    }

    fun recapture() {
        binding.btnRecapture1.setOnClickListener{
            startActivity(Intent(this, CameraXReturn::class.java))
        }
       }

    fun nextBtn() {
        binding.nextBtn.setOnClickListener{
            startActivity(Intent(this, ResultDetailImage::class.java))
        }
    }

}