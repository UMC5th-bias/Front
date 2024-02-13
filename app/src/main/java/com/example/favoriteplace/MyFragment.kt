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





        binding.mySettingIv.setOnClickListener {
            // MySettingFragment 이동

            parentFragmentManager.commit {
                replace(R.id.main_frameLayout, MySettingFragment())
                addToBackStack(null) // 백 스택에 추가하여 이전 Fragment로 돌아갈 수 있도록 함
            }
        }


        return binding.root
    }

}