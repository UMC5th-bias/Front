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
import com.example.favoriteplace.databinding.FragmentShopDetailLimitedFameBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class ShopBannerLimitedFameFragment: Fragment() {
    lateinit var binding: FragmentShopDetailLimitedFameBinding
    private var gson: Gson = Gson()
    private var limitedFameData = ArrayList<ShopDetailsResponse>()
    private var isLogIn=true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopDetailLimitedFameBinding.inflate(inflater, container, false)

        //api를 호출하는 코드
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

    //api를 호출하는 코드
    private fun callApi() {

        //신상품 페이지 한정 칭호 RVA로부터 아이템 아이디를 gson으로 가져오는 코드
        val itemIdJson = arguments?.getString("limitedFame")
        val itemId: Int = gson.fromJson(itemIdJson, Int::class.java)
        var accessToken: String? =null

        //로그인 중이라면 토큰을 서버에 전달
        if (isLogIn){
            accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0MzI5MjlAbmF2ZXIuY29tIiwiaWF0IjoxNzA3NzU3NzE5LCJleHAiOjE3MTAzNDk3MTl9.CHnXELf6b-vPC--rmZAnwRY6aAvUKt0iPy9Wq_1QYLo"
        }

        //서버에서 해당 아이템의 데이터를 가져오는 코드
        RetrofitClient.shopService.getDetailItem("{$accessToken}", itemId)
            .enqueue(object : Callback<ShopDetailsResponse> {
                override fun onResponse(
                    call: Call<ShopDetailsResponse>,
                    response: Response<ShopDetailsResponse>
                ) {
                    //서버에서 데이터를 가져오는 걸 성공할 경우
                    if (response.isSuccessful) {
                        val detailsResponse = response.body()

                        detailsResponse?.let {
                            limitedFameData.clear()
                            limitedFameData.add(it)

                            setView()   //데이터를 반영하여 화면에 보여주는 함수
                        }
                    }
                }

                override fun onFailure(call: Call<ShopDetailsResponse>, t: Throwable) {
                    Log.d("ShopBannerLimitedFameFragment","Network Error: ${t.message}")
                }
            })
    }

    //데이터를 반영하여 화면에 보여주는 함수
    private fun setView() {
        bind(binding.root.context,limitedFameData[0].imageUrl, binding.shopBannerDetailFameIv)  //svg 이미지를 가져오기 위한 함수
        binding.shopBannerDetailFameCostTv.text = limitedFameData[0].point.toString()
        binding.shopBannerDetailFameBodyTv.text = limitedFameData[0].description
        binding.shopBannerDetailFameTitleTv.text = limitedFameData[0].name
        binding.shopBannerDetailFameUmcTv.text=limitedFameData[0].category
        binding.shopBannerDetailFameLimitedTimeTv.text=limitedFameData[0].saleDeadline
        binding.shopBannerDetailFameTimeTv.text=limitedFameData[0].saleDeadline
    }

    //svg 이미지를 가져오기 위한 함수
    fun bind(context: Context,imageUrl: String, imageView: ImageView) {
        try {
            val imageLoader = ImageLoader.Builder(context)
                .componentRegistry {
                    add(SvgDecoder(context)) // SVG 이미지 처리를 위해 SvgDecoder 추가
                }
                .build()

            val imageRequest = ImageRequest.Builder(context)
                .crossfade(true)
                .crossfade(300)  //애니메이션 처리
                .data(imageUrl)
                .target(imageView)  //해당 이미지뷰를 타겟으로 svg 삽입
                .build()
            imageLoader.enqueue(imageRequest)

        } catch (e: Exception) {
            Log.e("ShopBannerDetailFragment", "Error loading image: ${e.message}")
            e.printStackTrace()
        }
    }
}

