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
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
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
        updateTabUI(isLimitedSale = true) // 초기 탭 설정

        // 스위치 초기 상태에 따라 레이아웃 설정
        updateSaleLayout(binding.shopMainSwitchOnOffSc.isChecked)

        // RecyclerView에 LayoutManager 설정
        binding.shopMainLimitedFrameRv.layoutManager = LinearLayoutManager(requireContext())
        binding.shopMainRegularFrameRv.layoutManager = LinearLayoutManager(requireContext())
        binding.shopMainLimitedFrameUMCRv.layoutManager = LinearLayoutManager(requireContext())
        binding.shopMainRegularFrameNormalRv.layoutManager = LinearLayoutManager(requireContext())

        // 사용자 정보 로드 및 프로필 UI 업데이트
        fetchSalesData()

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
            updateSaleLayout(isChecked)
            updateTabUI(isLimitedSale = !isChecked)
        }

        // 로그인 버튼 클릭 리스너 추가
        val loginButton = binding.root.findViewById<View>(R.id.shopMain_loginButton_btn)
        loginButton.setOnClickListener {
            if (!isLoggedIn()) {
                // 비회원 상태일 경우 로그인 화면으로 이동
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
            }
        }

        return binding.root
    }

    // 스위치 상태에 따라 한정판매/상시판매 레이아웃을 업데이트하는 메서드
    private fun updateSaleLayout(isChecked: Boolean) {
        if (!isChecked) {
            binding.limitedSaleFrameContainerCl.visibility = View.VISIBLE
            binding.regularSaleFrameContainerCl.visibility = View.GONE
            binding.limitedSaleIconContainerCl.visibility = View.VISIBLE
            binding.regularSaleIconContainerCl.visibility = View.GONE
        } else {
            binding.limitedSaleFrameContainerCl.visibility = View.GONE
            binding.regularSaleFrameContainerCl.visibility = View.VISIBLE
            binding.limitedSaleIconContainerCl.visibility = View.GONE
            binding.regularSaleIconContainerCl.visibility = View.VISIBLE
        }
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
        handler.removeCallbacks(slideRunnable)
        handler.postDelayed(slideRunnable, 3000)
    }

    private fun stopAutoSlide() {
        handler.removeCallbacks(slideRunnable)
    }

    override fun onResume() {
        super.onResume()

        if (isLoggedIn()) {
            fetchSalesData()
        } else {
            // include로 가져온 레이아웃도 다시 업데이트
            val memberProfileLayout = binding.root.findViewById<View>(R.id.shopMain_memberProfile_cl)
            val notLoginLayout = binding.root.findViewById<View>(R.id.shopMain_notLogin_cl)
            updateLoginStatusView(memberProfileLayout, notLoginLayout)
        }
        startAutoSlide()
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

                val authorizationHeader = if (accessToken != null) {
                    "Bearer $accessToken"
                } else {
                    null
                }
                // API 호출 로그
                Log.d("ShopMainFragment", "Fetching sales data with access token: $accessToken")


                val limitedSalesResponse = RetrofitClient.shopService.getLimitedSales(authorizationHeader).awaitResponse()
                val unlimitedSalesResponse = RetrofitClient.shopService.getUnlimitedSales(authorizationHeader).awaitResponse()

                if (limitedSalesResponse.isSuccessful && unlimitedSalesResponse.isSuccessful) {
                    Log.d("ShopMainFragment", "Limited sales API success: ${limitedSalesResponse.body()}")
                    Log.d("ShopMainFragment", "Unlimited sales API success: ${unlimitedSalesResponse.body()}")

                    // 데이터 초기화하는 코드
                    clearSalesData()

                    // 로그인 상태에 따른 UI 업데이트를 호출
                    limitedSalesResponse.body()?.let {
                        updateMemberProfile(it)  // 로그인 상태에 따른 프로필 정보 업데이트
                        updateLimitedSalesUI(it)
                    }
                    unlimitedSalesResponse.body()?.let {
                        updateMemberProfile(it)  // 로그인 상태에 따른 프로필 정보 업데이트
                        updateUnlimitedSalesUI(it)
                    }
                } else {
                    Log.d("ShopMainFragment", "API Error: ${limitedSalesResponse.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.d("ShopMainFragment", "Network Error: ${e.message}")
            }
        }
    }

    // 기존 리스트 데이터 초기화하는 메서드
    private fun clearSalesData() {
        limitedNewFrameData.clear()
        limitedNewIconData.clear()
        limitedUMCFrameData.clear()
        limitedUMCIconData.clear()
        regularFrameData.clear()
        regularNormalIconData.clear()
        regularFrameNormalData.clear()
        regularNewIconData.clear()
    }

    // memberProfile UI를 업데이트하는 메서드
    private fun updateMemberProfile(response: SalesResponse) {
        lifecycleScope.launch(Dispatchers.Main) {
            val userInfo = response.userInfo

            // include로 가져온 뷰들에 대해 findViewById로 뷰를 참조
            val memberProfileLayout: View = binding.root.findViewById(R.id.shopMain_memberProfile_cl)
            val notLoginLayout: View = binding.root.findViewById(R.id.shopMain_notLogin_cl)
            val profileImageView: ImageView = memberProfileLayout.findViewById(R.id.shopMain_myProfile_civ)
            val profileTitleImageView: ImageView = memberProfileLayout.findViewById(R.id.shopMain_profileTag_iv)
            val nicknameTextView: TextView = memberProfileLayout.findViewById(R.id.profile_nickname_tv)
            val pointTextView: TextView = memberProfileLayout.findViewById(R.id.profile_userPoint_tv)


            if (userInfo != null) {
                // 로그인 상태일 때 프로필 정보 보여주기
                memberProfileLayout.visibility = View.VISIBLE
                notLoginLayout.visibility = View.GONE

                // 닉네임 설정
                nicknameTextView.text = userInfo.nickname
                // 포인트 설정
                pointTextView.text = userInfo.point.toString()

                // 프로필 이미지 설정
                if (!userInfo.profileImageUrl.isNullOrEmpty()) {
                    Glide.with(this@ShopMainFragment)
                        .load(userInfo.profileImageUrl)
                        .placeholder(R.drawable.memberimg) // 기본 이미지
                        .into(profileImageView)
                }

                // 칭호 이미지 설정
                if (!userInfo.profileTitleUrl.isNullOrEmpty()) {
                    Glide.with(this@ShopMainFragment)
                        .load(userInfo.profileTitleUrl)
                        .into(profileTitleImageView)
                }
            }  else {
                // 비회원 상태일 경우 로그인 버튼 및 비회원 레이아웃을 보이게 함
                memberProfileLayout.visibility = View.GONE
                notLoginLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun updateLimitedSalesUI(response: SalesResponse) {
        lifecycleScope.launch(Dispatchers.Main) {
            response.titles?.forEach { category ->
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
            } ?: Log.d("ShopMainFragment", "Titles list is null")

            // 한정판매 어댑터 설정
            val limitedFameAdapter = ShopBannerLimitedFameRVAdapter(limitedNewFrameData)
            val limitedUMCAdapter = ShopBannerLimitedFameRVAdapter(limitedUMCFrameData)

            // 아이템 클릭 리스너 설정
            limitedFameAdapter.setMyItemClickListener(object : ShopBannerLimitedFameRVAdapter.MyItemClickListener {
                override fun onItemClick(itemId: Int) {
                    Log.d("ShopMainFragment", "Clicked item ID (limited fame): $itemId")
                }
            })

            limitedUMCAdapter.setMyItemClickListener(object : ShopBannerLimitedFameRVAdapter.MyItemClickListener {
                override fun onItemClick(itemId: Int) {
                    Log.d("ShopMainFragment", "Clicked item ID (UMC fame): $itemId")
                }
            })

            binding.shopMainLimitedFrameRv.adapter = limitedFameAdapter
            binding.shopMainLimitedFrameUMCRv.adapter = limitedUMCAdapter

            // 아이콘 데이터 업데이트
            response.icons?.forEach { category ->
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
            }?: Log.d("ShopMainFragment", "Icons list is null")

            // 아이콘 어댑터 설정 및 클릭 리스너
            val limitedNewIconAdapter = ShopBannerLimitedIconRVAdapter(limitedNewIconData)
            val limitedUMCIconAdapter = ShopBannerLimitedIconRVAdapter(limitedUMCIconData)

            limitedNewIconAdapter.setMyItemClickListener(object : ShopBannerLimitedIconRVAdapter.MyItemClickListener {
                override fun onItemClick(itemId: Int) {
                    Log.d("ShopMainFragment", "Clicked item ID (limited new icon): $itemId")
                }
            })

            limitedUMCIconAdapter.setMyItemClickListener(object : ShopBannerLimitedIconRVAdapter.MyItemClickListener {
                override fun onItemClick(itemId: Int) {
                    Log.d("ShopMainFragment", "Clicked item ID (limited UMC icon): $itemId")
                }
            })

            binding.shopMainLimitedIconNewRv.adapter = limitedNewIconAdapter
            binding.shopMainLimitedIconUMCRv.adapter = limitedUMCIconAdapter
        }
    }

    private fun updateUnlimitedSalesUI(response: SalesResponse) {
        lifecycleScope.launch(Dispatchers.Main) {

            response.titles?.forEach { category ->
                when (category.category) {
                    "New!" -> {
                        regularFrameData.addAll(category.itemList.map {
                            ShopMainUnlimitedFame(it.imageUrl, it.point.toString(), it.id)
                        })
                    }
                    "Normal" -> {
                        regularFrameNormalData.addAll(category.itemList.map {
                            Log.d("ShopMainFragment", "normal item id : ${it.id} ")
                            ShopMainUnlimitedFame(it.imageUrl, it.point.toString(), it.id)
                        })
                        Log.d("ShopMainFragment", "normal items : ${regularFrameNormalData} ")
                    }
                }
            } ?: Log.d("ShopMainFragment", "Titles list is null")

            // 상시판매 어댑터 설정
            val regularFameAdapter = ShopBannerUnlimitedFameRVAdapter(regularFrameData)
            val regularNormalFameAdapter = ShopBannerUnlimitedFameRVAdapter(regularFrameNormalData)

            regularFameAdapter.setMyItemClickListener(object : ShopBannerUnlimitedFameRVAdapter.MyItemClickListener {
                override fun onItemClick(itemId: Int) {
                    Log.d("ShopMainFragment", "Clicked item ID (regular fame): $itemId")
                }
            })

            regularNormalFameAdapter.setMyItemClickListener(object : ShopBannerUnlimitedFameRVAdapter.MyItemClickListener {
                override fun onItemClick(itemId: Int) {
                    Log.d("ShopMainFragment", "Clicked item ID (regular normal fame): $itemId")
                }
            })

            binding.shopMainRegularFrameRv.adapter = regularFameAdapter
            binding.shopMainRegularFrameNormalRv.adapter = regularNormalFameAdapter

            // 아이콘 데이터 업데이트
            response.icons?.forEach { category ->
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
            }?: Log.d("ShopMainFragment", "Icons list is null")

            val regularNewIconAdapter = ShopBannerUnlimitedIconRVAdapter(regularNewIconData)
            val regularNormalIconAdapter = ShopBannerUnlimitedIconRVAdapter(regularNormalIconData)

            regularNewIconAdapter.setMyItemClickListener(object : ShopBannerUnlimitedIconRVAdapter.MyItemClickListener {
                override fun onItemClick(itemId: Int) {
                    Log.d("ShopMainFragment", "Clicked item ID (regular new icon): $itemId")
                }
            })

            regularNormalIconAdapter.setMyItemClickListener(object : ShopBannerUnlimitedIconRVAdapter.MyItemClickListener {
                override fun onItemClick(itemId: Int) {
                    Log.d("ShopMainFragment", "Clicked item ID (regular normal icon): $itemId")
                }
            })

            binding.shopMainRegularIconNewRv.adapter = regularNewIconAdapter
            binding.shopMainRegularIconNormalRv.adapter = regularNormalIconAdapter
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
