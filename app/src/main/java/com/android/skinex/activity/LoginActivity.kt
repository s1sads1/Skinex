package com.android.skinex.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.skinex.databinding.ActivityLoginBinding
import com.android.skinex.activity.VisiterType
import com.android.skinex.camera2Api.CameraX
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    //파이어베이스 인증관련 연동
    private lateinit var auth: FirebaseAuth

    //BindingView사용
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        binding.btnLogin.setOnClickListener{ Login() }

        binding.layoutSkinex.setOnClickListener{ hideKeyboard() }
    }

    fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etId.windowToken, 0)
    }

    //로그인 기능
    private fun Login() {
        var email = binding.etId.text.toString()
        Log.d("idconfirm", email)
        var password = binding.etPw.text.toString()
        Log.d("pw확인", password)

        if (email.isNullOrEmpty()) {
            Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
        } else if (password.isNullOrEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    Log.d("id확인", email)
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("SkinexTag", "signInWithEmail:success")
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("SkinexTag", "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }
                }
        }
    }

//   private fun Login() {
//        binding.btnLogin.setOnClickListener{
//                var email = binding.etId.toString()
//                var password = binding.etPw.toString()
//                auth.signInWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(this) { task ->
//                            if (task.isSuccessful) {
//                                val user = auth.currentUser
//                                // Sign in success, update UI with the signed-in user's information
//                                Log.d("SkinexTag", "signInWithEmail:success")
//                                updateUI(user)
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Log.w("SkinexTag", "signInWithEmail:failure", task.exception)
//                                Toast.makeText(baseContext, "Authentication failed.",
//                                        Toast.LENGTH_SHORT).show()
//                                updateUI(null)
//                                // ...
//                            }
//
//                            // ...
//                        }
//
//            }
//    }


    /**보류
    //활동을 초기화할 때 사용자가 현재 로그인되어 있는지 확인
    private fun onStart(user: FirebaseUser?) {
    super.onStart()

    // Check if user is signed in (non-null) and update UI accordingly.
    updateUI(user)
    }
     **/

    //화면 전환기능
    private fun updateUI(user: FirebaseUser?) { //update ui code here
        Log.d("updateid", binding.etId.text.toString())
        Log.d("updatepw", binding.etPw.text.toString())
        Log.d("updateauth", auth.currentUser.toString())
        if (user != null) {
            val intent = Intent(this, VisiterType::class.java)
            startActivity(intent)
            finish()
        }
        else{
            Toast.makeText(this,
                "다시입력해주세요", Toast.LENGTH_SHORT).show()

        }
    }



}