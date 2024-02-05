package com.example.favoriteplace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.favoriteplace.databinding.ActivitySignupProfileSettingBinding

class SignUpProfileSettingActivity: AppCompatActivity() {

    lateinit var binding: ActivitySignupProfileSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupProfileSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}