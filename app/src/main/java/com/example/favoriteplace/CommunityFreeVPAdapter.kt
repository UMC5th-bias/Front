package com.example.favoriteplace

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CommunityFreeVPAdapter(fragment: Fragment):FragmentStateAdapter(fragment){
    override fun getItemCount(): Int=4
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->CommunityFreeLatelyFragment()
            1->CommunityFreeRecommendFragment()
            2->CommunityFreeMyFragment()
            3->CommunityFreeCommendFragment()
            else->CommunityFreeLatelyFragment()
        }
    }
}