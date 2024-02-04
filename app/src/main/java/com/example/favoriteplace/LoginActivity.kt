package com.example.favoriteplace

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.favoriteplace.databinding.ActivityLoginBinding

class LoginActivity :AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignupTv.setOnClickListener {
            startActivity(Intent(this, SignUpConditionConfirmActivity::class.java))
        }
    }
}