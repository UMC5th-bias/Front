package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.ActivitySignupFinishBinding

class SignUpFinishActivity: Fragment() {

    lateinit var binding: ActivitySignupFinishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupFinishBinding.inflate(layoutInflater)

    }
}