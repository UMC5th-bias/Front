package com.example.favoriteplace

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.favoriteplace.LoginActivity.Companion.ACCESS_TOKEN_KEY
import com.example.favoriteplace.databinding.FragmentShopMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopMainActivity : AppCompatActivity() {
    lateinit var binding: FragmentShopMainBinding

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentShopMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기화 작업
        setupBannerViewPager()
        fetchLimitedSales()
        updateLoginStatusView()

        slideRunnable = Runnable {
            binding.shopMainBannerVp2.currentItem = (binding.shopMainBannerVp2.currentItem + 1) % imageResIds.size
            handler.postDelayed(slideRunnable, 3000)
        }

        // 로그인 버튼 클릭 시
        binding.shopMainLoginButtonBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            if (!isLoggedIn()) startActivity(intent)
        }

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
                            this,
                            R.color.black
                        )
                    )
                    binding.shopMainSwitchRegularTv.setTextColor(
                        ContextCompat.getColor(
                            this,
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
                            this,
                            R.color.white
                        )
                    )
                    binding.shopMainSwitchRegularTv.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.black
                        )
                    )
                }
            }, 100) // 100ms 지연
        }
    }

    private fun setupBannerViewPager() {
        // BannerItem 리스트 생성
        val items = listOf(
            BannerItem(R.drawable.shop_banner1, false), // "보러가기" 버튼 없음
            BannerItem(R.drawable.shop_banner2, true)   // "보러가기" 버튼 포함
        )
        // 어댑터에 Activity와 items 리스트 전달
        val adapter = ShopBannerVPAdapter(this, items)
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

    private fun updateLoginStatusView() {
        if (isLoggedIn()) {
            binding.shopMainYesLoginCl.visibility = View.VISIBLE
            binding.shopMainNotLoginCl.visibility = View.GONE
        } else {
            binding.shopMainYesLoginCl.visibility = View.GONE
            binding.shopMainNotLoginCl.visibility = View.VISIBLE
        }
    }

    // sharePreferences에 저장된 액세스 토큰 반환하는 메소드
    private fun getAccessToken(): String? {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    // 사용자의 로그인 상태를 확인하는 메소드
    private fun isLoggedIn(): Boolean {
        return getAccessToken() != null
    }

    override fun onStop() {
        super.onStop()
        // 앱이 종료될 때 로그아웃 상태를 SharedPreferences에 저장
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", false)
            apply()
        }
    }

    // 한정 판매 목록 로드 메서드
    private fun fetchLimitedSales() {
        val accessToken = getAccessToken() // 액세스 토큰 가져오기
        val authorizationHeader = "Bearer $accessToken"

        val callLimitedSales = if (isLoggedIn()) {
            RetrofitClient.shopService.getLimitedSales(authorizationHeader)
        } else {
            RetrofitClient.shopService.getLimitedSales(null)
        }

        Log.d("ShopMainActivity", " use token : $accessToken")

        callLimitedSales.enqueue(object : Callback<SalesResponse> {
            override fun onResponse(
                call: Call<SalesResponse>,
                response: Response<SalesResponse>
            ) {
                if (response.isSuccessful) {
                    val limitedSalesResponse = response.body()

                    if (isLoggedIn()) {
                        limitedSalesResponse?.userInfo?.let { userInfo ->
                            updateUserInfo(userInfo)
                        }
                    }

                    Log.d("ShopMainActivity", "Limited sales data received: $limitedSalesResponse")

                    limitedSalesResponse?.let {
                        limitedNewFrameData.clear()
                        limitedUMCFrameData.clear()

                        var newLimitedCategoryFound = false
                        var umcLimitedCategoryFound = false

                        it.titles.forEach { category ->
                            when (category.category) {
                                "NEW!" -> {
                                    newLimitedCategoryFound = true
                                    limitedNewFrameData.addAll(category.itemList.map { item ->
                                        ShopMainLimitedFame(item.imageUrl, item.point.toString(), item.id)
                                    })
                                }
                                "UMC" -> {
                                    umcLimitedCategoryFound = true
                                    limitedUMCFrameData.addAll(category.itemList.map { item ->
                                        ShopMainLimitedFame(item.imageUrl, item.point.toString(), item.id)
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

                        var newLimitedIconCategoryFound = false
                        var umcLimitedIconCategoryFound = false

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

                        updateLimitedSalesUI()
                    }
                } else {
                    Log.d("ShopMainActivity", "API Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<SalesResponse>, t: Throwable) {
                Log.d("ShopMainActivity", "Network Error: ${t.message}")
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
                    val unlimitedSalesResponse = response.body()

                    Log.d("ShopMainActivity", "Unlimited sales data received: $unlimitedSalesResponse")

                    if (isLoggedIn()) {
                        unlimitedSalesResponse?.userInfo?.let { userInfo ->
                            updateUserInfo(userInfo)
                        }
                    }

                    unlimitedSalesResponse?.let {
                        regularFrameData.clear()
                        regularFrameNormalData.clear()
                        var newUnlimitedFrameCategoryFound = false
                        var normalUnlimitedFrameCategoryFound = false

                        it.titles.forEach { category ->
                            when (category.category) {
                                "New!" -> {
                                    newUnlimitedFrameCategoryFound = true
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

                        var newUnlimitedIconCategoryFound = false
                        var normalUnlimitedIconCategoryFound = false

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

                        updateUnlimitedSalesUI()
                    }
                } else {
                    Log.d("ShopMainActivity", "API Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<SalesResponse>, t: Throwable) {
                Log.d("ShopMainActivity", "Network Error: ${t.message}")
            }
        })
    }

    private fun updateLimitedSalesUI() {
        try {
            val limitedNewFrameDataAdapter = ShopBannerLimitedFameRVAdapter(limitedNewFrameData)
            binding.shopMainLimitedFrameRv.adapter = limitedNewFrameDataAdapter

            limitedNewFrameDataAdapter.setMyItemClickListener(object :
                ShopBannerLimitedFameRVAdapter.MyItemClickListener {
                override fun onItemClick(itemId: Int) {
                    val fragment = ShopMainLimitedFameFragment().apply {
                        arguments = Bundle().apply {
                            putInt("ITEM_ID", itemId)
                        }
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss()
                }
            })

            val limitedUMCFrameDataAdapter = ShopBannerLimitedFameRVAdapter(limitedUMCFrameData)
            binding.shopMainLimitedFrameUMCRv.adapter = limitedUMCFrameDataAdapter
            Log.d("ShopMainUPDATE", limitedUMCFrameData.toString())

            limitedUMCFrameDataAdapter.setMyItemClickListener(object :
                ShopBannerLimitedFameRVAdapter.MyItemClickListener {
                override fun onItemClick(itemId: Int) {
                    val fragment = ShopMainLimitedFameFragment().apply {
                        arguments = Bundle().apply {
                            putInt("ITEM_ID", itemId)
                        }
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss()
                }
            })

            val limitedNewIconAdapter = ShopBannerLimitedIconRVAdapter(limitedNewIconData)
            binding.shopMainLimitedIconNewRv.adapter = limitedNewIconAdapter

            limitedNewIconAdapter.setMyItemClickListener(object :
                ShopBannerLimitedIconRVAdapter.MyItemClickListener {
                override fun onItemClick(itemId: Int) {
                    val fragment = ShopMainLimitedIconFragment().apply {
                        arguments = Bundle().apply {
                            putInt("ITEM_ID", itemId)
                        }
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, fragment)
                        .addToBackStack(null)
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
                            putInt("ITEM_ID", itemId)
                        }
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss()
                }
            })

        } catch (e: Exception) {
            Log.e("ShopMainUpdateUI", "Error in update(): ${e.message}")
        }
    }

    private fun updateUnlimitedSalesUI() {
        try {
            val regularNewFrameDataAdapter = ShopBannerUnlimitedFameRVAdapter(regularFrameData)
            binding.shopMainRegularFrameRv.adapter = regularNewFrameDataAdapter

            regularNewFrameDataAdapter.setMyItemClickListener(object :
                ShopBannerUnlimitedFameRVAdapter.MyItemClickListener {
                override fun onItemClick(itemId: Int) {
                    val fragment = ShopMainUnlimitedFameFragment().apply {
                        arguments = Bundle().apply {
                            putInt("ITEM_ID", itemId)
                        }
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss()
                }
            })

            val regularNormalFrameDataAdapter =
                ShopBannerUnlimitedFameRVAdapter(regularFrameNormalData)
            binding.shopMainRegularFrameNormalRv.adapter = regularNormalFrameDataAdapter
            regularNormalFrameDataAdapter.setMyItemClickListener(object :
                ShopBannerUnlimitedFameRVAdapter.MyItemClickListener {
                override fun onItemClick(itemId: Int) {
                    val fragment = ShopMainUnlimitedFameFragment().apply {
                        arguments = Bundle().apply {
                            putInt("ITEM_ID", itemId)
                        }
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss()
                }
            })

            val regularNewIconAdapter = ShopBannerUnlimitedIconRVAdapter(regularNewIconData)
            binding.shopMainRegularIconNewRv.adapter = regularNewIconAdapter

            regularNewIconAdapter.setMyItemClickListener(object :
                ShopBannerUnlimitedIconRVAdapter.MyItemClickListener {
                override fun onItemClick(itemId: Int) {
                    val fragment = ShopMainUnlimitedIconFragment().apply {
                        arguments = Bundle().apply {
                            putInt("ITEM_ID", itemId)
                        }
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, fragment)
                        .addToBackStack(null)
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
                            putInt("ITEM_ID", itemId)
                        }
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frameLayout, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss()
                }
            })

        } catch (e: Exception) {
            Log.e("ShopMainUpdateUI", "Error in update(): ${e.message}")
        }
    }

    private fun updateUserInfo(userInfo: UserInfo?) {
        userInfo?.let {
            Log.d("ShopMainActivity", "Updating user info: $userInfo")
            binding.profileNicknameTv.text = userInfo.nickname
            binding.profileUserPointTv.text = userInfo.point.toString()

            Glide.with(this)
                .load(userInfo.profileImageUrl)
                .apply(RequestOptions().circleCrop())
                .placeholder(R.drawable.signup_default_profile_image)
                .error(R.drawable.signup_default_profile_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.shopMainMyProfileCiv)

            // SVG 이미지를 로드하는 예시
            val imageLoader = ImageLoader.Builder(this)
                .componentRegistry {
                    add(SvgDecoder(this@ShopMainActivity))
                }
                .build()

            val titleImageRequest = ImageRequest.Builder(this)
                .crossfade(true)
                .crossfade(300)
                .data(userInfo.profileTitleUrl)
                .target(binding.shopMainProfileTagIv)
                .build()

            imageLoader.enqueue(titleImageRequest)

            val iconImageRequest = ImageRequest.Builder(this)
                .crossfade(true)
                .crossfade(300)
                .data(userInfo.profileIconUrl)
                .target(binding.myIconIv)
                .build()

            imageLoader.enqueue(iconImageRequest)
        } ?: run {
            Log.d("ShopMainActivity", "로그인 정보가 없습니다.")
        }
    }
}
