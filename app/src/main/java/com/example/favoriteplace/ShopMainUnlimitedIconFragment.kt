package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentShopDetailUnlimitedIconBinding

class ShopMainUnlimitedIconFragment : Fragment() {
    lateinit var binding: FragmentShopDetailUnlimitedIconBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopDetailUnlimitedIconBinding.inflate(inflater, container, false)

        binding.shopBannerDetailIconIb.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, ShopMainFragment())
                .commitAllowingStateLoss()
        }

        binding.shopBannerDetailIconPurchaseBn.setOnClickListener {
            popupIconPurchaseClick()
        }

        return binding.root
    }

    //아이콘 구매 팝업창 띄우기
    private fun popupIconPurchaseClick() {
        IconPurchaseDialog().show(parentFragmentManager, "")
    }
}
