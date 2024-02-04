package com.example.favoriteplace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.favoriteplace.databinding.ActivitySignupFinishBinding

class SignUpFinishActivity: AppCompatActivity() {

    lateinit var binding: ActivitySignupFinishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignupFinishBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_signup_finish)
    }
}