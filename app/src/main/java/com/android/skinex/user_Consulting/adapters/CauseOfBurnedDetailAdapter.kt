package com.android.skinex.user_Consulting.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.android.skinex.R
import com.android.skinex.databinding.ActivityLoginBinding.inflate
import com.android.skinex.databinding.BodyPartDetailBinding
import com.android.skinex.databinding.BodyPartDetailBinding.inflate
import com.android.skinex.databinding.CauseOfDetailBurnedBinding
import com.android.skinex.publicObject.Validation
//import com.phonegap.WPIAS.R
//import com.phonegap.WPIAS.publicObject.Validation
//import kotlinx.android.synthetic.main.cause_of_burned_detail.view.*
//import kotlinx.android.synthetic.main.cause_of_burned_detail.view.appCompatCheckBox

class CauseOfBurnedDetailAdapter(var category : String) : RecyclerView.Adapter<CauseOfBurnedDetailAdapter.CauseOfBurnedDetailViewHolder>() {

    var viewArr = ArrayList<View>()

    var arr = ArrayList<String>()

    var boiling = arrayListOf("뜨거운 차, 커피", "정수기, 온수꼭지", "라면 등의 국물", "프라이팬 기름", "커피포트, 주전자", "끓는 국, 곰탕, 찌개", "국자 속 국물", "기타")
    var boilingImage = arrayListOf(R.drawable.yultang_1, R.drawable.yultang_2, R.drawable.yultang_3, R.drawable.yultang_4, R.drawable.yultang_6, R.drawable.yultang_7_2, R.drawable.yultang_7, R.drawable.and)
    var flame = arrayListOf("기름으로 인한 화염", "가스배너로 인한 화염", "시너 등으로 인한 화염", "성냥으로 인한 화염", "기타")
    var flameImage = arrayListOf(R.drawable.hwayum_1, R.drawable.hwayum_2, R.drawable.hwayum_3, R.drawable.hwayum_4, R.drawable.and)
    var electric = arrayListOf("전기 콘센트", "기타")
    var electricImage = arrayListOf(R.drawable.jungi_1, R.drawable.and)
    var touched = arrayListOf("구이용 석쇠", "다리미", "난로 등 전열 기구", "조리기구", "기타")
    var touchedImage = arrayListOf(R.drawable.jubchok_1, R.drawable.jubchok_2, R.drawable.jubchok_3, R.drawable.jubchok_4, R.drawable.and)
    var lowTemperature = arrayListOf("핫팩", "전기장판", "휴대폰", "기타")
    var lowTemperatureImage = arrayListOf(R.drawable.juon_1, R.drawable.juon_2, R.drawable.juon_3, R.drawable.and)
    var chemical = arrayListOf("빙초산 등 강산성", "강알칼리성 물질", "락스", "기타")
    var chemicalImage = arrayListOf(R.drawable.hwahag_1, R.drawable.hwahag_2, R.drawable.hwahag_3, R.drawable.and)
    var steam = arrayListOf("가열식 가습기", "밥솥", "기타")
    var steamImage = arrayListOf(R.drawable.junggi_1, R.drawable.junggi_2, R.drawable.and)
    var friction = arrayListOf("런닝머신", "미끄럼틀", "기타")
    var frictionImage = arrayListOf(R.drawable.machar_1, R.drawable.machar_2, R.drawable.and)
    var sunburned = arrayListOf("햇빛", "가타")
    var sunburnedImage = arrayListOf(R.drawable.hatbit_1, R.drawable.and)
    var smoke = arrayListOf("유독가스", "기타")
    var smokeImage = arrayListOf(R.drawable.heubib_1, R.drawable.and)

