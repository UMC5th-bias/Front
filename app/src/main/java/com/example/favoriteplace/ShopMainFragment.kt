package com.example.favoriteplace

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.favoriteplace.databinding.FragmentShopMainBinding

class ShopMainFragment : Fragment() {
    lateinit var binding: FragmentShopMainBinding

    private lateinit var yesLoginView: View
    private lateinit var notLoginView: View

    private val handler = Handler(Looper.getMainLooper())
    private val imageResIds = listOf(R.drawable.shop_banner1, R.drawable.shop_banner2)
    private lateinit var slideRunnable: Runnable

    private var limitedNewIconData=ArrayList<LimitedIcon>()
    private var limitedUMCIconData=ArrayList<LimitedIcon>()
    private var unlimitedIconData=ArrayList<UnlimitedIcon>()

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

        // 한정판매 칭호

        val limitedFrameData = listOf(
            LimitedFame(R.drawable.limited_frame_1, "10000P"),
            LimitedFame(R.drawable.limited_frame_2, "30000P"),
            LimitedFame(R.drawable.limited_frame_3, "300000P")
        )

        val limitedFrameAdapter = ShopBannerLimitedFameRVAdapter(limitedFrameData)
        binding.shopMainLimitedFrameRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.shopMainLimitedFrameRv.adapter = limitedFrameAdapter

//        limitedFrameAdapter.setMyItemClickListener(object :ShopBannerLimitedFameRVAdapter.MyItemClickListener{
//            override fun onItemClick() {
//                (context as MainActivity).supportFragmentManager.beginTransaction()
//                    .replace(R.id.main_frameLayout, ShopBannerLimitedFameFragment())
//                    .commitAllowingStateLoss()
//            }
//        })

        val limitedUMCFrameData = listOf(
            LimitedFame(R.drawable.limited_frame_umc_1, "5000P"),
            LimitedFame(R.drawable.limited_frame_umc_2, "5000P"),
            LimitedFame(R.drawable.limited_frame_umc_3, "5000P")
        )

        val limitedUMCFrameAdapter = ShopBannerLimitedFameRVAdapter(limitedUMCFrameData)
        binding.shopMainLimitedFrameUMCRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.shopMainLimitedFrameUMCRv.adapter = limitedUMCFrameAdapter

        // 한정판매 아이콘

        val limitedNewIconData = listOf(
            LimitedIcon(R.drawable.limited_icon_new_1, "산타 모자", "10000P"),
            LimitedIcon(R.drawable.limited_icon_new_2, "컨페티", "10000P"),
            LimitedIcon(R.drawable.limited_icon_new_3, "브이", "10000P")
        )

        val limitedNewIconAdapter = ShopBannerLimitedIconRVAdapter(limitedNewIconData)
        binding.shopMainLimitedIconNewRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.shopMainLimitedIconNewRv.adapter = limitedNewIconAdapter

        val limitedUMCIconData = listOf(
            LimitedIcon(R.drawable.limited_icon_umc_1, "유닝이", "20000P"),
            LimitedIcon(R.drawable.limited_icon_umc_2, "UMC 5기", "20000P"),
            LimitedIcon(R.drawable.limited_icon_umc_3, "Developer", "10000P")
        )

        val limitedUMCIconnAdapter = ShopBannerLimitedIconRVAdapter(limitedUMCIconData)
        binding.shopMainLimitedIconUMCRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.shopMainLimitedIconUMCRv.adapter = limitedUMCIconnAdapter


        // 상시판매 칭호

        val regularFrameData = listOf(
            UnlimitedFame(R.drawable.regular_frame_new_1, "5000P"),
            UnlimitedFame(R.drawable.regular_frame_new_2, "10000P"),
            UnlimitedFame(R.drawable.regular_frame_new_3, "100000P")
        )

        val regularFrameAdapter = ShopBannerUnlimitedFameRVAdapter(regularFrameData)
        binding.shopMainRegularFrameRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.shopMainRegularFrameRv.adapter = regularFrameAdapter

        val regularFrameNormalData = listOf(
            UnlimitedFame(R.drawable.regular_frame_normal_1, "50P"),
            UnlimitedFame(R.drawable.regular_frame_normal_2, "10000P"),
            UnlimitedFame(R.drawable.regular_frame_normal_3, "500000P")
        )
        val regularFrameNormalAdapter = ShopBannerUnlimitedFameRVAdapter(regularFrameNormalData)
        binding.shopMainRegularFrameNormalRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.shopMainRegularFrameNormalRv.adapter = regularFrameNormalAdapter

        // 상시판매 아이콘
        val regularNewIconData = listOf(
            UnlimitedIcon(R.drawable.regular_icon_new_1, "별행성", "10000P"),
            UnlimitedIcon(R.drawable.regular_icon_new_2, "새턴", "10000P"),
            UnlimitedIcon(R.drawable.regular_icon_new_3, "초승달", "10000P")
        )

        val regularNewIconAdapter = ShopBannerUnlimitedIconRVAdapter(regularNewIconData)
        binding.shopMainRegularIconNewRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.shopMainRegularIconNewRv.adapter = regularNewIconAdapter

        val regularNormalIconData = listOf(
            UnlimitedIcon(R.drawable.regular_icon_normal_1, "스포트라이트", "20000P"),
            UnlimitedIcon(R.drawable.regular_icon_normal_2, "하트", "20000P"),
            UnlimitedIcon(R.drawable.regular_icon_normal_3, "시그니처 별", "10000P")
        )

        val regularNormalIconAdapter = ShopBannerUnlimitedIconRVAdapter(regularNormalIconData)
        binding.shopMainRegularIconNormalRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.shopMainRegularIconNormalRv.adapter = regularNormalIconAdapter

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

        // 한정 판매 목록 로드
        fetchLimitedSales()


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

    // 한정 판매 목록 로드 메서드
    private fun fetchLimitedSales() {
        // RetrofitClient.shopService.getLimitedSales("Bearer YOUR_AUTH_TOKEN") 호출 구현 필요
        // 예시를 위한 가상 코드입니다. 실제로는 YOUR_AUTH_TOKEN을 적절한 토큰으로 대체해야 합니다.
        RetrofitClient.shopService.getLimitedSales("Bearer YOUR_AUTH_TOKEN").enqueue(object : Callback<LimitedSalesResponse> {
            override fun onResponse(call: Call<LimitedSalesResponse>, response: Response<LimitedSalesResponse>) {
                if (response.isSuccessful) {
                    // 성공적으로 데이터를 받음. 여기서 UI 업데이트 로직을 구현합니다.
                    val limitedSalesResponse = response.body()

                    // 받아온 데이터를 로그로 출력
                    Log.d("ShopMainFragment", "Limited sales data received: $limitedSalesResponse")

                    // 예: 받아온 데이터를 기반으로 UI 업데이트
                } else {
                    Log.d("ShopMainFragment", "API Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<LimitedSalesResponse>, t: Throwable) {
                // 네트워크 오류 등의 실패 처리
                Log.d("ShopMainFragment", "Network Error: ${t.message}")
            }
        })
    }
}
