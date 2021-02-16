package com.android.skinex.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.skinex.databinding.GuideBinding
import com.android.skinex.databinding.GuideLineBinding

class Guide: AppCompatActivity() {

    private lateinit var binding: GuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = GuideBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.back.setOnClickListener {
            onBackPressed()

        }
    }

}