package com.example.favoriteplace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.favoriteplace.databinding.ActivitySignupPwdInputBinding

class SignUpPwdInputActivity: AppCompatActivity() {

    lateinit var binding: ActivitySignupPwdInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignupPwdInputBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_signup_pwd_input)
    }
}