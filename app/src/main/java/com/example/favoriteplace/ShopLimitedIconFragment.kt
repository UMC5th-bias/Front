package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentShopDetailIconBinding

class ShopLimitedIconFragment : Fragment() {
    lateinit var binding: FragmentShopDetailIconBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentShopDetailIconBinding.inflate(inflater,container,false)

        binding.shopBannerDetailIconIb.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, ShopBannerNewFragment())
                .commitAllowingStateLoss()
        }

        return binding.root
    }
}
