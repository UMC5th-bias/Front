package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentCscBinding
import com.google.android.material.tabs.TabLayoutMediator


class CscFragment : Fragment() {

    lateinit var binding : FragmentCscBinding
    private val infomation = arrayListOf("도움말", "문의하기")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCscBinding.inflate(inflater,container,false)


        val lockerAdapter = CscVPAdapter(this)
        binding.cscContentVp.adapter = lockerAdapter
        TabLayoutMediator(binding.cscContentTb, binding.cscContentVp){
                tab, position ->
            tab.text = infomation[position]
        }.attach()


        return binding.root

    }






}