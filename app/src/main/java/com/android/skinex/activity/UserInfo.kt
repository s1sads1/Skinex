package com.android.skinex.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.skinex.databinding.UserInfoBinding
import com.android.skinex.databinding.VisiterTypeBinding

class UserInfo : AppCompatActivity() {
    private lateinit var binding: UserInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = UserInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }



}