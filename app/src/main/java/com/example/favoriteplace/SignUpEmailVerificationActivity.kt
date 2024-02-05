package com.example.favoriteplace

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.ActivitySignupEmailVerificationBinding

class SignUpEmailVerificationActivity: Fragment() {

    lateinit var binding: ActivitySignupEmailVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupEmailVerificationBinding.inflate(layoutInflater)

    }

}