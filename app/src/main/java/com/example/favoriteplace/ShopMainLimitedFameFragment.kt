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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopMainLimitedFameFragment: Fragment() {
    lateinit var binding: FragmentShopDetailLimitedFameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentShopDetailLimitedFameBinding.inflate(inflater,container,false)

        // Bundle에서 ITEM_ID 추출 (기본값으로 0 지정)
        val itemId = arguments?.getInt("ITEM_ID", 0) ?: 0

        // 아이템 ID를 사용하여 API 호출
        fetchItemDetails(itemId)

        binding.shopBannerDetailFameIb.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, ShopMainFragment())
                .commitAllowingStateLoss()
        }

        binding.shopBannerDetailFamePurchaseBn.setOnClickListener{
            popupFamePurchaseClick()
        }

        return binding.root
    }

    //칭호 구매 팝업창 띄우기
    private fun popupFamePurchaseClick() {
        FamePurchaseDialog().show(parentFragmentManager,"")
    }

    private fun fetchItemDetails(itemId: Int) {
        // 아이템 ID 로그 출력
        Log.d("ShopMainLimitedFameFragment", "Fetching details for item ID: $itemId")

        // 네트워크 라이브러리를 사용하여 /shop/detail/{item_id} 엔드포인트 호출
        // 예: Retrofit 사용 예시
        val itemDetail = RetrofitClient.shopService.getItemDetails(itemId)
        itemDetail.enqueue(object : Callback<ItemDetails> {
            override fun onResponse(
                call: Call<ItemDetails>,
                response: Response<ItemDetails>
            ) {
                if (response.isSuccessful) {
                    // 성공적으로 데이터를 받아온 경우, UI 업데이트

                    val itemDetails = response.body()

                    Log.d("ShopMainFragment", "detail item data received: $itemDetails")

                    updateUI(itemDetails)
                }
            }

            override fun onFailure(call: Call<ItemDetails>, t: Throwable) {
                Log.d("ShopMainFragment", "Item Detail - Network Error: ${t.message}")
            }
        })

    }

    private fun updateUI(itemDetails: ItemDetails?) {
        // 받아온 데이터로 UI 업데이트
        itemDetails?.let {
            // 받아온 데이터로 UI 업데이트
            binding.shopBannerDetailFameUmcTv.text = it.category
            binding.shopBannerDetailFameLimitedTimeTv.text = it.saleDeadline
            binding.shopBannerDetailFameTitleTv.text = it.name
            binding.shopBannerDetailFameCostTv.text = it.point.toString()
            binding.shopBannerDetailFameBodyTv.text = it.description


            val imageLoader = ImageLoader.Builder(binding.root.context)
                .componentRegistry {
                    add(SvgDecoder(binding.root.context)) // SVG 이미지 처리를 위해 SvgDecoder 추가
                }
                .build()

            val imageRequest = ImageRequest.Builder(binding.root.context)
                .crossfade(true)
                .crossfade(300)
                .data(it.imageUrl)
                .target(binding.shopBannerDetailFameImgIv)
                .build()
            imageLoader.enqueue(imageRequest)

//
//            binding.textViewDescription.text = it.description ?: "Description not available"
//            binding.imageView.load(it.imageUrl) // 이는 이미지 로딩 라이브러리를 사용한다고 가정한 예시입니다. 예: Glide or Picasso
            // 기타 필요한 UI 업데이트 로직 추가
        }
    }

}