    init {
        when(category){
            "열탕" -> arr = boiling
            "화염" -> arr = flame
            "전기" -> arr = electric
            "접촉" -> arr = touched
            "저온" -> arr = lowTemperature
            "화학" -> arr = chemical
            "증기" -> arr = steam
            "마찰" -> arr = friction
            "햇빛" -> arr = sunburned
            "흡입" -> arr = smoke
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CauseOfBurnedDetailViewHolder {
         var view = LayoutInflater.from(parent.context).inflate(R.layout.cause_of_detail_burned, parent, false)

        return CauseOfBurnedDetailViewHolder(view)

    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: CauseOfBurnedDetailViewHolder, position: Int) {

        holder.causeDetail.text = arr[position]

//        holder.causeDetail.text = (arr)[position]

        detailByCategory(category, position)

        (viewArr[position].findViewById<AppCompatCheckBox>(R.id.appCompatCheckBox)).setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){

                for(causeDetailView in viewArr){

                    if(causeDetailView != viewArr[position]){
                        (causeDetailView.findViewById<AppCompatCheckBox>(R.id.appCompatCheckBox)).isChecked = false
                    }

                }

                buttonView.isClickable = false

                Validation.vali.burnDetailV = (position + 1).toString().padStart(3, '0')

                if(viewArr[position].findViewById<TextView>(R.id.causeDetail).text == "기타"){

                    Validation.vali.burnDetailV = "999"
                    Validation.vali.bodyGitaV = ""

                }else{

                    Validation.vali.burnGitaV = ""

                }

            }else{

                buttonView.isClickable = true

            }

        }

        viewArr[0].findViewById<AppCompatCheckBox>(R.id.appCompatCheckBox).isChecked = true

    }

    //카테고리별 상세 원인 체크 박스를 view로 구성하는 펑션
    fun detailByCategory(category : String, position: Int){

        when(category){
            "열탕" -> {
                for((i, causeView) in viewArr.withIndex()){
                    if(position == i){
                        causeView.findViewById<ImageView>(R.id.causeOfBurnedDetailImage).setImageResource(boilingImage[i])
                    }
                }
            }
            "화염" -> {
                for((i, causeView) in viewArr.withIndex()){
                    if(position == i){
                        causeView.findViewById<ImageView>(R.id.causeOfBurnedDetailImage).setImageResource(flameImage[i])
                    }
                }
            }
            "전기" -> {
                for((i, causeView) in viewArr.withIndex()){
                    if(position == i){
                        causeView.findViewById<ImageView>(R.id.causeOfBurnedDetailImage).setImageResource(electricImage[i])
                    }
                }
            }
            "접촉" -> {
                for((i, causeView) in viewArr.withIndex()){
                    if(position == i){
                        causeView.findViewById<ImageView>(R.id.causeOfBurnedDetailImage).setImageResource(touchedImage[i])
                    }
                }
            }
            "저온" -> {
                for((i, causeView) in viewArr.withIndex()){
                    if(position == i){
                        causeView.findViewById<ImageView>(R.id.causeOfBurnedDetailImage).setImageResource(lowTemperatureImage[i])
                    }
                }
            }
            "화학" -> {
                for((i, causeView) in viewArr.withIndex()){
                    if(position == i){
                        causeView.findViewById<ImageView>(R.id.causeOfBurnedDetailImage).setImageResource(chemicalImage[i])
                    }
                }
            }
            "증기" -> {
                for((i, causeView) in viewArr.withIndex()){
                    if(position == i){
                        causeView.findViewById<ImageView>(R.id.causeOfBurnedDetailImage).setImageResource(steamImage[i])
                    }
                }
            }
            "마찰" -> {
                for((i, causeView) in viewArr.withIndex()){
                    if(position == i){
                        causeView.findViewById<ImageView>(R.id.causeOfBurnedDetailImage).setImageResource(frictionImage[i])
                    }
                }
            }
            "햇빛" -> {
                for((i, causeView) in viewArr.withIndex()){
                    if(position == i){
                        causeView.findViewById<ImageView>(R.id.causeOfBurnedDetailImage).setImageResource(sunburnedImage[i])
                    }
                }
            }
            "흡입" -> {
                for((i, causeView) in viewArr.withIndex()){
                    if(position == i){
                        causeView.findViewById<ImageView>(R.id.causeOfBurnedDetailImage).setImageResource(smokeImage[i])
                    }
                }
            }
        }

    }

    inner class CauseOfBurnedDetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var causeOfBurnedDetailWrapper = view.findViewById<ConstraintLayout>(R.id.causeOfBurnedDetailCheckWrapper)
//        var causeOfBurnedDetailCheckWrapper = view.findViewById<ConstraintLayout>(R.id.causeOfBurnedDetailCheckWrapper)
        var causeDetail = view.findViewById<TextView>(R.id.causeDetail)
//        var appCompatCheckBox = binding.appCompatCheckBox

        init {

            viewArr.add(view)

            causeOfBurnedDetailWrapper.setOnClickListener {

                viewArr[absoluteAdapterPosition].findViewById<AppCompatCheckBox>(R.id.appCompatCheckBox).isChecked = true

            }

        }

    }

}