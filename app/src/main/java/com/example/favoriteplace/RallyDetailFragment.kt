package com.example.favoriteplace

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.example.favoriteplace.databinding.FragmentRallydetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RallyDetailFragment : Fragment() {

    lateinit var binding:FragmentRallydetailBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var userToken: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentRallydetailBinding.inflate(inflater,container,false)
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rallyId = arguments?.getString("rallyId")

        fun checkLoginStatus() {
            // SharedPreferences에서 액세스 토큰 가져오기
            val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            var isLoggedIn = false
            isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", true)

            if (isLoggedIn) {
                // 로그인 상태인 경우 사용자 정보를 가져옴
                userToken = sharedPreferences.getString(LoginActivity.ACCESS_TOKEN_KEY, "") ?: ""
                if (userToken.isNotEmpty()) {
                    Log.d("HomeFragment", ">> 로그인 상태인 경우 사용자 정보를 가져옴, $userToken")
                }
            }else{
                // 비회원 상태인 경우
                Log.d("HomeFragment", ">> 비회원 상태입니다., $isLoggedIn")

            }
        }

        //유저 인증상태 가져오기
        checkLoginStatus()

        fun setRallyDetail(rallyDetailData: RallyDetailData) {
            Glide.with(this)
                .load(rallyDetailData.image)
                .into(binding.rallydetailImgIv)


            bind(binding.root.context,rallyDetailData.itemImage, binding.rallydetailGetbadgeIv)
            binding.rallydetailTitleTv.text = rallyDetailData.name
            binding.rallydetailCheckTv.text = rallyDetailData.myPilgrimageNumber.toString()
            binding.rallydetailTotalTv.text = rallyDetailData.pilgrimageNumber.toString()
            binding.rallydetailTextTv.text = rallyDetailData.description
            binding.rallydetailPlaceCountTv.text = rallyDetailData.pilgrimageNumber.toString()
            binding.countTv.text = rallyDetailData.achieveNumber.toString()



            binding.rallydetailLikeBtn.setOnClickListener {
                if(isLoggedIn()){

                    Log.d("RallyDetailFragment", "User is logged in")

                    rallyId?.let { id ->
                        RetrofitAPI.rallyDetailService.updateLikeStatus(id.toLong())
                            .enqueue(object : Callback<UpdateResponse> {

                                override fun onResponse(
                                    call: Call<UpdateResponse>,
                                    response: Response<UpdateResponse>
                                ) {
                                    if(response.isSuccessful){
                                        val responseData = response.body()
                                        if(responseData?.success ==true){
                                            binding.rallydetailLikeBtn.setImageResource(R.drawable.ic_like_on)
                                            rallyDetailData.isLike=true
                                            Log.d("RallyDetailFragment", "isLike: $responseData")

                                        }else{
                                            binding.rallydetailLikeBtn.setImageResource(R.drawable.ic_like_off)
                                            rallyDetailData.isLike=false
                                            Log.e("RallyDetailFragment", "Failed to update like status ${response.code()}, ${responseData?.success}, ${responseData?.message}")
                                        }
                                    }else{
                                        // 서버 응답이 실패한 경우
                                        Log.e("RallyDetailFragment", "Server error: ${response.code()}")
                                    }
                                }

                                override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                                    // 네트워크 오류 발생 시
                                    Log.e("RallyDetailFragment", "Network error: ${t.message}")
                                }
                            })
                    }
                }else{
                    Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                    Log.d("rallyDetailData", "isLike: : false ")
                }

            }


        }


        RetrofitAPI.rallyDetailService.getRallyDetail(rallyId?.toLong() ?: 1, "Bearer $userToken").enqueue(object:
            Callback<RallyDetailData> {
            override fun onResponse(call: Call<RallyDetailData>, response: Response<RallyDetailData>) {
                if(response.isSuccessful) {
                    val responseData = response.body()
                    if(responseData != null) {
                        Log.d("Retrofit:getRallyDetail()", "Response: ${responseData}")
                        setRallyDetail(responseData)
                    }
                }
                else {
                    Log.e("Retrofit:getRallyDetail()", "notSuccessful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RallyDetailData>, t: Throwable) {
                Log.e("Retrofit:getRallyDetail()", "onFailure: $t")
            }

        })

    }

    fun bind(context: Context, imageUrl: String, imageView: ImageView) {
        try {
            val imageLoader = ImageLoader.Builder(context)
                .componentRegistry {
                    add(SvgDecoder(context)) // SVG 이미지 처리를 위해 SvgDecoder 추가
                }
                .build()

            val imageRequest = ImageRequest.Builder(context)
                .crossfade(true)
                .data(imageUrl)
                .target(imageView)  //해당 이미지뷰를 타겟으로 svg 삽입
                .build()
            imageLoader.enqueue(imageRequest)

        } catch (e: Exception) {
            Log.e("RallyDetail", "Error loading image: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

}