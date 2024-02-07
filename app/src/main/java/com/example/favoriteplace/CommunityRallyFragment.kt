package com.example.favoriteplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentCommunityRallyBinding
import com.google.android.material.tabs.TabLayoutMediator

class CommunityRallyFragment : Fragment() {

    lateinit var binding: FragmentCommunityRallyBinding
    private val information= arrayListOf("최신 글","추천 많은 글","내가 작성한 글","내 댓글")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCommunityRallyBinding.inflate(inflater,container,false)

        val communityRallyAdapter=CommunityRallyVPAdapter(this)
        binding.communityRallyVp.adapter=communityRallyAdapter
        TabLayoutMediator(binding.communityRallyTb,binding.communityRallyVp){
                tab,position->
            tab.text=information[position]
        }.attach()

        binding.communityRallySortIb.setOnClickListener {
            val sortBottomSheet = SortBottomSheetFragment()
            sortBottomSheet.show(parentFragmentManager, sortBottomSheet.tag)
        }

        return binding.root
    }
}