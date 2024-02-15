package com.example.favoriteplace

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentRallydetailBinding.inflate(inflater,container,false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rallyId = arguments?.getString("rallyId")

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
            if(rallyDetailData.isLike) {
                binding.rallydetailLikeBtn.setImageResource(R.drawable.ic_like_on)
            }
            else {
                binding.rallydetailLikeBtn.setImageResource(R.drawable.ic_like_off)
            }

        }


        RetrofitAPI.rallyDetailService.getRallyDetail(rallyId?.toLong() ?: 1).enqueue(object:
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


}