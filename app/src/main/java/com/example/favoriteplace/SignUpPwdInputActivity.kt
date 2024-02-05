package com.example.favoriteplace

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.ActivitySignupPwdInputBinding

class SignUpPwdInputActivity: Fragment() {

    lateinit var binding: ActivitySignupPwdInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupPwdInputBinding.inflate(layoutInflater)

    }

}