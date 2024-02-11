package com.example.favoriteplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.favoriteplace.databinding.FragmentShopDetailLimitedFameBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class ShopBannerLimitedFameFragment: Fragment() {
    lateinit var binding: FragmentShopDetailLimitedFameBinding
    private var gson: Gson = Gson()
    private var limitedFameData = ArrayList<ShopDetailsResponse>()
    private var detailsResponse: ShopDetailsResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopDetailLimitedFameBinding.inflate(inflater, container, false)

        callApi()

        //돌아가기 버튼을 클릭했을 때
        binding.shopBannerDetailFameIb.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, ShopBannerNewFragment())
                .commitAllowingStateLoss()
        }

        //구매하기 버튼을 클릭했을 때
        binding.shopBannerDetailFamePurchaseBn.setOnClickListener {
            popupFamePurchaseClick()
        }

        return binding.root
    }

    //칭호 구매 팝업창 띄우기
    private fun popupFamePurchaseClick() {
        FamePurchaseDialog().show(parentFragmentManager, "")
    }

    private fun callApi() {

        //신상품 페이지 한정 칭호 RVA로부터 아이템 아이디를 gson으로 가져오는 코드
        val itemIdJson = arguments?.getString("limitedFame")
        val itemId: Int = gson.fromJson(itemIdJson, Int::class.java)
        Log.d("itemId", itemId.toString())

        //
        RetrofitClient.shopService.getDetailItem("", itemId)
            .enqueue(object : Callback<ShopDetailsResponse> {
                override fun onResponse(
                    call: Call<ShopDetailsResponse>,
                    response: Response<ShopDetailsResponse>
                ) {
                    if (response.isSuccessful) {
                        val detailsResponse = response.body()

                        detailsResponse?.let {
                            limitedFameData.clear()
                            limitedFameData.add(it)

                            setView()
                        }
                    }
                }

                override fun onFailure(call: Call<ShopDetailsResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun setView() {
        bind()
        binding.shopBannerDetailFameCostTv.text = limitedFameData[0].point.toString()
        binding.shopBannerDetailFameBodyTv.text = limitedFameData[0].description
        binding.shopBannerDetailFameTitleTv.text = limitedFameData[0].name
        binding.shopBannerDetailFameUmcTv.text=limitedFameData[0].category
        binding.shopBannerDetailFameLimitedTimeTv.text=limitedFameData[0].salesDeadline
        binding.shopBannerDetailFameTimeTv.text=limitedFameData[0].salesDeadline
    }

    private fun bind() {
        try {
            val imageLoader = ImageLoader.Builder(binding.root.context)
                .componentRegistry {
                    add(SvgDecoder(binding.root.context)) // SVG 이미지 처리를 위해 SvgDecoder 추가
                }
                .build()

            val imageRequest = ImageRequest.Builder(binding.root.context)
                .crossfade(true)
                .crossfade(300)  //애니메이션 처리
                .data(limitedFameData[0].imageUrl)
                .target(binding.shopBannerDetailFameIv)  //해당 이미지뷰를 타겟으로 svg 삽입
                .build()
            imageLoader.enqueue(imageRequest)

        } catch (e: Exception) {
            Log.e("ViewHolder", "Error loading image: ${e.message}")
            e.printStackTrace()
        }
    }
}

