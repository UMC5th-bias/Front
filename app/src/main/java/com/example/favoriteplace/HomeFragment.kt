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
    lateinit var binding: FragmentHomeBinding
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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        // 신상품 페이지 이동
        binding.homeNewItemMoreBtn.setOnClickListener {

            val shopBannerNewFragment = ShopBannerNewFragment() // newItemFragment 인스턴스 생성
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frameLayout, shopBannerNewFragment)
            transaction.addToBackStack(null)
            transaction.commit()

            // 바텀 네비게이션 바에서 상점 아이템을 선택된 상태로 설정
            (requireActivity() as MainActivity).setSelectedNavItem(R.id.shopFragment)
        }


        // 추천 랠리 이동
        binding.homeRecommendMoreBtn.setOnClickListener {
            (requireActivity() as MainActivity).setRecommendRally(R.id.rallyhomeFragment)

        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Retrofit 객체 생성
        retrofit = Retrofit.Builder()
            .baseUrl("http://favoriteplace.store:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        homeService = retrofit.create(HomeService::class.java)


        val bannerAdapter = BannerVPAdapter(this)
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_banner1))
        bannerAdapter.addFragment(BannerFragment(R.drawable.demo))


        //로그인 버튼
        binding.homeLoginBtn.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            try {
                startActivityForResult(intent, LOGIN_REQUEST_CODE)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // 앱이 처음 시작될 때 로그인 상태를 확인 후, 로그인 정보가 없으면 서버에 요청을 보냄
        checkLoginStatus()

    }


    private fun checkLoginStatus() {
        // SharedPreferences에서 액세스 토큰 가져오기
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        accessToken = sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
        isLoggedIn = !accessToken.isNullOrEmpty()



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
                val response: Response<HomeService.LoginResponse> = homeService.getUserInfo("Bearer $userToken")
                if (response.isSuccessful) {
                    // 로그인 상태인 경우
                    // 서버로부터 사용자 정보를 성공적으로 받아왔을 때 UI 업데이트
                    val loginResponse: HomeService.LoginResponse? = response.body()
                    if (loginResponse != null) {
                        updateUI(loginResponse)
                        Log.d("HomeFragment", "$loginResponse")
                        Log.d("HomeFragment", ">> Home Login Success")

                    }
                } else {
                    // 로그인 상태가 아닌 경우
                    Log.e("HomeFragment", "Failed to get home data: ${response.code()}")
                }
            } catch (e: Exception) {
                // 오류
                Log.e("HomeFragment", "Error fetching user info: ${e.message}", e)
            }
        }

    }


    private fun updateUI(homeData: HomeService.LoginResponse?) {

        Log.d("HomeFragment", ">> $homeData")

        if (homeData != null) {
            binding.userLayout.visibility = View.VISIBLE
            binding.unUserLayout.visibility = View.GONE

            binding.nonMembersLayout.visibility = View.GONE
            binding.membersRallyLayout.visibility = View.VISIBLE

            // 사용자 정보가 제대로 반환되었을 때만 UI 업데이트
            homeData.userInfo?.let { userInfo ->
                // 사용자 이미지
                Glide.with(this)
                    .load(userInfo.profileImageUrl.toString()) // 서버에 저장된 이미지 URI
                    .placeholder(R.drawable.signup_default_profile_image) // 이미지를 불러오는 동안 보여줄 임시 이미지
                    .error(R.drawable.signup_default_profile_image) // 이미지 로드 실패 시 보여줄 이미지
                    .into(binding.homeMemberProfileCiv) // 이미지를 설정할 ImageView

                // 사용자 아이콘
                val profileIconUrl = userInfo.profileIconUrl
                if (profileIconUrl != null) {
                    bind(binding.root.context, profileIconUrl, binding.homeMemberIconIv)
                    binding.homeMemberIconIv.visibility = View.VISIBLE
                } else {
                    // 아이콘이 없는 경우, 기본 이미지를 설정하거나 숨겨진 이미지를 보여줄 수 있습니다.
                    // 기본 이미지를 설정하는 경우
                    binding.homeMemberIconIv.visibility = View.GONE
                }


                // 사용자 닉네임
                binding.homeMemberNameTv.text = userInfo.nickname

                // 사용자 칭호
                bind(binding.root.context, userInfo.profileTitleUrl, binding.homeMemberBadgeIv)
            }

            homeData.rally?.let { rally ->
                binding.homeRallyingTv.text = rally.name
                binding.rallyLocationdetailTotalTv.text = rally.pilgrimageNumber.toString()
                binding.rallyLocationdetailCheckTv.text = rally.completeNumber.toString()

                // 회원랠리화면
                Glide.with(this)
                    .load(rally.backgroundImageUrl.toString())
                    .placeholder(null)
                    .into(binding.homeRallyIv)
            }


            setupTrendingPostsRecyclerView()
            homeData?.trendingPosts?.let { trendingPosts ->
                trendingPostsAdapter.submitList(trendingPosts)
            }

        } else {
            // 비회원
            binding.userLayout.visibility = View.GONE
            binding.unUserLayout.visibility = View.VISIBLE
        }
    }

    // 비회원
    private fun fetchNonMember() {
        lifecycleScope.launch {
            try {

                val response: Response<HomeService.NonMemberData> = homeService.getNonMemberInfo()
                if (response.isSuccessful) {
                    val nonMemberData = response.body()

                    nonMemberData?.let { data ->
                        setupTrendingPostsRecyclerView()
                        data.trendingPosts?.let { trendingPosts ->
                            trendingPostsAdapter.submitList(trendingPosts)

                            Log.d("HomeFragment", ">> 비회원 : $nonMemberData")

                            // 회원랠리화면
                            Glide.with(requireContext())
                                .load(nonMemberData.rally.backgroundImageUrl.toString())
                                .placeholder(null)
                                .into(binding.homeRecommendIv)

                        }

                    }
                } else {
                    // 비회원 게시물 요청이 실패
                    Log.e("HomeFragment", "Failed to retrieve non-member posts: ${response.code()}")
                }

            } catch (e: Exception) {
                // 오류 발생 시 처리
                Log.e("HomeFragment", "Error fetching non-member posts: ${e.message}", e)
            }
        }
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
}



