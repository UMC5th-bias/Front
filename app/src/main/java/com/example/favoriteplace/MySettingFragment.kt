package com.example.favoriteplace

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.example.favoriteplace.databinding.FragmentMySettingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MySettingFragment : Fragment() {
    lateinit var binding: FragmentMySettingBinding
    private var userToken: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMySettingBinding.inflate(inflater, container, false)



        // 뒤로가기
        binding.mySettingBackIb.setOnClickListener{
            parentFragmentManager.commit {
                replace(R.id.main_frameLayout, MyFragment())
                addToBackStack(null)
            }
        }

        checkLoginStatus(requireActivity()) // 인증 정보 가져오기

        getMyProfile(requireActivity()) //내 프로필 정보 가져오기

        return binding.root
    }

    fun getMyProfile(context: FragmentActivity) {
        //내 프로필 정보 불러오기
        RetrofitAPI.myService.getMyProfile("Bearer $userToken").enqueue(object:
            Callback<MyProfile> {
            override fun onResponse(call: Call<MyProfile>, response: Response<MyProfile>) {
                if(response.isSuccessful) {
                    val responseData = response.body()
                    if(responseData != null) {
                        Log.d("getMyProfile()", "Response: ${responseData}")
                        binding.mySettingNameTet.setText(responseData.nickname)
                        binding.mySettingOnelinerTet.setText(responseData.introducation)
                        binding.mySettingEmailEt.setText(responseData.email)
                        if(responseData.profileImg != null) bindImg(context, responseData.profileImg, binding.mySettingProfileCiv)
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

    //Glide로 이미지 등록
    fun bindImg(context: FragmentActivity, img: String, target: ImageView) {
        Glide.with(context)
            .load(img)
            .placeholder(null)
            .into(target)
    }

}