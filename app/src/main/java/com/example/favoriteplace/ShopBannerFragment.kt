package com.example.favoriteplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentShopBannerWithButtonBinding

class ShopBannerFragment : Fragment() {
    private var _binding: FragmentShopBannerWithButtonBinding? = null
    private val binding get() = _binding!!
    companion object {
        private const val ARG_IMAGE_RES = "imageRes"
        private const val ARG_SHOW_BUTTON = "showButton"

        fun newInstance(@DrawableRes imageRes: Int, showButton: Boolean = false): ShopBannerFragment {
            val fragment = ShopBannerFragment()
            val args = Bundle().apply {
                putInt(ARG_IMAGE_RES, imageRes)
                putBoolean(ARG_SHOW_BUTTON, showButton)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopBannerWithButtonBinding.inflate(inflater, container, false)
        val view = binding.root

        // 이미지 설정
        binding.shopMainShopBanner2Iv.setImageResource(arguments?.getInt(ARG_IMAGE_RES) ?: 0)

        // "보러가기" 버튼 가시성 설정
        if (arguments?.getBoolean(ARG_SHOW_BUTTON) == true) {
            binding.shopGoToCl.visibility = View.VISIBLE
            binding.shopGoToCl.setOnClickListener {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frameLayout, ShopBannerNewFragment())
                    .commitAllowingStateLoss()
            }
        } else {
            binding.shopGoToCl.visibility = View.GONE
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}