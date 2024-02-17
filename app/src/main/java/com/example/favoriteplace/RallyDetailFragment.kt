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

    private var isLiked: Boolean = false // 좋아요 상태 추적


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

        RetrofitAPI.rallyDetailService.getRallyDetail(rallyId?.toLong() ?: 1, accessToken).enqueue(object :
            Callback<RallyDetailData> {
            override fun onResponse(
                call: Call<RallyDetailData>,
                response: Response<RallyDetailData>
            ) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null) {
                        Log.d("Retrofit:getRallyDetail()", "Response: ${responseData}")
                        setRallyDetail(responseData)
                    }
                } else {
                    Log.e("Retrofit:getRallyDetail()", "notSuccessful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RallyDetailData>, t: Throwable) {
                Log.e("Retrofit:getRallyDetail()", "onFailure: $t")
            }

        })

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
                Log.d("RallyDetailFragment", ">> $accessToken")
                if(!accessToken.isNullOrEmpty()){
                    // 토큰이 있는 경우 -> 로그인 상태

                    // 클릭할 때마다 isLike 값을 토글
                    isLiked = !isLiked
                    // isLike 값에 따라 이미지 변경
                    if (isLiked) {
                        binding.rallydetailLikeBtn.setImageResource(R.drawable.ic_like_on)
                    } else {
                        binding.rallydetailLikeBtn.setImageResource(R.drawable.ic_like_off)
                    }

                    // isLike 값 업데이트
                    rallyDetailData.isLike = isLiked

                    Log.d("RallyDetailFragment", "isLike: $isLiked")
                    Log.d("RallyDetailFragment", ">> $rallyDetailData")

                }else{
                    Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                }
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
//    private fun isLoggedIn(): Boolean {
//        return sharedPreferences.getBoolean("isLoggedIn", false)
//    }
