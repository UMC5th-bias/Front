package com.example.favoriteplace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.favoriteplace.databinding.ActivitySignupConditionConfirmBinding

class SignUpConditionConfirmActivity: AppCompatActivity() {

    lateinit var binding: ActivitySignupConditionConfirmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupConditionConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

}