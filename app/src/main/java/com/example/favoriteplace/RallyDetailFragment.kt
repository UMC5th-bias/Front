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

    lateinit var binding : FragmentRallydetailBinding
    private lateinit var sharedPreferences: SharedPreferences

    private var isLiked: Boolean = false // 좋아요 상태 추적
    var clickCount = 0 // 클릭 횟수를 추적하기 위한 변수

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
        val accessToken = sharedPreferences.getString("token", null)

        Log.d("RallyDetailFragment", "--------> : $accessToken")


        if(!accessToken.isNullOrEmpty()){
            RetrofitAPI.rallyDetailService.getRallyDetail(rallyId?.toLong() ?: 1, accessToken).enqueue(object :
                Callback<RallyDetailData> {
                override fun onResponse(
                    call: Call<RallyDetailData>,
                    response: Response<RallyDetailData>
                ) {
                    if (response.isSuccessful) {
                        val responseData = response.body()
                        if (responseData != null) {
                            Log.d("RallyDetailFragment", "Response: ${responseData}")
                            setRallyDetail(responseData)
                            handleLikeButton(isLiked)
                            Log.d("RallyDetailFragment", "--------> : ${isLiked}")
                        }
                    } else {
                        Log.e("RallyDetailFragment", "notSuccessful: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<RallyDetailData>, t: Throwable) {
                    Log.e("RallyDetailFragment", "onFailure: $t")
                }

            })

        }
    }

    private fun setRallyDetail(rallyDetailData: RallyDetailData) {
        Glide.with(this)
            .load(rallyDetailData.image)
            .into(binding.rallydetailImgIv)


        bind(binding.root.context, rallyDetailData.itemImage, binding.rallydetailGetbadgeIv)
        binding.rallydetailTitleTv.text = rallyDetailData.name
        binding.rallydetailCheckTv.text = rallyDetailData.myPilgrimageNumber.toString()
        binding.rallydetailTotalTv.text = rallyDetailData.pilgrimageNumber.toString()
        binding.rallydetailTextTv.text = rallyDetailData.description
        binding.rallydetailPlaceCountTv.text = rallyDetailData.pilgrimageNumber.toString()
        binding.countTv.text = rallyDetailData.achieveNumber.toString()

        binding.rallydetailLikeBtn.setOnClickListener {
            // 토큰 확인하여 로그인 여부 확인
            val accessToken = sharedPreferences.getString("token", null)
            val rallyId = arguments?.getString("rallyId")
            Log.d("RallyDetailFragment", ">> rallyId: $rallyId, \n $accessToken")
        }
    }

    private fun sendLikeStatusToServer(rallyId: String?, accessToken: String?, isLiked: Boolean) {
        // rallyId를 Long으로 변환
        val rallyIdLong = rallyId?.toLongOrNull()
        if (rallyIdLong == null) {
            Log.d("RallyDetailFragment", "Invalid rallyId: $rallyId")
            return
        }
        val accessToken = sharedPreferences.getString("token", null)

        // 서버에 좋아요 상태 업데이트를 요청하는 Retrofit 호출
        RetrofitAPI.rallyDetailService.updateLikeStatus(rallyIdLong, "Bearer $accessToken")
            .enqueue(object : Callback<UpdateLikeResponse> {
                override fun onResponse(call: Call<UpdateLikeResponse>, response: Response<UpdateLikeResponse>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            val message = responseBody.message
                            val success = responseBody.success

                            Log.d("RallyDetailFragment", "Response: $message, $success")
                            if(success==true){
                                binding.rallydetailLikeBtn.setImageResource(R.drawable.ic_like_on)
                            }else{
                                binding.rallydetailLikeBtn.setImageResource(R.drawable.ic_like_off)
                            }
                        } else {
                            Log.e("RallyDetailFragment", "Response body is null")
                        }
                    } else {
                        Log.e("RallyDetailFragment", "Response is not successful: ${response.code()}")
                        // 서버로부터 받은 오류 메시지 또는 기타 처리 가능
                    }
                }

                override fun onFailure(call: Call<UpdateLikeResponse>, t: Throwable) {
                    // 통신 실패 처리
                    Log.e("RallyDetailFragment", "onFailure: $t")
                    // 통신 실패 메시지를 사용자에게 표시하거나 다른 방식으로 처리 가능
                }
            })
    }

    private fun handleLikeButton(isLike: Boolean) {
        // 좋아요 버튼 클릭 리스너 설정
        binding.rallydetailLikeBtn.setOnClickListener {
            val rallyId = arguments?.getString("rallyId")
            val accessToken = sharedPreferences.getString("token", null)

            Log.d("RallyDetailFragment", "애니 번호 : $rallyId 번")
            if (!accessToken.isNullOrEmpty()) {
                // 클릭할 때마다 isLike 값 설정
                val updatedIsLiked  = !isLike
                // 서버에 좋아요 상태 업데이트 요청 보내기
                Log.d("RallyDetailFragment", "2번 isLike : $isLike")
                sendLikeStatusToServer(rallyId, accessToken, isLiked)
            } else {
                Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
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
}