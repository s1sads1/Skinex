package com.android.skinex.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
//import com.android.skinex.databinding.ReturningVisiterBinding
import com.android.skinex.databinding.SelectVisiterBinding
//import com.android.skinex.databinding.VisiterTypeBinding
import com.android.skinex.room.Visit
import com.android.skinex.room.VisitDatabase
import com.android.skinex.user_Consulting.ReturningInfoActivity
import com.android.skinex.user_Consulting.UserInfoActivity
import com.android.skinex.user_Consulting.adapters.ReturningAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SeclectVisiter : AppCompatActivity() {

    private lateinit var binding: SelectVisiterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SelectVisiterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)




        var db = Room.databaseBuilder(
            applicationContext,
            VisitDatabase::class.java, "VisitDatabase"
        ).build()

        binding.submit.setOnClickListener{
            GlobalScope.launch {
                db.VisitDao().insertAll(
                    Visit(
                        "20210217210212",
                        "박선호",
                        "남성",
                        "1994-08-17",
                        "2021-01-22",
                        "2020-01-21",
                        "머리",
                        "이마",
                        "열탕",
                        "010",
                        "3403",
                        "좋아요치료합시다"
                    )
                )
            }
        }

//            var select :Array<Visit> = db.VisitDao().getBirthDay("박선호입니다요")

        binding.btnSelect.setOnClickListener {

            binding.returningRecyclerView.layoutManager = LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            )

            GlobalScope.launch {
                val name = binding.edName.text.toString()
                var select: Array<Visit> = db.VisitDao().getBirthDay(name)

//                var adbDao =  db.VisitDao().getBirthDay("박선호입니다요")


                var returningAdapter = ReturningAdapter(select)

                launch(Dispatchers.Main) {
                    binding.returningRecyclerView.adapter = returningAdapter
                }
            }
//                var daosize = db.VisitDao().getBirthDay("박선호입니다요").size
//                GlobalScope.launch {
//                    for(i in 0 until daosize) {
//                        binding.selectText.text = db.VisitDao().getBirthDay("박선호입니다요")[0].birthDay
//                    }
//                }

        }



        binding.layoutSelect.setOnClickListener{ hideKeyboard() }
    }

//    override fun onBackPressed() {
////        super.onBackPressed()
//    }

    fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.edName.windowToken, 0)
    }


}