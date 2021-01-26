package com.android.skinex.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.skinex.R
import com.android.skinex.databinding.ActivityLoginBinding
import com.android.skinex.databinding.VisiterTypeBinding
import com.android.skinex.publicObject.Visiter
import com.android.skinex.user_Consulting.ReturningInfoActivity
import com.android.skinex.user_Consulting.UserInfoActivity

class VisiterType : AppCompatActivity() {

    private lateinit var binding: VisiterTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = VisiterTypeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btn1.setOnClickListener{ firstClick() }
//        Log.d("test",binding.root.toString() )
        binding.btn2.setOnClickListener {  returningClick() }
    }



   private fun firstClick() {
       Visiter.Visi.name = ""
       Visiter.Visi.gender = ""
       Visiter.Visi.birth = ""
       Visiter.Visi.burndate = ""
       val intent = Intent(this, UserInfoActivity::class.java)
       startActivity(intent)

   }

    private fun returningClick() {
        val intent = Intent(this, SeclectVisiter::class.java)
        startActivity(intent)
    }


}