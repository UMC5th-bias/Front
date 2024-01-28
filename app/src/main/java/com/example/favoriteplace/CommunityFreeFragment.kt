package com.example.favoriteplace

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentCommunityFreeBinding
import com.google.android.material.tabs.TabLayoutMediator

class CommunityFreeFragment : Fragment() {

    lateinit var binding: FragmentCommunityFreeBinding

    private val information= arrayListOf("최신 글","추천 많은 글","내가 작성한 글","내 댓글")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCommunityFreeBinding.inflate(inflater,container,false)

        val communityFreeAdapter=CommunityFreeVPAdapter(this)
        binding.communityFreeVp.adapter=communityFreeAdapter
        TabLayoutMediator(binding.communityFreeTb,binding.communityFreeVp){
            tab,position->
            tab.text=information[position]
        }.attach()

        binding.communityFreeWriteBn.setOnClickListener {
            val intent = Intent(activity, FreeWritePostActivity::class.java)
            startActivity(intent)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.communityFreeSortIb.setOnClickListener {
            modalWithRoundCorner()
        }
    }

    private fun modalWithRoundCorner() {
        val modal = SortBottomSheetFragment().apply {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
        }
        modal.show(childFragmentManager, SortBottomSheetFragment.TAG)
    }
}