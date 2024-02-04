package com.example.favoriteplace

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.favoriteplace.databinding.ActivitySignupEmailInputBinding

class SignUpEmailInputActivity: AppCompatActivity() {

    lateinit var binding: ActivitySignupEmailInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignupEmailInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}