package com.android.skinex.result_Consulting

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.android.skinex.R
import com.android.skinex.activity.Guide
import com.android.skinex.activity.GuideLine
import com.android.skinex.databinding.ResultInfo2Binding
import com.android.skinex.databinding.ResultInfoBinding
import com.android.skinex.publicObject.Analy
import com.android.skinex.publicObject.Visiter
import com.davemorrissey.labs.subscaleview.ImageSource
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.guide.*
import kotlinx.android.synthetic.main.guide.longDistanceShot4
import kotlinx.android.synthetic.main.result_info.*
import kotlinx.android.synthetic.main.result_info2.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class ResultActivity : AppCompatActivity()  {

    private lateinit var binding: ResultInfo2Binding


    var MYyear = 0
    var MYmonth = 0
    var MYday = 0

    var pieChart: PieChart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ResultInfo2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        binding.redbox.bringToFront()


        guide()


        //setContentView(R.layout.result_info2)

        pieChart = findViewById<View>(R.id.piechart) as PieChart?
        pieChart!!.setUsePercentValues(true)
        pieChart!!.description.isEnabled = false
        pieChart!!.setExtraOffsets(5f, 10f, 5f, 10f)
        pieChart!!.dragDecelerationFrictionCoef = 0.95f
        pieChart!!.isDragDecelerationEnabled = false
        pieChart!!.setTouchEnabled(false)
        pieChart!!.isDrawHoleEnabled = false
        pieChart!!.setHoleColor(Color.WHITE)
        pieChart!!.setCenterTextSize(25f)
        pieChart!!.transparentCircleRadius = 65f
        pieChart!!.legend.isEnabled = false


        val yValues = ArrayList<PieEntry>()
        yValues.add(PieEntry(Analy.Analy.degree_value.toFloat()*1000f, Analy.Analy.degree_key))
        yValues.add(PieEntry(Analy.Analy.degree_value2.toFloat()*1000f, Analy.Analy.degree_key2))
        yValues.add(PieEntry(Analy.Analy.degree_value3.toFloat()*1000f, Analy.Analy.degree_key3))
        yValues.add(PieEntry(Analy.Analy.degree_value4.toFloat()*1000f, Analy.Analy.degree_key4))
        yValues.add(PieEntry(Analy.Analy.degree_value5.toFloat()*1000f, Analy.Analy.degree_key5))
        yValues.add(PieEntry(Analy.Analy.degree_value6.toFloat()*1000f, Analy.Analy.degree_key6))
        //        yValues.add(new PieEntry(40f,"Korea"));

        val description = Description()
        description.text = "화상 심도 " //라벨

        description.textSize = 25f
        description.textColor = Color.WHITE
        pieChart!!.description = description
        pieChart!!.animateY(1000, Easing.EasingOption.EaseInOutCubic) //애니메이션

        val dataSet = PieDataSet(yValues, "화상심도")
        dataSet.sliceSpace = 5f
        dataSet.selectionShift = 5f
        dataSet.valueTextColor = Color.RED
        dataSet.setColors(*ColorTemplate.PASTEL_COLORS)


        val data = PieData(dataSet)
        data.setValueTextSize(25f)

        Log.d("dataset:",data.dataSet.toString())
        Log.d("dataset:",data.dataSet.yValuePosition.toString())
        data.setValueTextColor(Color.BLACK)
        pieChart!!.data = data

        info()
        imageUp()
    }

    fun guide() {
        binding.mulm.setOnClickListener {
            val intent = Intent(this, GuideLine::class.java)
            startActivity(intent)
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

        binding.patientName77.setText(Visiter.Visi.name)
        binding.patientGender77.setText(Visiter.Visi.gender)
        binding.patientBirth77.setText(Visiter.Visi.birth)
        binding.patientBurnday77.setText(Visiter.Visi.burndate)

        var idBirth = Visiter.Visi.birth
        idBirth = idBirth.replace("-", "")
        var reMYyear = MYyear.toString()
        reMYyear = reMYyear.replace("20", "")
        reMYyear = reMYyear.replace("19", "")
        Log.d("xxoo년도 중 xx를 잘라서 받아옴 : ", reMYyear)
        binding.patientNum.setText(
            idBirth + "$reMYyear${MYmonth.toString().padStart(2, '0')}${
                MYday.toString().padStart(
                    2,
                    '0'
                )
            }"
        )

        binding.patientInputDay.setText(
            "$MYyear-${MYmonth.toString().padStart(2, '0')}-${
                MYday.toString().padStart(
                    2,
                    '0'
                )
            }"
        )
    }



    fun imageUp() {

        Log.d("screenshot" , Visiter.Visi.camerauri1)
//        binding.shortDistanceShot2.setImageURI(Visiter.Visi.camerauri1.toUri())
//        Glide.with(this).load(Visiter.Visi.camerauri2).into(findViewById<ImageView>(R.id.longDistanceShot4))
        Log.d("screenshot:.screenshot", Visiter.Visi.screenshot.toString())

        binding.longDistanceShot4.setImageBitmap(Visiter.Visi.screenshot)
//        getScreenShot(binding.longDistanceShot4)
//        view_result.getScreenShot(binding.longDistanceShot4)
//        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Visiter.Visi.screenshot.toUri())
    }


}