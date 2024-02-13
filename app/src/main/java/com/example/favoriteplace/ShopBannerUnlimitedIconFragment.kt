package com.example.favoriteplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentShopDetailUnlimitedIconBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopBannerUnlimitedIconFragment : Fragment() {
    lateinit var binding: FragmentShopDetailUnlimitedIconBinding
    private var gson: Gson = Gson()
    private var unlimitedIconData=ArrayList<ShopDetailsResponse>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopDetailUnlimitedIconBinding.inflate(inflater, container, false)

        //api를 호출하는 코드
        callApi()

        //돌아가기 버튼을 클릭했을 때
        binding.shopBannerDetailIconIb.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, ShopBannerNewFragment())
                .commitAllowingStateLoss()
        }

        //구매하기 버튼을 클릭했을 때
        binding.shopBannerDetailIconPurchaseBn.setOnClickListener {
            popupIconPurchaseClick()
        }

        return binding.root
    }

    //아이콘 구매 팝업창 띄우기
    private fun popupIconPurchaseClick() {
        IconPurchaseDialog().show(parentFragmentManager, "")
    }

    private fun callApi() {

        //신상품 페이지 한정 칭호 RVA로부터 아이템 아이디를 gson으로 가져오는 코드
        val itemIdJson = arguments?.getString("unlimitedIcon")
        val itemId: Int = gson.fromJson(itemIdJson, Int::class.java)
        Log.d("itemId", itemId.toString())

        //서버에서 해당 아이템의 데이터를 가져오는 코드
        RetrofitClient.shopService.getDetailItem("", itemId)
            .enqueue(object : Callback<ShopDetailsResponse> {
                override fun onResponse(
                    call: Call<ShopDetailsResponse>,
                    response: Response<ShopDetailsResponse>
                ) {
                    //서버에서 데이터를 가져오는 걸 성공할 경우
                    if (response.isSuccessful) {
                        val detailsResponse = response.body()

                        Log.d("detailsID",itemId.toString())
                        Log.d("detailsResponse",detailsResponse.toString())
                        detailsResponse?.let {
                            unlimitedIconData.clear()
                            unlimitedIconData.add(it)

                            setView()   //데이터를 반영하여 화면에 보여주는 함수
                        }
                    }
                }

                override fun onFailure(call: Call<ShopDetailsResponse>, t: Throwable) {
                    Log.d("ShopBannerUnlimitedIconFragment","Network Error: ${t.message}")
                }
            })
    }

    //데이터를 반영하여 화면에 보여주는 함수
    private fun setView() {

        ShopBannerLimitedFameFragment().bind(binding.root.context,unlimitedIconData[0].imageUrl, binding.shopBannerDetailIconIconIv)
        ShopBannerLimitedFameFragment().bind(binding.root.context,unlimitedIconData[0].imageUrl, binding.shopBannerDetailIconApplyIconIv)
        binding.shopBannerDetailIconCostTv.text = unlimitedIconData[0].point.toString()
        binding.shopBannerDetailIconBodyTv.text = unlimitedIconData[0].description
        binding.shopBannerDetailIconTitleTv.text = unlimitedIconData[0].name
        binding.shopBannerDetailIconUmcTv.text=unlimitedIconData[0].category
        binding.shopBannerDetailIconUmcTv.text=unlimitedIconData[0].category
    }
}
