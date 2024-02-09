package com.example.favoriteplace


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.favoriteplace.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var retrofit: Retrofit
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var homeService: HomeService
    private var userToken: String? = null



    companion object{
        const val LOGIN_REQUEST_CODE=101

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        //광고 배너
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

        // LoginActivity로부터 accessToken 받기
        val accessToken = arguments?.getString(LoginActivity.ACCESS_TOKEN_KEY)
        if (!accessToken.isNullOrEmpty()) {
            // accessToken이 전달된 경우, 사용자가 로그인 상태임을 나타내는 작업 수행
            getUserInfo(accessToken)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
            val userToken = data?.getStringExtra("accessToken")
            if (!userToken.isNullOrEmpty()) {
                Log.d("HomeFragment", ">> Home Login userToken : $userToken")
                // Retrofit 요청을 보내어 사용자 정보를 가져옵니다.
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
                    // 서버로부터 사용자 정보를 성공적으로 받아왔을 때 UI를 업데이트합니다.
                    val loginResponse: HomeService.LoginResponse? = response.body()
                    if(loginResponse != null && loginResponse.isLoggedIn){
                        updateUI(loginResponse)
                        Log.d("HomeFragment", ">> Home Login Success")
                        Log.d("HomeFragment", ">> $loginResponse")
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

    private fun updateUI(homeData: HomeService.LoginResponse) {
        binding.userLayout.visibility=View.VISIBLE
        binding.unUserLayout.visibility=View.GONE

        // 사용자 정보가 제대로 반환되었을 때만 UI 업데이트
        homeData.userInfo?.let { userInfo ->
            // 사용자 이미지
            Glide.with(this)
                .load(userInfo.profileImageUrl.toString()) // 서버에 저장된 이미지 URI
                .placeholder(R.drawable.signup_default_profile_image) // 이미지를 불러오는 동안 보여줄 임시 이미지
                .error(R.drawable.signup_default_profile_image) // 이미지 로드 실패 시 보여줄 이미지
                .into(binding.homeMemberProfileCiv) // 이미지를 설정할 ImageView

            // 사용자 아이콘
            Glide.with(this)
                .load(userInfo.profileIconUrl.toString()) // 서버에 저장된 이미지 URI
                .placeholder(R.drawable.signup_profile_background) // 이미지를 불러오는 동안 보여줄 임시 이미지
                .error(R.drawable.signup_profile_background) // 이미지 로드 실패 시 보여줄 이미지
                .into(binding.homeMemberIconIv) // 이미지를 설정할 ImageView

            // 사용자 뱃지
            Glide.with(this)
                .load(userInfo.profileTitleUrl.toString()) // 서버에 저장된 이미지 URI
                .placeholder(R.drawable.user_title) // 이미지를 불러오는 동안 보여줄 임시 이미지
                .error(R.drawable.user_title) // 이미지 로드 실패 시 보여줄 이미지
                .into(binding.homeMemberIconIv) // 이미지를 설정할 ImageView

            // 사용자 닉네임
            binding.homeMemberNameTv.text = userInfo.nickname
            }


        // 비회원 랠리 텍스트
        Glide.with(this)
            .load(homeData.rally?.backgroundImageUrl) // 서버에 저장된 이미지 URI
            .placeholder(R.drawable.community_rally_place_7) // 이미지를 불러오는 동안 보여줄 임시 이미지
            .error(R.drawable.community_rally_place_7) // 이미지 로드 실패 시 보여줄 이미지
            .into(binding.homeRecommendIv) // 이미지를 설정할 ImageView



    }
}

