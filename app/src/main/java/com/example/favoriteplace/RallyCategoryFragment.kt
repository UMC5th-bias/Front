package com.example.favoriteplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentRallycategoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RallyCategoryFragment : Fragment() {

    lateinit var binding: FragmentRallycategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentRallycategoryBinding.inflate(inflater,container,false)


        fun setCategory(rallyCategoryResponseList: List<RallyCategoryResponse>) {
            val animationDatas = mutableListOf<Animation>()
            rallyCategoryResponseList.forEach {
                animationDatas.add(Animation(it.image, it.name, "${it.myPilgrimageNumber}/${it.pilgrimageNumber}"))
            }
            val animationRVAdapter=AnimationRVAdapter(animationDatas, context as MainActivity)
            binding.rallyCategoryAnimationRv.adapter=animationRVAdapter
            binding.rallyCategoryAnimationRv.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        RetrofitAPI.rallyCategoryService.getCategory().enqueue(object: Callback<List<RallyCategoryResponse>> {
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

        return binding.root
    }
}