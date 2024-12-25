package com.example.favoriteplace


import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.example.favoriteplace.LoginActivity.Companion.ACCESS_TOKEN_KEY
import com.example.favoriteplace.databinding.FragmentHomeBinding
import com.example.favoriteplace.databinding.FragmentShopBannerNewBinding
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    lateinit var retrofit: Retrofit
    private lateinit var homeService: HomeService
    private lateinit var trendingPostsAdapter: TrendingPostsAdapter // Adapter 선언


    private var trendingPostsData: MutableList<HomeService.TrendingPosts> = mutableListOf()
    private var isLoggedIn = false // 로그인 상태를 나타내는 변수
    private var accessToken: String? = null // 액세스 토큰을 저장할 변수

    private var nonMemberData: HomeService.NonMemberData? = null // 비회원 데이터 변수 추가

    companion object {
        const val LOGIN_REQUEST_CODE = 101
        const val ACCESS_TOKEN_KEY = "token" // SharedPreferences 키 상수
        private const val TAG = "HomeFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

//
//        // SharedPreferences에서 토큰 삭제
//        // TODO : Test를 위해 토큰 지움
////        clearAccessToken()
//
//        // 신상품 페이지 이동
//        binding.homeNewItemMoreBtn.setOnClickListener {
//
//            val shopBannerNewFragment = ShopBannerNewFragment() // newItemFragment 인스턴스 생성
//            val transaction = parentFragmentManager.beginTransaction()
//            transaction.replace(R.id.main_frameLayout, shopBannerNewFragment)
//            transaction.addToBackStack(null)
//            transaction.commit()
//
//            // 바텀 네비게이션 바에서 상점 아이템을 선택된 상태로 설정
//            (requireActivity() as MainActivity).setSelectedNavItem(R.id.shopFragment)
//        }
//
//
//        // 추천 랠리 이동
//        binding.homeRecommendMoreBtn.setOnClickListener {
//            (requireActivity() as MainActivity).setRecommendRally(R.id.rallyhomeFragment)
//
//        }
//
//
//
//        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // View Binding 해제
    }

    private fun setupRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl("http://favoriteplace.store:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        homeService = retrofit.create(HomeService::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupRetrofit()
        setupViewPager()

        //로그인 버튼
        setupClickListeners()

        // 앱이 처음 시작될 때 로그인 상태를 확인 후, 로그인 정보가 없으면 서버에 요청을 보냄
        checkLoginStatus()

        // 알림 상태 처리
        // TODO : 로그인 시 서버에서 사용자 알림 존재 여부 가져오기
        updateNotificationIcon(hasNotification = false) // 기본값으로 알림 없음 설정

    }

    private fun setupViewPager() {
        val bannerAdapter = BannerVPAdapter(this)
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_banner1))
        bannerAdapter.addFragment(BannerFragment(R.drawable.demo))
    }

    private fun setupClickListeners() {
        binding.homeNewItemMoreBtn.setOnClickListener {
            navigateToFragment(ShopBannerNewFragment(), R.id.shopFragment)
        }

        binding.homeRecommendMoreBtn.setOnClickListener {
            (requireActivity() as MainActivity).setRecommendRally(R.id.rallyhomeFragment)
        }

        binding.homeLoginBtn.setOnClickListener {
            startActivityForResult(
                Intent(requireActivity(), LoginActivity::class.java),
                LOGIN_REQUEST_CODE
            )
        }
    }

    private fun navigateToFragment(fragment: Fragment, navItemId: Int) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_frameLayout, fragment)
            .addToBackStack(null)
            .commit()
        (requireActivity() as MainActivity).setSelectedNavItem(navItemId)
    }



    private fun checkLoginStatus() {
        // SharedPreferences에서 액세스 토큰 가져오기
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        accessToken = sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
        // TODO : 로그인 상태 다시 돌려놓기
        isLoggedIn = !accessToken.isNullOrEmpty()
//        isLoggedIn = false;


        if (isLoggedIn) {
            // 로그인 상태인 경우 사용자 정보를 가져옴
            getUserInfo(accessToken!!)
            Log.d("HomeFragment", ">> 로그인 상태 : $isLoggedIn, \n $accessToken")

        } else {
            // 비회원 상태인 경우
            Log.d("HomeFragment", ">> 비회원 상태입니다., $isLoggedIn")
            fetchNonMember()

        }
    }

    private fun setupTrendingPostsRecyclerView() {

        // TrendingPostsAdapter 초기화
        trendingPostsAdapter = TrendingPostsAdapter(trendingPostsData, object :
            TrendingPostsAdapter.TrendingPostClickListener {
            override fun onTrendingPostClicked(post: HomeService.TrendingPosts) {
                val intent = when (post.board) {
                    "자유게시판" -> Intent(context, PostDetailActivity::class.java).apply {
                        putExtra("POST_ID", post.id) // "자유게시판"의 경우 "POST_ID" 사용
                    }
                    "성지순례 인증" -> Intent(context, MyGuestBookActivity::class.java).apply {
                        putExtra("GUESTBOOK_ID", post.id) // "성지순례 인증"의 경우 "GUESTBOOK_ID" 사용
                    }
                    else -> return
                }
                startActivity(intent)
            }
        })
        binding.trendingPostsRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.trendingPostsRecyclerView.adapter = trendingPostsAdapter

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
            val isLoggedIn = data?.getBooleanExtra("isLoggedIn", false) ?: false
            val userToken = data?.getStringExtra("token")
            if (!userToken.isNullOrEmpty() && isLoggedIn) {
                this.isLoggedIn = true
                Log.d("HomeFragment", ">> Home Login userToken : $userToken")
                Log.d("HomeFragment", ">> Home Login isLoggedIn : $isLoggedIn")
                // Retrofit 요청 -> 사용자 정보 가져옴
                getUserInfo(userToken)
            }
        }
    }

    private fun getUserInfo(userToken: String) {
        lifecycleScope.launch {
            try {
                val response = homeService.getUserInfo("Bearer $userToken")
                if (response.isSuccessful) {
                    updateUI(response.body())
                } else {
                    Log.e(TAG, "Failed to get user info: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching user info: ${e.message}", e)
            }
        }
    }


    private fun updateUI(homeData: HomeService.LoginResponse?) {
        if (homeData == null) {
            binding.userLayout.visibility = View.GONE
            binding.unUserLayout.visibility = View.VISIBLE
            return
        }

        binding.userLayout.visibility = View.VISIBLE
        binding.unUserLayout.visibility = View.GONE

        homeData.userInfo?.let { userInfo ->
            loadImage(userInfo.profileImageUrl, binding.homeMemberProfileCiv)
            loadImage(userInfo.profileIconUrl, binding.homeMemberIconIv)
            binding.homeMemberIconIv.visibility =
                if (userInfo.profileIconUrl != null) View.VISIBLE else View.GONE
            binding.homeMemberNameTv.text = userInfo.nickname
            loadImage(userInfo.profileTitleUrl, binding.homeMemberBadgeIv)
        }

        homeData.rally?.let { rally ->
            binding.homeRallyingTv.text = rally.name
            binding.rallyLocationdetailTotalTv.text = rally.pilgrimageNumber.toString()
            binding.rallyLocationdetailCheckTv.text = rally.completeNumber.toString()
            loadImage(rally.backgroundImageUrl, binding.homeRallyIv)
        }

        setupTrendingPostsRecyclerView()
        homeData.trendingPosts?.let { trendingPostsAdapter.submitList(it) }
    }

    // 비회원
    private fun fetchNonMember() {
        lifecycleScope.launch {
            try {
                val response = homeService.getNonMemberInfo()
                if (response.isSuccessful) {
                    response.body()?.let { nonMemberData ->
                        setupTrendingPostsRecyclerView()
                        nonMemberData.trendingPosts?.let { trendingPostsAdapter.submitList(it) }
                        loadImage(nonMemberData.rally.backgroundImageUrl, binding.homeRecommendIv)
                    }
                } else {
                    Log.e(TAG, "Failed to retrieve non-member data: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching non-member data: ${e.message}", e)
            }
        }
    }

    private fun updateNotificationIcon(hasNotification: Boolean) {
        // 알림 아이콘을 위한 View ID 가져오기
        val notificationIconDefault = binding.notificationIv
        val notificationIconWithBadge = binding.notificationWithBadgeIv

        if (hasNotification) {
            notificationIconDefault.visibility = View.GONE // 기본 아이콘 숨기기
            notificationIconWithBadge.visibility = View.VISIBLE // 배지 아이콘 표시
        } else {
            notificationIconDefault.visibility = View.VISIBLE // 기본 아이콘 표시
            notificationIconWithBadge.visibility = View.GONE // 배지 아이콘 숨기기
        }
    }

    private fun loadImage(url: String?, imageView: ImageView) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.signup_default_profile_image)
            .error(R.drawable.signup_default_profile_image)
            .into(imageView)
    }

    //svg 이미지를 가져오기 위한 함수
    fun bind(context: Context, iconImageUrl: String?, imageView: ImageView) {
        try {
            // iconImageUrl이 null이 아닌 경우에는 해당 이미지를 로드하여 설정
            iconImageUrl?.let {
                val imageLoader = ImageLoader.Builder(context)
                    .componentRegistry {
                        add(SvgDecoder(context)) // SVG 이미지 처리를 위해 SvgDecoder 추가
                    }
                    .build()

                val imageRequest = ImageRequest.Builder(context)
                    .data(it)
                    .target(imageView)  // 해당 이미지뷰를 타겟으로 svg 삽입
                    .build()
                imageLoader.enqueue(imageRequest)
            } ?: run {
                // iconImageUrl이 null인 경우에는 기본 이미지를 설정
            }

        } catch (e: Exception) {
            Log.e("ShopBannerDetailFragment", "Error loading image: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun clearAccessToken() {
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(ACCESS_TOKEN_KEY)  // 토큰 삭제
        editor.apply()  // 변경 사항을 적용
    }
}




