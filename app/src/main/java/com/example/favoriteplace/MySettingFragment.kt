package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.favoriteplace.databinding.FragmentMySettingBinding

class MySettingFragment : Fragment() {
    lateinit var binding: FragmentMySettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMySettingBinding.inflate(inflater, container, false)




        binding.mySettingBackIb.setOnClickListener{
            parentFragmentManager.commit {
                replace(R.id.main_frameLayout, MyFragment())
                addToBackStack(null)
            }
        }

        return binding.root
    }
}