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


    companion object{
        const val LOGIN_REQUEST_CODE=101
        const val ACCESS_TOKEN_KEY = "accessToken" // SharedPreferences 키 상수
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

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
        binding.homeBannerVp.adapter=bannerAdapter
        binding.homeBannerVp.orientation=ViewPager2.ORIENTATION_HORIZONTAL
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_banner1))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_banner1))




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

        }else{
            // 비회원 상태인 경우
            Log.d("HomeFragment", ">> 비회원 상태입니다., $isLoggedIn")
            fetchNonMember()

        }
    }



    private fun sendLoginStatusToServer(isLoggedIn: String?) {

        lifecycleScope.launch {
            try {
                // 로그인 상태를 서버에 전달
                val response: Response<HomeService.LoginResponse> = homeService.getUserInfo(isLoggedIn)
                if (response.isSuccessful) {
                    Log.d("HomeFragment", ">> Login status sent to server: $isLoggedIn")
                } else {
                    Log.e("HomeFragment", "Failed to send login status to server: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "Error sending login status to server: ${e.message}", e)
            }
        }
    }


    override fun onStop() {
        super.onStop()
        // 앱이 종료될 때 로그아웃 상태를 SharedPreferences에 저장
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", isLoggedIn)
            putString(ACCESS_TOKEN_KEY, accessToken)
            apply()
        }

    }

    private fun setupTrendingPostsRecyclerView() {

        // TrendingPostsAdapter 초기화
        trendingPostsAdapter = TrendingPostsAdapter(trendingPostsData)
        binding.trendingPostsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.trendingPostsRecyclerView.adapter = trendingPostsAdapter

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
            val isLoggedIn = data?.getBooleanExtra("isLoggedIn", false) ?: false
            val userToken = data?.getStringExtra("accessToken")
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
                if(response.isSuccessful){
                    // 로그인 상태인 경우
                    // 서버로부터 사용자 정보를 성공적으로 받아왔을 때 UI 업데이트
                    val loginResponse: HomeService.LoginResponse? = response.body()
                    if(loginResponse != null){
                        updateUI(loginResponse)

                        Log.d("HomeFragment", ">> Home Login Success")

                    }
                }else{
                    // 로그인 상태가 아닌 경우
                    Log.e("HomeFragment", "Failed to get home data: ${response.code()}")
                }
            }catch (e:Exception){
                // 오류
                Log.e("HomeFragment", "Error fetching user info: ${e.message}", e)
            }
        }

    }


    private fun updateUI(homeData: HomeService.LoginResponse?) {

        Log.d("HomeFragment", ">> $homeData")



        if (homeData != null ) {
            binding.userLayout.visibility=View.VISIBLE
            binding.unUserLayout.visibility=View.GONE

            binding.nonMembersLayout.visibility=View.GONE
            binding.membersRallyLayout.visibility=View.VISIBLE


            // 사용자 정보가 제대로 반환되었을 때만 UI 업데이트
            homeData.userInfo?.let { userInfo ->
                // 사용자 이미지
                Glide.with(this)
                    .load(userInfo.profileImageUrl.toString()) // 서버에 저장된 이미지 URI
                    .placeholder(R.drawable.signup_default_profile_image) // 이미지를 불러오는 동안 보여줄 임시 이미지
                    .error(R.drawable.signup_default_profile_image) // 이미지 로드 실패 시 보여줄 이미지
                    .into(binding.homeMemberProfileCiv) // 이미지를 설정할 ImageView


                // 사용자 뱃지
                Glide.with(this)
                    .load(userInfo.profileTitleUrl.toString())
                    .placeholder(null)
                    .into(binding.homeMemberIconIv)

                // 사용자 닉네임
                binding.homeMemberNameTv.text = userInfo.nickname

                // 사용자 아이콘
                bind(binding.root.context, userInfo.profileIconUrl, binding.homeMemberIconIv)
            }

            homeData.rally?.let { rally ->
                binding.homeRallyingTv.text=rally.name
                binding.rallyLocationdetailTotalTv.text=rally.pilgrimageNumber.toString()
                binding.rallyLocationdetailCheckTv.text=rally.completeNumber.toString()

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

        }
        else{
            // 비회원
            binding.userLayout.visibility = View.GONE
            binding.unUserLayout.visibility = View.VISIBLE
        }
    }

    private fun fetchNonMember() {
        // 비회원
        lifecycleScope.launch {
            try {

                val response : Response<HomeService.NonMemberData> = homeService.getNonMemberInfo()
                if(response.isSuccessful){
                    val nonMemberData = response.body()

                    nonMemberData?.let { data ->
                        setupTrendingPostsRecyclerView()
                        data.trendingPosts?.let { trendingPosts ->
                            trendingPostsAdapter.submitList(trendingPosts)
                        }
                    }
                }else{
                    // 비회원 게시물 요청이 실패
                    Log.e("HomeFragment", "Failed to retrieve non-member posts: ${response.code()}")
                }

            }catch (e:Exception){
                // 오류 발생 시 처리
                Log.e("HomeFragment", "Error fetching non-member posts: ${e.message}", e)
            }
        }
    }

    //svg 이미지를 가져오기 위한 함수
    fun bind(context: Context,iconImageUrl: String?, imageView: ImageView) {
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




