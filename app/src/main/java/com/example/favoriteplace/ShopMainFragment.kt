package com.example.favoriteplace

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.FragmentShopMainBinding

class ShopMainFragment : Fragment() {
    lateinit var binding: FragmentShopMainBinding

    private val handler = Handler(Looper.getMainLooper())
    private val imageResIds = listOf(R.drawable.shop_banner1, R.drawable.shop_banner2)
    private lateinit var slideRunnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopMainBinding.inflate(inflater, container, false)

        slideRunnable = Runnable {
            if (isAdded) {
                binding.shopMainBannerVp2.currentItem = (binding.shopMainBannerVp2.currentItem + 1) % imageResIds.size
                handler.postDelayed(slideRunnable, 3000)
            }
        }

        return binding.root
    }

    private fun setupBannerViewPager() {
        val adapter = ShopBannerVPAdapter(imageResIds)
        binding.shopMainBannerVp2.adapter = adapter

        // 자동 슬라이딩 시작
        startAutoSlide()
    }


    private fun startAutoSlide() {
        handler.postDelayed(slideRunnable, 3000) // 3초마다 배너 변경
    }

    private fun stopAutoSlide() {
        handler.removeCallbacks(slideRunnable)
    }

    override fun onResume() {
        super.onResume()
        startAutoSlide()
    }

    override fun onPause() {
        super.onPause()
        stopAutoSlide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        setupBannerViewPager()

        // 초기 상태 설정
        binding.shopMainSwitchOnOffSc.isChecked = false
        binding.limitedSaleContainer.visibility = View.VISIBLE
        binding.regularSaleContainer.visibility = View.GONE
        binding.shopMainSwitchLimitedTv.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.shopMainSwitchRegularTv.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.black
            )
        )

        // 상태 변경 리스너 설정
        binding.shopMainSwitchOnOffSc.setOnCheckedChangeListener { _, isChecked ->
            Handler(Looper.getMainLooper()).postDelayed({
                if (isChecked) {
                    binding.limitedSaleContainer.visibility = View.GONE
                    binding.regularSaleContainer.visibility = View.VISIBLE
                    binding.shopMainSwitchLimitedTv.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    binding.shopMainSwitchRegularTv.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                } else {
                    binding.limitedSaleContainer.visibility = View.VISIBLE
                    binding.regularSaleContainer.visibility = View.GONE
                    binding.shopMainSwitchLimitedTv.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    binding.shopMainSwitchRegularTv.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                }
            }, 100) // 100ms 지연
        }
    }


}
