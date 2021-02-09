package com.android.skinex.result_Consulting

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import com.android.skinex.R
import com.android.skinex.databinding.ResultInfo2Binding
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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ResultInfo2Binding

    var pieChart: PieChart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ResultInfo2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
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

        val yValues = ArrayList<PieEntry>()
        yValues.add(PieEntry(Analy.Analy.degree_value.toFloat(), Analy.Analy.degree_key))
        yValues.add(PieEntry(Analy.Analy.degree_value2.toFloat(), Analy.Analy.degree_key2))
        yValues.add(PieEntry(Analy.Analy.degree_value3.toFloat(),Analy.Analy.degree_key3))
        yValues.add(PieEntry(Analy.Analy.degree_value4.toFloat(), Analy.Analy.degree_key4))
        yValues.add(PieEntry(Analy.Analy.degree_value5.toFloat(), Analy.Analy.degree_key5))
        yValues.add(PieEntry(Analy.Analy.degree_value6.toFloat(), Analy.Analy.degree_key6))
        //        yValues.add(new PieEntry(40f,"Korea"));

        val description = Description()
        description.text = "화상 심도 " //라벨

        description.textSize = 25f
        description.textColor = Color.WHITE
        pieChart!!.description = description
        pieChart!!.animateY(1000, Easing.EasingOption.EaseInOutCubic) //애니메이션

        val dataSet = PieDataSet(yValues, "화상심도")
        dataSet.sliceSpace = 8f
        dataSet.selectionShift = 8f
        dataSet.valueTextColor = Color.RED
        dataSet.setColors(*ColorTemplate.JOYFUL_COLORS)

        val data = PieData(dataSet)
        data.setValueTextSize(25f)
        data.setValueTextColor(Color.BLACK)
        pieChart!!.data = data

        info()
        imageUp()
    }

    fun info() {
        binding.patientName77.setText(Visiter.Visi.name)
        binding.patientGender77.setText(Visiter.Visi.gender)
        binding.patientBirth77.setText(Visiter.Visi.birth)
        binding.patientBurnday77.setText(Visiter.Visi.burndate)
    }

    fun imageUp() {
        binding.shortDistanceShot2.setImageURI(Visiter.Visi.camerauri1.toUri())
//        Glide.with(this).load(Visiter.Visi.camerauri2).into(findViewById<ImageView>(R.id.longDistanceShot4))
        Log.d("screenshot", Visiter.Visi.screenshot)
        binding.longDistanceShot4.setImageBitmap(Visiter.Visi.screenshot.toBitmap())
    }
}