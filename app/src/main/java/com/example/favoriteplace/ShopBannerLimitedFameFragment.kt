package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentShopDetailLimitedFameBinding

class ShopBannerLimitedFameFragment: Fragment() {
    lateinit var binding: FragmentShopDetailLimitedFameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentShopDetailLimitedFameBinding.inflate(inflater,container,false)

        binding.shopBannerDetailFameIb.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, ShopBannerNewFragment())
                .commitAllowingStateLoss()
        }

        return binding.root
    }
}
