package com.example.favoriteplace

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

    private var limitedNewFrameData = ArrayList<ShopMainLimitedFame>()
    private var limitedUMCFrameData = ArrayList<ShopMainLimitedFame>()
    private var limitedNewIconData = ArrayList<ShopMainLimitedIcon>()
    private var limitedUMCIconData = ArrayList<ShopMainLimitedIcon>()
    private var regularFrameData = ArrayList<ShopMainUnlimitedFame>()
    private var regularFrameNormalData = ArrayList<ShopMainUnlimitedFame>()
    private var regularNewIconData = ArrayList<ShopMainUnlimitedIcon>()
    private var regularNormalIconData = ArrayList<ShopMainUnlimitedIcon>()

    // ShopMainLimitedFame 객체를 저장할 변수
    private var selectedLimitedFame: ShopMainLimitedFame? = null



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
                binding.shopMainBannerVp2.currentItem =
                    (binding.shopMainBannerVp2.currentItem + 1) % imageResIds.size
                handler.postDelayed(slideRunnable, 3000)
            }
        }

        // 한정판매 칭호 - NEW
        limitedNewFrameData.apply {
            add(ShopMainLimitedFame(R.drawable.limited_frame_1.toString(), "10000P"))
            add(ShopMainLimitedFame(R.drawable.limited_frame_2.toString(), "30000P"))
            add(ShopMainLimitedFame(R.drawable.limited_frame_3.toString(), "300000P"))
        }

        val limitedFrameAdapter = ShopBannerLimitedFameRVAdapter(limitedNewFrameData)
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

        // 한정판매 칭호 - UMC

        limitedUMCFrameData.apply {
            add(ShopMainLimitedFame(R.drawable.limited_frame_umc_1.toString(), "5000P"))
            add(ShopMainLimitedFame(R.drawable.limited_frame_umc_2.toString(), "5000P"))
            add(ShopMainLimitedFame(R.drawable.limited_frame_umc_3.toString(), "5000P"))
        }

        val limitedUMCFrameAdapter = ShopBannerLimitedFameRVAdapter(limitedUMCFrameData)
        binding.shopMainLimitedFrameUMCRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.shopMainLimitedFrameUMCRv.adapter = limitedUMCFrameAdapter


        // 한정판매 아이콘 NEW
        limitedNewIconData.apply {
            add(ShopMainLimitedIcon(R.drawable.limited_icon_new_1.toString(), "산타 모자", "10000P"))
            add(ShopMainLimitedIcon(R.drawable.limited_icon_new_2.toString(), "컨페티", "10000P"))
            add(ShopMainLimitedIcon(R.drawable.limited_icon_new_3.toString(), "브이", "10000P"))
        }

        val limitedNewIconAdapter = ShopBannerLimitedIconRVAdapter(limitedNewIconData)
        binding.shopMainLimitedIconNewRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.shopMainLimitedIconNewRv.adapter = limitedNewIconAdapter

        // 한정판매 아이콘 UMC
        limitedUMCIconData.apply {
            add(ShopMainLimitedIcon(R.drawable.limited_icon_umc_1.toString(), "유닝이", "20000P"))
            add(ShopMainLimitedIcon(R.drawable.limited_icon_umc_2.toString(), "UMC 5기", "20000P"))
            add(
                ShopMainLimitedIcon(
                    R.drawable.limited_icon_umc_3.toString(),
                    "Developer",
                    "10000P"
                )
            )
        }

        val limitedUMCIconnAdapter = ShopBannerLimitedIconRVAdapter(limitedUMCIconData)
        binding.shopMainLimitedIconUMCRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.shopMainLimitedIconUMCRv.adapter = limitedUMCIconnAdapter


        // 상시판매 칭호 - NEW

        regularFrameData.apply {
            add(ShopMainUnlimitedFame(R.drawable.regular_frame_new_1.toString(), "5000P"))
            add(ShopMainUnlimitedFame(R.drawable.regular_frame_new_2.toString(), "10000P"))
            add(ShopMainUnlimitedFame(R.drawable.regular_frame_new_3.toString(), "100000P"))
        }

        val regularFrameAdapter = ShopBannerUnlimitedFameRVAdapter(regularFrameData)
        binding.shopMainRegularFrameRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.shopMainRegularFrameRv.adapter = regularFrameAdapter

        // 상시판매 칭호 - Normal

        regularFrameNormalData.apply {
            add(ShopMainUnlimitedFame(R.drawable.regular_frame_normal_1.toString(), "50P"))
            add(ShopMainUnlimitedFame(R.drawable.regular_frame_normal_2.toString(), "10000P"))
            add(ShopMainUnlimitedFame(R.drawable.regular_frame_normal_3.toString(), "500000P"))
        }

        val regularFrameNormalAdapter = ShopBannerUnlimitedFameRVAdapter(regularFrameNormalData)
        binding.shopMainRegularFrameNormalRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.shopMainRegularFrameNormalRv.adapter = regularFrameNormalAdapter


        // 상시판매 아이콘 - NEW
        regularNewIconData.apply {
            add(ShopMainUnlimitedIcon(R.drawable.regular_icon_new_1.toString(), "별행성", "10000P"))
            add(ShopMainUnlimitedIcon(R.drawable.regular_icon_new_2.toString(), "새턴", "10000P"))
            add(ShopMainUnlimitedIcon(R.drawable.regular_icon_new_3.toString(), "초승달", "10000P"))
        }

        val regularNewIconAdapter = ShopBannerUnlimitedIconRVAdapter(regularNewIconData)
        binding.shopMainRegularIconNewRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.shopMainRegularIconNewRv.adapter = regularNewIconAdapter

        // 상시판매 아이콘 - Normal
        regularNormalIconData.apply {
            add(
                ShopMainUnlimitedIcon(
                    R.drawable.regular_icon_normal_1.toString(),
                    "스포트라이트",
                    "20000P"
                )
            )
            add(ShopMainUnlimitedIcon(R.drawable.regular_icon_normal_2.toString(), "하트", "20000P"))
            add(
                ShopMainUnlimitedIcon(
                    R.drawable.regular_icon_normal_3.toString(),
                    "시그니처 별",
                    "10000P"
                )
            )
        }

        val regularNormalIconAdapter = ShopBannerUnlimitedIconRVAdapter(regularNormalIconData)
        binding.shopMainRegularIconNormalRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.shopMainRegularIconNormalRv.adapter = regularNormalIconAdapter

        val loginButton = binding.shopMainLoginButtonBtn
        loginButton.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            if (!isLoggedIn()) startActivity(intent)
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

        val accessToken =
            "yJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzanVAbmF2ZXIuY29tIiwiYXV0aCI6IiIsImV4cCI6MTcwNjcwMTI2NH0.2PxFwZIDCCsERyb4a31d_TbbIydJzAGj1PfCRDDV7bw"

        val callLimitedSales = if (isLoggedIn()) {
            RetrofitClient.shopService.getLimitedSales(authorization = accessToken)
        } else {
            RetrofitClient.shopService.getLimitedSales(null)
        }

        Log.d("ShopMainFragment", " use token : ${accessToken}")

        callLimitedSales.enqueue(object : Callback<LimitedSalesResponse> {
            override fun onResponse(
                call: Call<LimitedSalesResponse>,
                response: Response<LimitedSalesResponse>
            ) {
                if (response.isSuccessful) {
                    // 성공적으로 데이터를 받음. 여기서 UI 업데이트 로직을 구현합니다.
                    val limitedSalesResponse = response.body()

                    // 받아온 데이터를 로그로 출력
                    Log.d("ShopMainFragment", "Limited sales data received: $limitedSalesResponse")

                    // 받아온 데이터로 한정판매 칭호와 아이콘 리스트 업데이트
                    limitedSalesResponse?.let {
                        // 한정판매 칭호 리스트 업데이트
                        limitedNewFrameData.clear()
                        limitedUMCFrameData.clear()
                        var newLimitedCategoryFound = false // "NEW!" 카테고리를 찾았는지 여부를 추적하는 변수
                        var umcLimitedCategoryFound = false // "NEW!" 카테고리를 찾았는지 여부를 추적하는 변수

                        it.titles.forEach { category ->
                            when (category.category) {
                                "NEW!" -> {
                                    newLimitedCategoryFound = true // "NEW!" 카테고리를 찾았음을 표시
                                    limitedNewFrameData.addAll(category.itemList.map { item ->
                                        ShopMainLimitedFame(item.imageUrl, item.point.toString())
                                    })
                                }

                                "UMC" -> {
                                    umcLimitedCategoryFound = true
                                    limitedUMCFrameData.addAll(category.itemList.map { item ->
                                        ShopMainLimitedFame(item.imageUrl, item.point.toString())
                                    })
                                }
                            }
                        }
                        if (!newLimitedCategoryFound) {
                            binding.limitedFrameNewCl.visibility = View.INVISIBLE
                        }
                        if (!umcLimitedCategoryFound) {
                            binding.limitedFrameUmcCl.visibility = View.INVISIBLE
                        }

                        limitedNewIconData.clear()
                        limitedUMCIconData.clear()

                        var newLimitedIconCategoryFound = false // "NEW!" 카테고리를 찾았는지 여부를 추적하는 변수
                        var umcLimitedIconCategoryFound = false // "NEW!" 카테고리를 찾았는지 여부를 추적하는 변수

                        // 한정판매 아이콘 리스트 업데이트
                        it.icons.forEach { category ->
                            when (category.category) {
                                "NEW!" -> {
                                    newLimitedIconCategoryFound = true
                                    limitedNewIconData.addAll(category.itemList.map { item ->
                                        ShopMainLimitedIcon(
                                            item.imageUrl,
                                            item.name,
                                            item.point.toString()
                                        )
                                    })
                                }

                                "UMC" -> {
                                    umcLimitedIconCategoryFound = true
                                    limitedUMCIconData.addAll(category.itemList.map { item ->
                                        ShopMainLimitedIcon(
                                            item.imageUrl,
                                            item.name,
                                            item.point.toString()
                                        )
                                    })
                                }
                            }
                        }

                        if (!newLimitedIconCategoryFound) {
                            binding.limitedIconNewCl.visibility = View.INVISIBLE
                        }
                        if (!umcLimitedIconCategoryFound) {
                            binding.limitedIconUmcCl.visibility = View.INVISIBLE
                        }

                        // UI 업데이트를 위한 함수 호출
                        updateLimitedSalesUI()
                    }
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

        val callUnlimitedSales = if (isLoggedIn()) {
            RetrofitClient.shopService.getUnlimitedSales(authorization = accessToken)
        } else {
            RetrofitClient.shopService.getUnlimitedSales(null)
        }

        callUnlimitedSales.enqueue(object : Callback<UnlimitedSalesResponse> {
            override fun onResponse(
                call: Call<UnlimitedSalesResponse>,
                response: Response<UnlimitedSalesResponse>
            ) {
                if (response.isSuccessful) {
                    // 성공적으로 데이터를 받음. 여기서 UI 업데이트 로직을 구현합니다.
                    val unlimitedSalesResponse = response.body()

                    // 받아온 데이터를 로그로 출력
                    Log.d(
                        "ShopMainFragment",
                        "Unlimited sales data received: $unlimitedSalesResponse"
                    )

                    // 받아온 데이터로 상시판매 칭호와 아이콘 리스트 업데이트
                    unlimitedSalesResponse?.let {
                        // 상시판매 칭호 리스트 업데이트
                        regularFrameData.clear()
                        regularFrameNormalData.clear()
                        var newUnlimitedFrameCategoryFound =
                            false // "NEW!" 카테고리를 찾았는지 여부를 추적하는 변수
                        var normalUnlimitedFrameCategoryFound =
                            false // "NEW!" 카테고리를 찾았는지 여부를 추적하는 변수

                        it.titles.forEach { category ->
                            when (category.category) {
                                "New!" -> {
                                    newUnlimitedFrameCategoryFound =
                                        true // "NEW!" 카테고리를 찾았음을 표시
                                    regularFrameData.addAll(category.itemList.map { item ->
                                        ShopMainUnlimitedFame(
                                            item.imageUrl,
                                            item.point.toString()
                                        )
                                    })
                                }

                                "Normal" -> {
                                    normalUnlimitedFrameCategoryFound = true
                                    regularFrameNormalData.addAll(category.itemList.map { item ->
                                        ShopMainUnlimitedFame(
                                            item.imageUrl,
                                            item.point.toString()
                                        )
                                    })
                                }
                            }
                        }
                        if (!newUnlimitedFrameCategoryFound) {
                            binding.unlimitedFrameNewCl.visibility = View.INVISIBLE
                        }
                        if (!normalUnlimitedFrameCategoryFound) {
                            binding.unlimitedFrameNormalCl.visibility = View.INVISIBLE
                        }

                        regularNewIconData.clear()
                        regularNormalIconData.clear()

                        var newUnlimitedIconCategoryFound =
                            false // "NEW!" 카테고리를 찾았는지 여부를 추적하는 변수
                        var normalUnlimitedIconCategoryFound =
                            false // "NEW!" 카테고리를 찾았는지 여부를 추적하는 변수

                        // 한정판매 아이콘 리스트 업데이트
                        it.icons.forEach { category ->
                            when (category.category) {
                                "New!" -> {
                                    newUnlimitedIconCategoryFound = true
                                    regularNewIconData.addAll(category.itemList.map { item ->
                                        ShopMainUnlimitedIcon(
                                            item.imageUrl,
                                            item.name,
                                            item.point.toString()
                                        )
                                    })
                                }

                                "Normal" -> {
                                    normalUnlimitedIconCategoryFound = true
                                    regularNormalIconData.addAll(category.itemList.map { item ->
                                        ShopMainUnlimitedIcon(
                                            item.imageUrl,
                                            item.name,
                                            item.point.toString()
                                        )
                                    })
                                }
                            }
                        }

                        if (!newUnlimitedIconCategoryFound) {
                            binding.unlimitedIconNewCl.visibility = View.INVISIBLE
                        }
                        if (!normalUnlimitedIconCategoryFound) {
                            binding.unlimitedIconNormalCl.visibility = View.INVISIBLE
                        }

                        // UI 업데이트를 위한 함수 호출
                        updateUnlimitedSalesUI()
                    }
                    // 예: 받아온 데이터를 기반으로 UI 업데이트
                } else {
                    Log.d("ShopMainFragment", "API Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<UnlimitedSalesResponse>, t: Throwable) {
                // 네트워크 오류 등의 실패 처리
                Log.d("ShopMainFragment", "Network Error: ${t.message}")
            }
        })
    }

    private fun updateLimitedSalesUI() {
        // 한정판매 칭호 어댑터 업데이트
        try {
            val limitedNewFrameDataAdapter = ShopBannerLimitedFameRVAdapter(limitedNewFrameData)
            binding.shopMainLimitedFrameRv.adapter = limitedNewFrameDataAdapter

            limitedNewFrameDataAdapter.setMyItemClickListener(object :
                ShopBannerLimitedFameRVAdapter.MyItemClickListener {
                override fun onItemClick() {
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, ShopMainLimitedFameFragment())
                        .commitAllowingStateLoss()
                }
            })


            val limitedUMCFrameDataAdapter = ShopBannerLimitedFameRVAdapter(limitedUMCFrameData)
            binding.shopMainLimitedFrameUMCRv.adapter = limitedUMCFrameDataAdapter

            limitedUMCFrameDataAdapter.setMyItemClickListener(object :
                ShopBannerLimitedFameRVAdapter.MyItemClickListener {
                override fun onItemClick() {
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, ShopMainLimitedFameFragment())
                        .commitAllowingStateLoss()
                }
            })

            // 한정판매 아이콘 어댑터 업데이트
            val limitedNewIconAdapter = ShopBannerLimitedIconRVAdapter(limitedNewIconData)
            binding.shopMainLimitedIconNewRv.adapter = limitedNewIconAdapter

            limitedNewIconAdapter.setMyItemClickListener(object :
                ShopBannerLimitedIconRVAdapter.MyItemClickListener {
                override fun onItemClick() {
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, ShopMainLimitedIconFragment())
                        .commitAllowingStateLoss()
                }
            })

            val limitedUMCIconAdapter = ShopBannerLimitedIconRVAdapter(limitedUMCIconData)
            binding.shopMainLimitedIconUMCRv.adapter = limitedUMCIconAdapter

            limitedUMCIconAdapter.setMyItemClickListener(object :
                ShopBannerLimitedIconRVAdapter.MyItemClickListener {
                override fun onItemClick() {
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, ShopMainLimitedIconFragment())
                        .commitAllowingStateLoss()
                }
            })

        } catch (e: Exception) {
            Log.e("ShopMainUpdateUI", "Error in update(): ${e.message}")
        }

    }

    private fun updateUnlimitedSalesUI() {
        // 상시판매 칭호 어댑터 업데이트
        try {
            val regularNewFrameDataAdapter = ShopBannerUnlimitedFameRVAdapter(regularFrameData)
            binding.shopMainRegularFrameRv.adapter = regularNewFrameDataAdapter

            regularNewFrameDataAdapter.setMyItemClickListener(object :
                ShopBannerUnlimitedFameRVAdapter.MyItemClickListener {
                override fun onItemClick() {
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, ShopMainUnlimitedFameFragment())
                        .commitAllowingStateLoss()
                }
            })

            val regularNormalFrameDataAdapter =
                ShopBannerUnlimitedFameRVAdapter(regularFrameNormalData)
            binding.shopMainRegularFrameNormalRv.adapter = regularNormalFrameDataAdapter
            regularNormalFrameDataAdapter.setMyItemClickListener(object :
                ShopBannerUnlimitedFameRVAdapter.MyItemClickListener {
                override fun onItemClick() {
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, ShopMainUnlimitedFameFragment())
                        .commitAllowingStateLoss()
                }
            })


            // 상시판매 아이콘 어댑터 업데이트
            val regularNewIconAdapter = ShopBannerUnlimitedIconRVAdapter(regularNewIconData)
            binding.shopMainRegularIconNewRv.adapter = regularNewIconAdapter

            regularNewIconAdapter.setMyItemClickListener(object :
                ShopBannerUnlimitedIconRVAdapter.MyItemClickListener {
                override fun onItemClick() {
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, ShopMainLimitedIconFragment())
                        .commitAllowingStateLoss()
                }
            })

            val regularNormalIconAdapter = ShopBannerUnlimitedIconRVAdapter(regularNormalIconData)
            binding.shopMainRegularIconNormalRv.adapter = regularNormalIconAdapter

            regularNormalIconAdapter.setMyItemClickListener(object :
                ShopBannerUnlimitedIconRVAdapter.MyItemClickListener {
                override fun onItemClick() {
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, ShopMainLimitedIconFragment())
                        .commitAllowingStateLoss()
                }
            })


        } catch (e: Exception) {
            Log.e("ShopMainUpdateUI", "Error in update(): ${e.message}")
        }

    }
}
