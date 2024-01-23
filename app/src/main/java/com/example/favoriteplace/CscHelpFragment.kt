package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentCscHlepBinding
import com.example.favoriteplace.databinding.FragmentCscInquiryBinding

class CscHelpFragment : Fragment() {

    lateinit var binding : FragmentCscHlepBinding
    private var clickCount: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCscHlepBinding.inflate(inflater, container, false)


        binding.cscHelpContent1Iv.setOnClickListener {
            onIvClicked(binding)
        }

        return binding.root
    }

    private fun onIvClicked(binding: FragmentCscHlepBinding) {

        clickCount++
        if(clickCount %2 ==1){
            binding.cscHelpContent1Tv.visibility = View.VISIBLE
        }else{
            binding.cscHelpContent1Tv.visibility = View.GONE
        }
    }
}