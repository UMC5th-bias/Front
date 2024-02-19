package com.example.favoriteplace

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.example.favoriteplace.databinding.FragmentMyBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFragment : Fragment(){
    lateinit var binding: FragmentMyBinding

    private lateinit var sharedPreferences: SharedPreferences
    private var userToken: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)



        // 프로필 카드 설정
        binding.mySettingIv.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.main_frameLayout, MySettingFragment())
                addToBackStack(null)
            }
        }

        // 찜해둔 성지순례
        binding.myLikeRallyTv.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.main_frameLayout, MyLikeRallyFragment())
                addToBackStack(null)
            }
        }

        // 인증한 성지순례
        binding.myVerifyRallyTv.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.main_frameLayout, MyVerifyRallyFragment())
                addToBackStack(null)
            }
        }

        // 고객센터
        binding.myCsTv.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.main_frameLayout, MyCsFragment())
                addToBackStack(null)
            }
        }



        checkLoginStatus(requireActivity()) //유저 인증정보 가져오기

        //내 정보(완료한 성지순례, 방문한 장소, 작성글, 댓글 불러오기)
        getMyInfo()

        //내 프로필 정보 불러오기(이름, 한줄소개, 프로필 이미지, 뱃지, 아이콘 등)
        getMyProfile(requireActivity())

        //로그아웃
        binding.myLogoutTv.setOnClickListener {
            logout(requireActivity())
        }

        return binding.root
    }

    fun logout(context: FragmentActivity) {
        if(userToken.isEmpty()) return

        RetrofitAPI.myService.logout("Bearer $userToken").enqueue(object:
            Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    val responseData = response.body()
                    if(responseData != null) {
                        Log.d("logout()", "Response: ${responseData}")
                    }
                }
                else {
                    Log.e("logout()", "notSuccessful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                //로그아웃 성공했을 때 failure가 실행됨
                Log.e("logout()", "onFailure: $t")
                sharedPreferences.edit {
                    putBoolean("isLoggedIn", false)
                    putString(LoginActivity.ACCESS_TOKEN_KEY, null)
                    putString(LoginActivity.REFRESH_TOKEN_KEY, null)
                }

                // 홈 fragment로 이동
                val transaction = context.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.main_frameLayout, HomeFragment())
                transaction.commit()
                context.findViewById<BottomNavigationView>(R.id.main_bnv)
                    .selectedItemId = R.id.homeFragment

                Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun getMyProfile(context: FragmentActivity) {
        //내 프로필 정보 불러오기
        RetrofitAPI.myService.getMyProfile("Bearer $userToken").enqueue(object: Callback<MyProfile> {
            override fun onResponse(call: Call<MyProfile>, response: Response<MyProfile>) {
                if(response.isSuccessful) {
                    val responseData = response.body()
                    if(responseData != null) {
                        Log.d("getMyProfile()", "Response: ${responseData}")
                        binding.myNameTv.text = responseData.nickname
                        binding.myPointTv.text = responseData.point.toString()
                        if(responseData.profileImg != null) bindImg(context, responseData.profileImg, binding.myProfileCiv)
                        if(responseData.userIconImg != null) bindSvgImg(context, responseData.userIconImg, binding.myIconIv)
                        if(responseData.userTitleImg != null) bindSvgImg(context, responseData.userTitleImg, binding.myBadgeIv)
                    }
                }
                else {
                    Log.e("getMyProfile()", "notSuccessful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MyProfile>, t: Throwable) {
                Log.e("getMyProfile()", "onFailure: $t")
            }

        })
    }

    fun getMyInfo() {
        //내 정보(완료한 성지순례, 방문한 장소, 작성글, 댓글 불러오기)
        RetrofitAPI.myService.getMyInfo("Bearer $userToken").enqueue(object: Callback<MyInfo> {
            override fun onResponse(call: Call<MyInfo>, response: Response<MyInfo>) {
                if(response.isSuccessful) {
                    val responseData = response.body()
                    if(responseData != null) {
                        Log.d("getMyInfo()", "Response: ${responseData}")
                        binding.myCleanRallyTv.text = responseData.doneRally.toString()
                        binding.myVisitPlaceTv.text = responseData.visitedPlace.toString()
                        binding.myGuestbookTv.text = responseData.posts.toString()
                        binding.myCommentTv.text = responseData.comments.toString()
                    }
                }
                else {
                    Log.e("getMyInfo()", "notSuccessful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MyInfo>, t: Throwable) {
                Log.e("getMyInfo()", "onFailure: $t")
            }

        })
    }

    //Glide로 이미지 등록
    fun bindImg(context: FragmentActivity, img: String, target: ImageView) {
        Glide.with(context)
            .load(img)
            .placeholder(null)
            .into(target)
    }


    //Glide로 svg이미지 불러오기
    fun bindSvgImg(context: FragmentActivity, img: String, target: ImageView) {
        //svg이미지 로더
        val imageLoader = ImageLoader.Builder(context)
            .componentRegistry {
                add(SvgDecoder(context)) // SVG 이미지 처리를 위해 SvgDecoder 추가
            }
            .build()
        val imageRequest = ImageRequest.Builder(context)
            .data(img)
            .target(target)  // 해당 이미지뷰를 타겟으로 svg 삽입
            .build()
        imageLoader.enqueue(imageRequest)
    }

    fun checkLoginStatus(context: FragmentActivity) {
        // SharedPreferences에서 액세스 토큰 가져오기
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        userToken = sharedPreferences.getString(LoginActivity.ACCESS_TOKEN_KEY, "") ?: ""

        if (userToken.isNotEmpty()) {
            Log.d("MyFragment", ">> 로그인 상태입니다.")
        }else{
            // 비회원 상태인 경우
            Log.d("MyFragment", ">> 비회원 상태입니다.")
        }
    }

}