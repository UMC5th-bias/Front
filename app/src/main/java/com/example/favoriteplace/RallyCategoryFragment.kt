package com.example.favoriteplace

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentRallycategoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RallyCategoryFragment : Fragment() {

    lateinit var binding: FragmentRallycategoryBinding
    private var userToken: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentRallycategoryBinding.inflate(inflater,container,false)

        fun checkLoginStatus() {
            // SharedPreferences에서 액세스 토큰 가져오기
            val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            userToken = sharedPreferences.getString(LoginActivity.ACCESS_TOKEN_KEY, "") ?: ""

            if (userToken.isNotEmpty()) {
                Log.d("RallyCategoryFragment", ">> 로그인 상태입니다.")
            }else{
                // 비회원 상태인 경우
                Log.d("RallyCategoryFragment", ">> 비회원 상태입니다.")
            }
        }

        //유저 인증상태 가져오기
        checkLoginStatus()


        fun setCategory(rallyCategoryResponseList: List<RallyCategoryResponse>) {
            val animationDatas = mutableListOf<Animation>()
            rallyCategoryResponseList.forEach {
                animationDatas.add(Animation(it.image, it.name, "${it.myPilgrimageNumber}/${it.pilgrimageNumber}", it.id.toString()))
            }
            val animationRVAdapter=AnimationRVAdapter(animationDatas, context as MainActivity)
            binding.rallyCategoryAnimationRv.adapter=animationRVAdapter
            binding.rallyCategoryAnimationRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        //애니메이션별 카테고리 불러오기
        if(userToken == "") {
            Toast.makeText(context, "로그인 후 이용 가능한 메뉴입니다.", Toast.LENGTH_SHORT).show()
        }
        else {
            RetrofitAPI.rallyCategoryService.getCategory("Bearer $userToken").enqueue(object: Callback<List<RallyCategoryResponse>> {
                override fun onResponse(call: Call<List<RallyCategoryResponse>>, response: Response<List<RallyCategoryResponse>>) {
                    if(response.isSuccessful) {
                        val responseData = response.body()
                        if(responseData != null) {
                            Log.d("Retrofit:getCategory()", "Response: ${responseData}")
                            setCategory(responseData)
                        }
                    }
                    else {
                        Log.e("Retrofit:getCategory()", "notSuccessful: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<RallyCategoryResponse>>, t: Throwable) {
                    Log.e("Retrofit:getCategory()", "onFailure: $t")
                }

            })
        }

        return binding.root
    }
}