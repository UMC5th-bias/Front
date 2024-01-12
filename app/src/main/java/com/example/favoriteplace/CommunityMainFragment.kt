package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentCommunitymainBinding

class CommunityMainFragment: Fragment() {

    lateinit var binding: FragmentCommunitymainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunitymainBinding.inflate(inflater,container,false)

        return binding.root
    }

}