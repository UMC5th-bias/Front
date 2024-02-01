package com.example.favoriteplace

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
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

//            val mDialogView=LayoutInflater.from(this.context).inflate(R.layout.dialog_shop_detail_purchase_fame, null)
//            val mBuilder=AlertDialog.Builder(this.context).setView(mDialogView)
//
//            mBuilder.show()
        }

        return binding.root
    }

    private fun popupFamePurchaseClick() {
        FamePurchaseDialog().show(parentFragmentManager,"123456")
    }
}
