package com.example.favoriteplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.favoriteplace.databinding.FragmentRallydetailBinding
import com.example.favoriteplace.databinding.FragmentRallyhomeBinding
import com.google.android.gms.common.api.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
//import kotlinx.serialization.decodeFromString
//import kotlinx.serialization.json.Json
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.lang.Exception


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
            Glide.with(this)
                .load(rallyDetailData.itemImage)
                .into(binding.rallydetailGetbadgeIv)
            binding.rallydetailTitleTv.text = rallyDetailData.name
            binding.rallydetailCheckTv.text = rallyDetailData.myPilgrimageNumber.toString()
            binding.rallydetailTotalTv.text = rallyDetailData.pilgrimageNumber.toString()
            binding.rallydetailTextTv.text = rallyDetailData.description
            binding.rallydetailPlaceCountTv.text = rallyDetailData.pilgrimageNumber.toString()
            binding.rallydetailCountTv.text = rallyDetailData.achieveNumber.toString()
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


}