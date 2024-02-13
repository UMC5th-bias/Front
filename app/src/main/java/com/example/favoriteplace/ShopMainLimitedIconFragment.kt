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
import com.example.favoriteplace.databinding.FragmentShopDetailLimitedIconBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopMainLimitedIconFragment : Fragment() {
    lateinit var binding: FragmentShopDetailLimitedIconBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentShopDetailLimitedIconBinding.inflate(inflater,container,false)
// Bundle에서 ITEM_ID 추출 (기본값으로 0 지정)
        val itemId = arguments?.getInt("ITEM_ID", 0) ?: 0

        // 아이템 ID를 사용하여 API 호출
        fetchItemDetails(itemId)
        binding.shopBannerDetailIconIb.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, ShopMainFragment())
                .commitAllowingStateLoss()
        }

        binding.shopBannerDetailIconPurchaseBn.setOnClickListener {
            popupIconPurchaseClick()
        }

        return binding.root
    }

    //아이콘 구매 팝업창 띄우기
    private fun popupIconPurchaseClick() {
        IconPurchaseDialog().show(parentFragmentManager, "")
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

                    Log.d("ShopMainFragment", "detail icon data received: $itemDetails")

                    updateUI(itemDetails)
                }
            }

            override fun onFailure(call: Call<ItemDetails>, t: Throwable) {
                Log.d("ShopMainFragment", "Icon Detail - Network Error: ${t.message}")
            }
        })

    }

    private fun updateUI(itemDetails: ItemDetails?) {
        // 받아온 데이터로 UI 업데이트
        itemDetails?.let {
            // 받아온 데이터로 UI 업데이트
            binding.shopBannerDetailIconUmcTv.text = it.category
            binding.shopBannerDetailIconLimitedTimeTv.text = it.saleDeadline
            binding.shopBannerDetailIconTimeTv.text = it.saleDeadline
            binding.shopBannerDetailIconTitleTv.text = it.name
            binding.shopBannerDetailIconCostTv.text = it.point.toString()
            binding.shopBannerDetailIconBodyTv.text = it.description


            val imageLoader = ImageLoader.Builder(binding.root.context)
                .componentRegistry {
                    add(SvgDecoder(binding.root.context)) // SVG 이미지 처리를 위해 SvgDecoder 추가
                }
                .build()

            val imageRequest = ImageRequest.Builder(binding.root.context)
                .crossfade(true)
                .crossfade(300)
                .data(it.imageUrl)
//                .target(binding.shopBannerDetailIconIv)
//                .target(binding.shopBannerDetailIconApplyIconIv)
                .target { drawable ->
                    // 첫 번째 ImageView에 이미지 적용
                    binding.shopBannerDetailIconIv.setImageDrawable(drawable)
                    // 두 번째 ImageView에 이미지 적용
                    binding.shopBannerDetailIconApplyIconIv.setImageDrawable(drawable)
                }
                .build()
            imageLoader.enqueue(imageRequest)

//
//            binding.textViewDescription.text = it.description ?: "Description not available"
//            binding.imageView.load(it.imageUrl) // 이는 이미지 로딩 라이브러리를 사용한다고 가정한 예시입니다. 예: Glide or Picasso
            // 기타 필요한 UI 업데이트 로직 추가
        }
    }

}