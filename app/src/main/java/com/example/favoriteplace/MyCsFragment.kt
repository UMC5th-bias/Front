package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.favoriteplace.databinding.FragmentCscBinding

class MyCsFragment : Fragment() {
    lateinit var binding: FragmentCscBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCscBinding.inflate(inflater, container, false)


        // 뒤로가기
        binding.placeInquiryBackIb.setOnClickListener{
            parentFragmentManager.commit {
                replace(R.id.main_frameLayout, MyFragment())
                addToBackStack(null)
            }
        }


        return  binding.root
    }
}