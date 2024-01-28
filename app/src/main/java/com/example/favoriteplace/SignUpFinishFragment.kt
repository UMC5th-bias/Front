package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentCommunityMainBinding
import com.example.favoriteplace.databinding.FragmentRallyplaceBinding
import com.example.favoriteplace.databinding.FragmentSignupConditionConfirmBinding
import com.example.favoriteplace.databinding.FragmentSignupFinishBinding

class SignUpFinishFragment: Fragment() {

    lateinit var binding: FragmentSignupFinishBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignupFinishBinding.inflate(inflater, container,false)

        return binding.root
    }

}