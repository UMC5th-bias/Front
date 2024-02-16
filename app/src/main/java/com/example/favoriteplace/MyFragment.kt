package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.favoriteplace.databinding.FragmentMyBinding

class MyFragment : Fragment(){
    lateinit var binding: FragmentMyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyBinding.inflate(inflater, container, false)





        // 프로필 카드 환경설정
        binding.mySettingIv.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.main_frameLayout, MySettingFragment())
                addToBackStack(null)
            }
        }


        // 고객센터
        binding.myCsTv.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.main_frameLayout, MyCsFragment())
                addToBackStack(null)
            }
        }

        return binding.root
    }

}