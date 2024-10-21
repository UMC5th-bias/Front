package com.example.favoriteplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentShopBanner1Binding
import com.example.favoriteplace.databinding.FragmentShopBannerWithButtonBinding

class ShopBannerFragment : Fragment() {
    private var _binding: FragmentShopBanner1Binding? = null
    private val binding get() = _binding!!
    companion object {
        private const val ARG_IMAGE_RES = "imageRes"
//        private const val ARG_SHOW_BUTTON = "showButton"
        private const val ARG_FRAGMENT_CLASS_NAME = "fragmentClassName"

        fun newInstance(@DrawableRes imageRes: Int, fragmentClassName: String?): ShopBannerFragment {
            val fragment = ShopBannerFragment()
            val args = Bundle().apply {
                putInt(ARG_IMAGE_RES, imageRes)
//                putBoolean(ARG_SHOW_BUTTON, showButton)
                putString(ARG_FRAGMENT_CLASS_NAME, fragmentClassName)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopBanner1Binding.inflate(inflater, container, false)
        val view = binding.root

        // 이미지 설정
        binding.shopMainShopBanner1Iv.setImageResource(arguments?.getInt(ARG_IMAGE_RES) ?: 0)

        /*
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
         */

        // 이미지를 클릭하면 targetFragment로 이동
        binding.shopMainShopBanner1Iv.setOnClickListener {
            val fragmentClassName = arguments?.getString(ARG_FRAGMENT_CLASS_NAME)
            if (fragmentClassName != null) {
                try {
                    // 동적으로 프래그먼트를 생성
                    val fragment = Class.forName(fragmentClassName).newInstance() as Fragment
                    (activity as? MainActivity)?.navigateToFragment(fragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                    // 오류 발생 시 처리
                }
            } else {
                // 이동할 프래그먼트가 없는 경우
                Log.d("ShopBannerFragment", "No target fragment set for this banner.")
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}