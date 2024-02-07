package com.example.favoriteplace

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CommunityRallyVPAdapter(fragment: Fragment):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int=4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->CommunityRallyLatelyFragment()
            1->CommunityRallyRecommendFragment()
            2->CommunityRallyMyFragment()
            else->CommunityRallyCommendFragment()
        }
    }


}