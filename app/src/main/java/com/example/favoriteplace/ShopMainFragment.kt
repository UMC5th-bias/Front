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

    private lateinit var yesLoginView: View
    private lateinit var notLoginView: View

    private val handler = Handler(Looper.getMainLooper())
    private val imageResIds = listOf(R.drawable.shop_banner1, R.drawable.shop_banner2)
    private lateinit var slideRunnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopMainBinding.inflate(inflater, container, false)

        // 뷰 초기화
        yesLoginView = binding.shopMainYesLoginCl
        notLoginView = binding.shopMainNotLoginCl

        // 로그인 상태에 따라 뷰 업데이트
        updateLoginStatusView()

        slideRunnable = Runnable {
            if (isAdded) {
                binding.shopMainBannerVp2.currentItem = (binding.shopMainBannerVp2.currentItem + 1) % imageResIds.size
                handler.postDelayed(slideRunnable, 3000)
            }
        }

        return binding.root
    }

    private fun updateLoginStatusView() {
        if (isLoggedIn()) {
            yesLoginView.visibility = View.VISIBLE
            notLoginView.visibility = View.GONE
        } else {
            yesLoginView.visibility = View.GONE
            notLoginView.visibility = View.VISIBLE
        }
    }

    // 사용자의 로그인 상태를 확인하는 메소드
    private fun isLoggedIn(): Boolean {
        // 로그인 상태 확인 로직 구현
        // 예를 들어, SharedPreferences, 데이터베이스 조회 등
        return true // 임시로 false 반환
    }

    private fun setupBannerViewPager() {
// BannerItem 리스트 생성
        val items = listOf(
            BannerItem(R.drawable.shop_banner1, false), // "보러가기" 버튼 없음
            BannerItem(R.drawable.shop_banner2, true)   // "보러가기" 버튼 포함
        )
        // 어댑터에 FragmentActivity와 items 리스트 전달
        val adapter = ShopBannerVPAdapter(requireActivity(), items)
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
        binding.limitedSaleFrameContainerCl.visibility = View.VISIBLE
        binding.regularSaleFrameContainerCl.visibility = View.GONE
        binding.limitedSaleIconContainerCl.visibility = View.VISIBLE
        binding.regularSaleIconContainerCl.visibility = View.GONE
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
                    binding.limitedSaleFrameContainerCl.visibility = View.GONE
                    binding.regularSaleFrameContainerCl.visibility = View.VISIBLE
                    binding.limitedSaleIconContainerCl.visibility = View.GONE
                    binding.regularSaleIconContainerCl.visibility = View.VISIBLE

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
                    binding.limitedSaleFrameContainerCl.visibility = View.VISIBLE
                    binding.regularSaleFrameContainerCl.visibility = View.GONE
                    binding.limitedSaleIconContainerCl.visibility = View.VISIBLE
                    binding.regularSaleIconContainerCl.visibility = View.GONE

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
