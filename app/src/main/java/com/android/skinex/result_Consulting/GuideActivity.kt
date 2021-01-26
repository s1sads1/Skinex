package com.android.skinex.result_Consulting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.skinex.databinding.GuideBinding

class GuideActivity : AppCompatActivity()  {

    private lateinit var binding:GuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GuideBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        backResult()
    }
    fun backResult(){
        binding.goresultinfo2.setOnClickListener {
            startActivity(Intent(this, ResultInfoActivity::class.java))
            //startActivityForResult(Intent(this, ResultInfoActivity::class.java), REQUEST_TAKE_PHOTO_10)
            //startActivity(Intent(this@FindPasswordActivity, LoginActivity::class.java)
        }
    }

}