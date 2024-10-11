package com.example.favoriteplace

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.favoriteplace.LoginActivity.Companion.ACCESS_TOKEN_KEY
import com.example.favoriteplace.databinding.FragmentShopMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class ShopMainFragment : Fragment() {
    private var _binding: FragmentShopMainBinding? = null
    private val binding get() = _binding!!

    private val handler = Handler(Looper.getMainLooper())
    private val imageResIds = listOf(R.drawable.shop_banner1, R.drawable.shop_banner2)
    private lateinit var slideRunnable: Runnable

    private var limitedNewFrameData = ArrayList<ShopMainLimitedFame>()
    private var limitedUMCFrameData = ArrayList<ShopMainLimitedFame>()
    private var limitedNewIconData = ArrayList<ShopMainLimitedIcon>()
    private var limitedUMCIconData = ArrayList<ShopMainLimitedIcon>()
    private var regularFrameData = ArrayList<ShopMainUnlimitedFame>()
    private var regularFrameNormalData = ArrayList<ShopMainUnlimitedFame>()
    private var regularNewIconData = ArrayList<ShopMainUnlimitedIcon>()
    private var regularNormalIconData = ArrayList<ShopMainUnlimitedIcon>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopMainBinding.inflate(inflater, container, false)

        // 스위치 상태를 항상 한정판매로 설정 (false)
        binding.shopMainSwitchOnOffSc.isChecked = false
        // 초기 탭 색상 설정 (한정판매 선택됨)
        updateTabUI(isLimitedSale = true)

        // 스위치 초기 상태에 따라 레이아웃 설정
        if (binding.shopMainSwitchOnOffSc.isChecked) {
            binding.limitedSaleFrameContainerCl.visibility = View.GONE
            binding.regularSaleFrameContainerCl.visibility = View.VISIBLE
        } else {
            binding.limitedSaleFrameContainerCl.visibility = View.VISIBLE
            binding.regularSaleFrameContainerCl.visibility = View.GONE
        }

        // include로 가져온 레이아웃에서 뷰 참조
        val memberProfileLayout = binding.root.findViewById<View>(R.id.shopMain_memberProfile_cl)
        val notLoginLayout = binding.root.findViewById<View>(R.id.shopMain_notLogin_cl)

        val loginButton = notLoginLayout.findViewById<View>(R.id.shopMain_loginButton_btn)

        // 로그인 버튼 클릭 처리
        loginButton.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            if (!isLoggedIn()) startActivity(intent)
        }

        // 로그인 상태에 따라 UI 업데이트
        updateLoginStatusView(memberProfileLayout, notLoginLayout)

        // slideRunnable 초기화
        slideRunnable = Runnable {
            if (isAdded) {
                val nextItem = (binding.shopMainBannerVp2.currentItem + 1) % imageResIds.size
                binding.shopMainBannerVp2.currentItem = nextItem
                handler.postDelayed(slideRunnable, 3000)
            }
        }

        // 배너 슬라이드 설정
        setupBannerViewPager()

        // 스위치 상태 변경 리스너 설정
        binding.shopMainSwitchOnOffSc.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                // 한정판매 관련 레이아웃 보이기
                binding.limitedSaleFrameContainerCl.visibility = View.VISIBLE
                binding.regularSaleFrameContainerCl.visibility = View.GONE
                binding.limitedSaleIconContainerCl.visibility = View.VISIBLE
                binding.regularSaleIconContainerCl.visibility = View.GONE
            } else {
                // 상시판매 관련 레이아웃 보이기
                binding.limitedSaleFrameContainerCl.visibility = View.GONE
                binding.regularSaleFrameContainerCl.visibility = View.VISIBLE
                binding.limitedSaleIconContainerCl.visibility = View.GONE
                binding.regularSaleIconContainerCl.visibility = View.VISIBLE
            }

            // 스위치 상태에 따른 탭 UI 업데이트
            updateTabUI(isLimitedSale = !isChecked)
        }

        // 데이터 로드 및 UI 설정
        fetchSalesData()

        return binding.root
    }

    // 로그인 상태에 따라 뷰를 보이게 처리하는 메서드
    private fun updateLoginStatusView(memberProfileLayout: View, notLoginLayout: View) {
        if (isLoggedIn()) {
            memberProfileLayout.visibility = View.VISIBLE
            notLoginLayout.visibility = View.GONE
        } else {
            memberProfileLayout.visibility = View.GONE
            notLoginLayout.visibility = View.VISIBLE
        }
    }
    // sharePreferences에 저장된 액세스 토큰 반환하는 메소드
    private fun getAccessToken(): String? {
        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences?.getString(ACCESS_TOKEN_KEY, null)
    }

    // 사용자의 로그인 상태를 확인하는 메소드
    private fun isLoggedIn(): Boolean {
        return getAccessToken() != null
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
        // 기존에 등록된 콜백을 모두 제거하여 중복 호출 방지
        // 기존에 등록된 콜백을 제거하고 슬라이드 시작
        handler.removeCallbacks(slideRunnable)
        handler.postDelayed(slideRunnable, 3000)
    }

    private fun stopAutoSlide() {
        handler.removeCallbacks(slideRunnable)
    }

    override fun onResume() {
        super.onResume()
        startAutoSlide()

        // include로 가져온 레이아웃도 다시 업데이트
        val memberProfileLayout = binding.root.findViewById<View>(R.id.shopMain_memberProfile_cl)
        val notLoginLayout = binding.root.findViewById<View>(R.id.shopMain_notLogin_cl)
        updateLoginStatusView(memberProfileLayout, notLoginLayout)
    }

    override fun onPause() {
        super.onPause()
        stopAutoSlide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 데이터 로드 (한정 판매 및 상시 판매)
    private fun fetchSalesData() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val accessToken = getAccessToken()
                val authorizationHeader = "Bearer $accessToken"

                val limitedSalesResponse = RetrofitClient.shopService.getLimitedSales(authorizationHeader).awaitResponse()
                val unlimitedSalesResponse = RetrofitClient.shopService.getUnlimitedSales(authorizationHeader).awaitResponse()

                if (limitedSalesResponse.isSuccessful && unlimitedSalesResponse.isSuccessful) {
                    limitedSalesResponse.body()?.let { updateLimitedSalesUI(it) }
                    unlimitedSalesResponse.body()?.let { updateUnlimitedSalesUI(it) }
                } else {
                    Log.d("ShopMainFragment", "API Error: ${limitedSalesResponse.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.d("ShopMainFragment", "Network Error: ${e.message}")
            }
        }
    }

    private fun updateLimitedSalesUI(response: SalesResponse) {
        lifecycleScope.launch(Dispatchers.Main) {
            limitedNewFrameData.clear()
            limitedUMCFrameData.clear()
            response.titles.forEach { category ->
                when (category.category) {
                    "NEW!" -> {
                        limitedNewFrameData.addAll(category.itemList.map {
                            ShopMainLimitedFame(it.imageUrl, it.point.toString(), it.id)
                        })
                    }
                    "UMC" -> {
                        limitedUMCFrameData.addAll(category.itemList.map {
                            ShopMainLimitedFame(it.imageUrl, it.point.toString(), it.id)
                        })
                    }
                }
            }

            // 한정판매 어댑터 설정
            binding.shopMainLimitedFrameRv.adapter = ShopBannerLimitedFameRVAdapter(limitedNewFrameData)
            binding.shopMainLimitedFrameUMCRv.adapter = ShopBannerLimitedFameRVAdapter(limitedUMCFrameData)

            // 아이콘 데이터 업데이트
            limitedNewIconData.clear()
            limitedUMCIconData.clear()
            response.icons.forEach { category ->
                when (category.category) {
                    "NEW!" -> {
                        limitedNewIconData.addAll(category.itemList.map {
                            ShopMainLimitedIcon(it.imageUrl, it.name, it.point.toString(), it.id)
                        })
                    }
                    "UMC" -> {
                        limitedUMCIconData.addAll(category.itemList.map {
                            ShopMainLimitedIcon(it.imageUrl, it.name, it.point.toString(), it.id)
                        })
                    }
                }
            }

            binding.shopMainLimitedIconNewRv.adapter = ShopBannerLimitedIconRVAdapter(limitedNewIconData)
            binding.shopMainLimitedIconUMCRv.adapter = ShopBannerLimitedIconRVAdapter(limitedUMCIconData)
        }
    }

    private fun updateUnlimitedSalesUI(response: SalesResponse) {
        lifecycleScope.launch(Dispatchers.Main) {
            regularFrameData.clear()
            regularFrameNormalData.clear()

            response.titles.forEach { category ->
                when (category.category) {
                    "New!" -> {
                        regularFrameData.addAll(category.itemList.map {
                            ShopMainUnlimitedFame(it.imageUrl, it.point.toString(), it.id)
                        })
                    }
                    "Normal" -> {
                        regularFrameNormalData.addAll(category.itemList.map {
                            ShopMainUnlimitedFame(it.imageUrl, it.point.toString(), it.id)
                        })
                    }
                }
            }

            // 상시판매 어댑터 설정
            binding.shopMainRegularFrameRv.adapter = ShopBannerUnlimitedFameRVAdapter(regularFrameData)
            binding.shopMainRegularFrameNormalRv.adapter = ShopBannerUnlimitedFameRVAdapter(regularFrameNormalData)

            // 아이콘 데이터 업데이트
            regularNewIconData.clear()
            regularNormalIconData.clear()
            response.icons.forEach { category ->
                when (category.category) {
                    "New!" -> {
                        regularNewIconData.addAll(category.itemList.map {
                            ShopMainUnlimitedIcon(it.imageUrl, it.name, it.point.toString(), it.id)
                        })
                    }
                    "Normal" -> {
                        regularNormalIconData.addAll(category.itemList.map {
                            ShopMainUnlimitedIcon(it.imageUrl, it.name, it.point.toString(), it.id)
                        })
                    }
                }
            }

            binding.shopMainRegularIconNewRv.adapter = ShopBannerUnlimitedIconRVAdapter(regularNewIconData)
            binding.shopMainRegularIconNormalRv.adapter = ShopBannerUnlimitedIconRVAdapter(regularNormalIconData)
        }
    }

    private fun updateTabUI(isLimitedSale: Boolean) {
        if (isLimitedSale) {
            // 한정판매 탭 선택 시
            binding.shopMainSwitchLimitedTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.shopMainSwitchRegularTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        } else {
            // 상시판매 탭 선택 시
            binding.shopMainSwitchLimitedTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.shopMainSwitchRegularTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }
}
