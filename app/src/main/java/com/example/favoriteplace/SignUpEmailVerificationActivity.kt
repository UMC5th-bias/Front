package com.example.favoriteplace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.favoriteplace.databinding.ActivitySignupEmailVerificationBinding

class SignUpEmailVerificationActivity: AppCompatActivity() {

    lateinit var binding: ActivitySignupEmailVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignupEmailVerificationBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_signup_email_verification)
    }
}