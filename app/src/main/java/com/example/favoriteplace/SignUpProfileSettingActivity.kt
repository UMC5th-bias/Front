package com.example.favoriteplace

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.ActivitySignupProfileSettingBinding

class SignUpProfileSettingActivity: Fragment() {

    lateinit var binding: ActivitySignupProfileSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupProfileSettingBinding.inflate(layoutInflater)

    }
}