package com.example.favoriteplace

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentShopDetailUnlimitedFameBinding

class ShopBannerUnlimitedFameFragment: Fragment() {
    lateinit var binding: FragmentShopDetailUnlimitedFameBinding

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentShopDetailUnlimitedFameBinding.inflate(inflater,container,false)

        binding.shopBannerDetailFameIb.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, ShopBannerNewFragment())
                .commitAllowingStateLoss()
        }

        binding.shopBannerDetailFamePurchaseBn.setOnClickListener{
            popupFamePurchaseClick()
        }

        return binding.root
    }

    //칭호 구매 팝업창 띄우기
    private fun popupFamePurchaseClick() {
        FamePurchaseDialog().show(parentFragmentManager,"")
    }
}
