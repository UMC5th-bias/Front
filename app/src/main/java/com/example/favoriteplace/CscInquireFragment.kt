package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentCscBinding
import com.example.favoriteplace.databinding.FragmentCscInquiryBinding

class CscInquireFragment : Fragment() {

    lateinit var binding : FragmentCscInquiryBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCscInquiryBinding.inflate(inflater, container, false)

        return binding.root
    }
}