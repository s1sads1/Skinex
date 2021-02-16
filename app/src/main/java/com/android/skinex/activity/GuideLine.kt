package com.android.skinex.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.skinex.databinding.ActivityLoginBinding
import com.android.skinex.databinding.GuideLineBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class GuideLine: AppCompatActivity() {

    private lateinit var binding: GuideLineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = GuideLineBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.back.setOnClickListener {
          onBackPressed()
        }
    }

//    fun back() {
//        val intent = Intent(this, LoginActivity::class.java)
//        startActivityForResult()
//        finish()
//    }


}