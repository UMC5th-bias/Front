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
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.favoriteplace.LoginActivity.Companion.ACCESS_TOKEN_KEY
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
            add(ShopMainLimitedFame(R.drawable.limited_frame_1.toString(), "10000P", 0))
            add(ShopMainLimitedFame(R.drawable.limited_frame_2.toString(), "30000P", 1))
            add(ShopMainLimitedFame(R.drawable.limited_frame_3.toString(), "300000P", 2))
        }

//         한정판매 칭호 - UMC

        limitedUMCFrameData.apply {
            add(ShopMainLimitedFame(R.drawable.limited_frame_umc_1.toString(), "5000P", 3))
            add(ShopMainLimitedFame(R.drawable.limited_frame_umc_2.toString(), "5000P", 4))
            add(ShopMainLimitedFame(R.drawable.limited_frame_umc_3.toString(), "5000P", 5))
        }
        val limitedUMCFrameAdapter = ShopBannerLimitedFameRVAdapter(limitedUMCFrameData)
        binding.shopMainLimitedFrameUMCRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.shopMainLimitedFrameUMCRv.adapter = limitedUMCFrameAdapter


        // 한정판매 아이콘 NEW
        limitedNewIconData.apply {
            add(ShopMainLimitedIcon(R.drawable.limited_icon_new_1.toString(), "산타 모자", "10000P", 0))
            add(ShopMainLimitedIcon(R.drawable.limited_icon_new_2.toString(), "컨페티", "10000P", 1))
            add(ShopMainLimitedIcon(R.drawable.limited_icon_new_3.toString(), "브이", "10000P", 2))
        }

        val limitedNewIconAdapter = ShopBannerLimitedIconRVAdapter(limitedNewIconData)
        binding.shopMainLimitedIconNewRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.shopMainLimitedIconNewRv.adapter = limitedNewIconAdapter

        // 한정판매 아이콘 UMC
        limitedUMCIconData.apply {
            add(ShopMainLimitedIcon(R.drawable.limited_icon_umc_1.toString(), "유닝이", "20000P", 3))
            add(ShopMainLimitedIcon(R.drawable.limited_icon_umc_2.toString(), "UMC 5기", "20000P", 4))
            add(
                ShopMainLimitedIcon(
                    R.drawable.limited_icon_umc_3.toString(),
                    "Developer",
                    "10000P",
                    5
                )
            )
        }

        val limitedUMCIconnAdapter = ShopBannerLimitedIconRVAdapter(limitedUMCIconData)
        binding.shopMainLimitedIconUMCRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.shopMainLimitedIconUMCRv.adapter = limitedUMCIconnAdapter


        // 상시판매 칭호 - NEW

        regularFrameData.apply {
            add(ShopMainUnlimitedFame(R.drawable.regular_frame_new_1.toString(), "5000P", 0))
            add(ShopMainUnlimitedFame(R.drawable.regular_frame_new_2.toString(), "10000P", 1))
            add(ShopMainUnlimitedFame(R.drawable.regular_frame_new_3.toString(), "100000P", 2))
        }

        val regularFrameAdapter = ShopBannerUnlimitedFameRVAdapter(regularFrameData)
        binding.shopMainRegularFrameRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.shopMainRegularFrameRv.adapter = regularFrameAdapter

        // 상시판매 칭호 - Normal

        regularFrameNormalData.apply {
            add(ShopMainUnlimitedFame(R.drawable.regular_frame_normal_1.toString(), "50P", 3))
            add(ShopMainUnlimitedFame(R.drawable.regular_frame_normal_2.toString(), "10000P", 4))
            add(ShopMainUnlimitedFame(R.drawable.regular_frame_normal_3.toString(), "500000P", 5))
        }

        val regularFrameNormalAdapter = ShopBannerUnlimitedFameRVAdapter(regularFrameNormalData)
        binding.shopMainRegularFrameNormalRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.shopMainRegularFrameNormalRv.adapter = regularFrameNormalAdapter


        // 상시판매 아이콘 - NEW
        regularNewIconData.apply {
            add(ShopMainUnlimitedIcon(R.drawable.regular_icon_new_1.toString(), "별행성", "10000P", 0))
            add(ShopMainUnlimitedIcon(R.drawable.regular_icon_new_2.toString(), "새턴", "10000P", 1))
            add(ShopMainUnlimitedIcon(R.drawable.regular_icon_new_3.toString(), "초승달", "10000P", 2))
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
                    "20000P",
                    3
                )
            )
            add(ShopMainUnlimitedIcon(R.drawable.regular_icon_normal_2.toString(), "하트", "20000P", 4))
            add(
                ShopMainUnlimitedIcon(
                    R.drawable.regular_icon_normal_3.toString(),
                    "시그니처 별",
                    "10000P",
                    5
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
        handler.postDelayed(slideRunnable, 3000) // 3초마다 배너 변경
    }

    private fun stopAutoSlide() {
        handler.removeCallbacks(slideRunnable)
    }

    override fun onResume() {
        super.onResume()
        startAutoSlide()
        updateLoginStatusView() // 로그인 상태에 따라 뷰 업데이트

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

    override fun onStop() {
        super.onStop()
        // 앱이 종료될 때 로그아웃 상태를 SharedPreferences에 저장
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", false)
            apply()
        }
    }

    // 한정 판매 목록 로드 메서드
    private fun fetchLimitedSales() {
        // RetrofitClient.shopService.getLimitedSales("Bearer YOUR_AUTH_TOKEN") 호출 구현 필요
        // 예시를 위한 가상 코드입니다. 실제로는 YOUR_AUTH_TOKEN을 적절한 토큰으로 대체해야 합니다.

        val accessToken = getAccessToken() // 액세스 토큰 가져오기
        val authorizationHeader = "Bearer $accessToken"


        val callLimitedSales = if (isLoggedIn()) {
            RetrofitClient.shopService.getLimitedSales(authorizationHeader)

        } else {
            RetrofitClient.shopService.getLimitedSales(null)
        }

        Log.d("ShopMainFragment", " use token : ${accessToken}")

        callLimitedSales.enqueue(object : Callback<SalesResponse> {
            override fun onResponse(
                call: Call<SalesResponse>,
                response: Response<SalesResponse>
            ) {
                if (response.isSuccessful) {
                    // 성공적으로 데이터를 받음. 여기서 UI 업데이트 로직을 구현합니다.
                    val limitedSalesResponse = response.body()

                    if (isLoggedIn()) {
                        if (limitedSalesResponse != null) {
                            limitedSalesResponse.userInfo?.let { userInfo ->
                                updateUserInfo(userInfo)
                            }
                        }
                    }

                    // 받아온 데이터를 로그로 출력
                    Log.d("ShopMainFragment", "Limited sales data received: $limitedSalesResponse")

                    // 받아온 데이터로 한정판매 칭호와 아이콘 리스트 업데이트
                    limitedSalesResponse?.let {
                        // 한정판매 칭호 리스트 업데이트
                        limitedNewFrameData.clear()
                        limitedUMCFrameData.clear()

                        Log.d("LimitedFameBeforeMain", limitedUMCFrameData.toString())

                        var newLimitedCategoryFound = false // "NEW!" 카테고리를 찾았는지 여부를 추적하는 변수
                        var umcLimitedCategoryFound = false // "NEW!" 카테고리를 찾았는지 여부를 추적하는 변수

                        it.titles.forEach { category ->
                            when (category.category) {
                                "NEW!" -> {
                                    newLimitedCategoryFound = true // "NEW!" 카테고리를 찾았음을 표시
                                    limitedNewFrameData.addAll(category.itemList.map { item ->
                                        ShopMainLimitedFame(item.imageUrl, item.point.toString(), item.id)
                                    })
                                }

                                "UMC" -> {
                                    umcLimitedCategoryFound = true
                                    limitedUMCFrameData.addAll(category.itemList.map { item ->
                                        ShopMainLimitedFame(item.imageUrl, item.point.toString(), item.id)
                                    })
                                    Log.d("LimitedFameAfterMain", limitedUMCFrameData.toString())
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
                                            item.point.toString(),
                                            item.id
                                        )
                                    })
                                }

                                "UMC" -> {
                                    umcLimitedIconCategoryFound = true
                                    limitedUMCIconData.addAll(category.itemList.map { item ->
                                        ShopMainLimitedIcon(
                                            item.imageUrl,
                                            item.name,
                                            item.point.toString(),
                                            item.id
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

            override fun onFailure(call: Call<SalesResponse>, t: Throwable) {
                // 네트워크 오류 등의 실패 처리
                Log.d("ShopMainFragment", "Network Error: ${t.message}")
            }
        })

        val callUnlimitedSales = if (isLoggedIn()) {
            RetrofitClient.shopService.getUnlimitedSales(authorizationHeader)
        } else {
            RetrofitClient.shopService.getUnlimitedSales(null)
        }

        callUnlimitedSales.enqueue(object : Callback<SalesResponse> {
            override fun onResponse(
                call: Call<SalesResponse>,
                response: Response<SalesResponse>
            ) {
                if (response.isSuccessful) {
                    // 성공적으로 데이터를 받음. 여기서 UI 업데이트 로직을 구현합니다.
                    val unlimitedSalesResponse = response.body()

                    // 받아온 데이터를 로그로 출력
                    Log.d(
                        "ShopMainFragment",
                        "Unlimited sales data received: $unlimitedSalesResponse"
                    )

                    if (isLoggedIn()) {
                        if (unlimitedSalesResponse != null) {
                            unlimitedSalesResponse.userInfo?.let { userInfo ->
                                updateUserInfo(userInfo)
                            }
                        }
                    }


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
                                            item.point.toString(),
                                            item.id
                                        )
                                    })
                                }

                                "Normal" -> {
                                    normalUnlimitedFrameCategoryFound = true
                                    regularFrameNormalData.addAll(category.itemList.map { item ->
                                        ShopMainUnlimitedFame(
                                            item.imageUrl,
                                            item.point.toString(),
                                            item.id
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
                                            item.point.toString(),
                                            item.id
                                        )
                                    })
                                }

                                "Normal" -> {
                                    normalUnlimitedIconCategoryFound = true
                                    regularNormalIconData.addAll(category.itemList.map { item ->
                                        ShopMainUnlimitedIcon(
                                            item.imageUrl,
                                            item.name,
                                            item.point.toString(),
                                            item.id
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

            override fun onFailure(call: Call<SalesResponse>, t: Throwable) {
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
                override fun onItemClick(itemId: Int) { // onItemClick의 파라미터를 Int로 변경
                    val fragment = ShopMainLimitedFameFragment().apply {
                        arguments = Bundle().apply {
                            putInt("ITEM_ID", itemId) // ITEM_ID 키로 아이템 ID 저장
                        }
                    }
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, fragment)
                        .addToBackStack(null) // 이전 Fragment로 돌아갈 수 있도록 BackStack에 추가
                        .commitAllowingStateLoss()
                }
            })


            val limitedUMCFrameDataAdapter = ShopBannerLimitedFameRVAdapter(limitedUMCFrameData)
            binding.shopMainLimitedFrameUMCRv.adapter = limitedUMCFrameDataAdapter
            Log.d("ShopMainUPDATE", limitedUMCFrameData.toString())

            limitedUMCFrameDataAdapter.setMyItemClickListener(object :
                ShopBannerLimitedFameRVAdapter.MyItemClickListener {
                override fun onItemClick(itemId: Int) { // onItemClick의 파라미터를 Int로 변경
                    val fragment = ShopMainLimitedFameFragment().apply {
                        arguments = Bundle().apply {
                            putInt("ITEM_ID", itemId) // ITEM_ID 키로 아이템 ID 저장
                        }
                    }
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, fragment)
                        .addToBackStack(null) // 이전 Fragment로 돌아갈 수 있도록 BackStack에 추가
                        .commitAllowingStateLoss()
                }

            })

            // 한정판매 아이콘 어댑터 업데이트
            val limitedNewIconAdapter = ShopBannerLimitedIconRVAdapter(limitedNewIconData)
            binding.shopMainLimitedIconNewRv.adapter = limitedNewIconAdapter

            limitedNewIconAdapter.setMyItemClickListener(object :
                ShopBannerLimitedIconRVAdapter.MyItemClickListener {
                override fun onItemClick(itemId: Int) {
                    val fragment = ShopMainLimitedIconFragment().apply {
                        arguments = Bundle().apply {
                            putInt("ITEM_ID", itemId) // ITEM_ID 키로 아이템 ID 저장
                        }
                    }
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, fragment)
                        .addToBackStack(null) // 이전 Fragment로 돌아갈 수 있도록 BackStack에 추가
                        .commitAllowingStateLoss()
                }
            })

            val limitedUMCIconAdapter = ShopBannerLimitedIconRVAdapter(limitedUMCIconData)
            binding.shopMainLimitedIconUMCRv.adapter = limitedUMCIconAdapter

            limitedUMCIconAdapter.setMyItemClickListener(object :
                ShopBannerLimitedIconRVAdapter.MyItemClickListener {
                override fun onItemClick(itemId: Int) {
                    val fragment = ShopMainLimitedIconFragment().apply {
                        arguments = Bundle().apply {
                            putInt("ITEM_ID", itemId) // ITEM_ID 키로 아이템 ID 저장
                        }
                    }
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, fragment)
                        .addToBackStack(null) // 이전 Fragment로 돌아갈 수 있도록 BackStack에 추가
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
                override fun onItemClick(itemId: Int) { // onItemClick의 파라미터를 Int로 변경
                    val fragment = ShopMainUnlimitedFameFragment().apply {
                        arguments = Bundle().apply {
                            putInt("ITEM_ID", itemId) // ITEM_ID 키로 아이템 ID 저장
                        }
                    }
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, fragment)
                        .addToBackStack(null) // 이전 Fragment로 돌아갈 수 있도록 BackStack에 추가
                        .commitAllowingStateLoss()
                }
            })

            val regularNormalFrameDataAdapter =
                ShopBannerUnlimitedFameRVAdapter(regularFrameNormalData)
            binding.shopMainRegularFrameNormalRv.adapter = regularNormalFrameDataAdapter
            regularNormalFrameDataAdapter.setMyItemClickListener(object :
                ShopBannerUnlimitedFameRVAdapter.MyItemClickListener {
                override fun onItemClick(itemId: Int) { // onItemClick의 파라미터를 Int로 변경
                    val fragment = ShopMainUnlimitedFameFragment().apply {
                        arguments = Bundle().apply {
                            putInt("ITEM_ID", itemId) // ITEM_ID 키로 아이템 ID 저장
                        }
                    }
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, fragment)
                        .addToBackStack(null) // 이전 Fragment로 돌아갈 수 있도록 BackStack에 추가
                        .commitAllowingStateLoss()
                }
            })


            // 상시판매 아이콘 어댑터 업데이트
            val regularNewIconAdapter = ShopBannerUnlimitedIconRVAdapter(regularNewIconData)
            binding.shopMainRegularIconNewRv.adapter = regularNewIconAdapter

            regularNewIconAdapter.setMyItemClickListener(object :
                ShopBannerUnlimitedIconRVAdapter.MyItemClickListener {
                override fun onItemClick(itemId: Int) {
                    val fragment = ShopMainUnlimitedIconFragment().apply {
                        arguments = Bundle().apply {
                            putInt("ITEM_ID", itemId) // ITEM_ID 키로 아이템 ID 저장
                        }
                    }
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, fragment)
                        .addToBackStack(null) // 이전 Fragment로 돌아갈 수 있도록 BackStack에 추가
                        .commitAllowingStateLoss()
                }
            })


            val regularNormalIconAdapter = ShopBannerUnlimitedIconRVAdapter(regularNormalIconData)
            binding.shopMainRegularIconNormalRv.adapter = regularNormalIconAdapter

            regularNormalIconAdapter.setMyItemClickListener(object :
                ShopBannerUnlimitedIconRVAdapter.MyItemClickListener {
                override fun onItemClick(itemId: Int) {
                    val fragment = ShopMainUnlimitedIconFragment().apply {
                        arguments = Bundle().apply {
                            putInt("ITEM_ID", itemId) // ITEM_ID 키로 아이템 ID 저장
                        }
                    }
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, fragment)
                        .addToBackStack(null) // 이전 Fragment로 돌아갈 수 있도록 BackStack에 추가
                        .commitAllowingStateLoss()
                }
            })


        } catch (e: Exception) {
            Log.e("ShopMainUpdateUI", "Error in update(): ${e.message}")
        }

    }

    private fun updateUserInfo(userInfo: UserInfo?) {
        if (userInfo != null) {
            // 사용자 정보 업데이트 로직
            // 예: 닉네임 표시
            Log.d("ShopMainFragment", "Updating user info: $userInfo")
            // 닉네임 업데이트
            binding.profileNicknameTv.text = userInfo.nickname
            binding.profileUserPointTv.text = userInfo.point.toString()

            Glide.with(binding.root.context)
                .load(userInfo.profileImageUrl)
                .apply(RequestOptions().circleCrop()) // RequestOptions를 사용하여 circleCrop 적용
                .placeholder(R.drawable.signup_default_profile_image) // 로딩 중에 표시할 플레이스홀더 이미지
                .error(R.drawable.signup_default_profile_image) // 로딩 실패 시 표시할 이미지
                .transition(DrawableTransitionOptions.withCrossFade()) // 크로스페이드 효과 적용
                .into(binding.shopMainMyProfileCiv) // 이미지를 표시할 ImageView
// Glide CircleCrop으로 표시

            val imageLoader = ImageLoader.Builder(binding.root.context)
                .componentRegistry {
                    add(SvgDecoder(binding.root.context)) // SVG 이미지 처리를 위해 SvgDecoder 추가
                }
                .build()

            val titleImageRequest = ImageRequest.Builder(binding.root.context)
                .crossfade(true)
                .crossfade(300)
                .data(userInfo.profileTitleUrl)
                .target (binding.shopMainProfileTagIv)
                .build()

            imageLoader.enqueue(titleImageRequest)

            val iconImageRequest = ImageRequest.Builder(binding.root.context)
                .crossfade(true)
                .crossfade(300)
                .data(userInfo.profileIconUrl)
                .target (binding.myIconIv)
                .build()

            imageLoader.enqueue(iconImageRequest)


            // ...
        } else {
            // 로그인 정보 없음 처리
            Log.d("ShopMainFragment", "로그인 정보가 없습니다.")
        }
        // 사용자 프로필 UI 업데이트
        // 예를 들어, 사용자 닉네임, 포인트 등을 UI에 반영하는 로직을 여기에 구현합니다.
    }
}
