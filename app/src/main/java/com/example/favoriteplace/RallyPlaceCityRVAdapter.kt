package com.example.favoriteplace

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemRallyplaceCitylistBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RallyPlaceCityRVAdapter(
    private val context: Context,
    private val districtDetailList: List<DistrictDetail>,
    private val naverMap: NaverMap,
    private val markerAdd: (LatLng, String) -> Unit,
    private val markerCLear: () -> Unit
) : RecyclerView.Adapter<RallyPlaceCityRVAdapter.viewHolder>() {

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): RallyPlaceCityRVAdapter.viewHolder {
        val binding: ItemRallyplaceCitylistBinding = ItemRallyplaceCitylistBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
        return viewHolder(binding)
    }

    override fun getItemCount(): Int {
        return districtDetailList.size
    }

    override fun onBindViewHolder(holder: RallyPlaceCityRVAdapter.viewHolder, position: Int) {
        holder.bind(districtDetailList[position])
    }

    inner class viewHolder(val binding: ItemRallyplaceCitylistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(districtDetail: DistrictDetail) {
            binding.rallyplaceCityName.text = districtDetail.district
            binding.rallyplaceCityName.setOnClickListener {

                markerCLear() // 모든 마커 지우기

                var animationList: List<RallyPlaceAnimation>? = null

                if (binding.rallyplaceLocationRV.isVisible) binding.rallyplaceLocationRV.visibility =
                    View.GONE
                else {
                    binding.rallyplaceLocationRV.visibility = View.VISIBLE
                    //애니메이션 목록 불러오기
                    RetrofitAPI.rallyPlaceService.getAnimationList(districtDetail.id)
                        .enqueue(object :
                            Callback<List<RallyPlaceAnimation>> {
                            override fun onResponse(
                                call: Call<List<RallyPlaceAnimation>>,
                                response: Response<List<RallyPlaceAnimation>>
                            ) {
                                if (response.isSuccessful) {
                                    val responseData = response.body()
                                    if (responseData != null) {
                                        Log.d(
                                            "Retrofit:getAnimationList()",
                                            "Response: ${responseData}"
                                        )
                                        animationList = responseData
                                        animationList?.sortedBy { it.id } //정렬

                                        animationList?.forEach {
                                            markerAdd(LatLng(it.latitude.toDouble(), it.longitude.toDouble()), it.title)
                                        }

                                        //애니메이션 rv 설정
                                        val rallyPlaceLocationRVAdapter =
                                            RallyPlaceLocationRVAdapter(
                                                context,
                                                animationList ?: emptyList()
                                            )
                                        binding.rallyplaceLocationRV.adapter =
                                            rallyPlaceLocationRVAdapter
                                        binding.rallyplaceLocationRV.layoutManager =
                                            LinearLayoutManager(
                                                context,
                                                LinearLayoutManager.VERTICAL,
                                                false
                                            )
                                    }
                                } else {
                                    Log.e(
                                        "Retrofit:getAnimationList()",
                                        "notSuccessful: ${response.code()}"
                                    )
                                }
                            }

                            override fun onFailure(
                                call: Call<List<RallyPlaceAnimation>>,
                                t: Throwable
                            ) {
                                Log.e("Retrofit:getAnimationList()", "onFailure: $t")
                            }

                        })
                }
            }
        }
    }

}