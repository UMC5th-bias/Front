package com.example.favoriteplace

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.favoriteplace.databinding.ActivitySignupEmailInputBinding

class SignUpEmailInputActivity: AppCompatActivity() {

    lateinit var binding: ActivitySignupEmailInputBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        binding = ActivitySignupEmailInputBinding.inflate(layoutInflater)


    }

}