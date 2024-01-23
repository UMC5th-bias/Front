package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment

class ShopBannerFragment : Fragment() {
    companion object {
        private const val ARG_IMAGE_RES = "imageRes"

        fun newInstance(@DrawableRes imageRes: Int): ShopBannerFragment {
            val fragment = ShopBannerFragment()
            val args = Bundle()
            args.putInt(ARG_IMAGE_RES, imageRes)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shop_banner1, container, false)
        val imageView = view.findViewById<ImageView>(R.id.shopMain_shopBanner1_iv)
        val imageResId = arguments?.getInt(ARG_IMAGE_RES) ?: 0
        imageView.setImageResource(imageResId)
        return view
    }

}