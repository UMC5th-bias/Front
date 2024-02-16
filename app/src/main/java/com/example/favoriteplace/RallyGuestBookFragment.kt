package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.favoriteplace.databinding.FragmentRallyGuestbookBinding

class RallyGuestBookFragment:Fragment() {
    lateinit var binding : FragmentRallyGuestbookBinding




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentRallyGuestbookBinding.inflate(inflater,container,false)


        val bannerAdapter = BannerVPAdapter(this)
        binding.rallyGuestbookVp.adapter=bannerAdapter
        binding.rallyGuestbookVp.orientation= ViewPager2.ORIENTATION_HORIZONTAL
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_banner1))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_banner1))



        return binding.root
    }
}