package com.example.favoriteplace

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.favoriteplace.databinding.FragmentShopDetailUnlimitedIconBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopMainUnlimitedIconFragment : Fragment() {
    lateinit var binding: FragmentShopDetailUnlimitedIconBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopDetailUnlimitedIconBinding.inflate(inflater, container, false)

        val itemId = arguments?.getInt("ITEM_ID", 0) ?: 0

        fetchItemDetails(itemId)

        binding.shopBannerDetailIconIb.setOnClickListener {
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


    private fun getAccessToken(): String? {
        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences?.getString(LoginActivity.ACCESS_TOKEN_KEY, null)
    }

    private fun fetchItemDetails(itemId: Int) {
        // 아이템 ID 로그 출력
        Log.d("ShopMainLimitedFameFragment", "Fetching details for item ID: $itemId")
        val accessToken = getAccessToken() // 액세스 토큰 가져오기
        val authorizationHeader = "Bearer $accessToken"

        val itemDetail = RetrofitClient.shopService.getItemDetails(authorizationHeader, itemId)
        itemDetail.enqueue(object : Callback<ItemDetails> {
            override fun onResponse(
                call: Call<ItemDetails>,
                response: Response<ItemDetails>
            ) {
                if (response.isSuccessful) {
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
            binding.shopBannerDetailIconUmcTv.text = it.category
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
//                .target(binding.shopBannerDetailIconIconIv)
//                .target(binding.shopBannerDetailIconApplyIconIv)
                .target { drawable ->
                    // 첫 번째 ImageView에 이미지 적용
                    binding.shopBannerDetailIconIconIv.setImageDrawable(drawable)
                    // 두 번째 ImageView에 이미지 적용
                    binding.shopBannerDetailIconApplyIconIv.setImageDrawable(drawable)
                }
                .build()
            imageLoader.enqueue(imageRequest)

//            imageLoader.enqueue(
//                imageRequest,
//                ImageLoader.OnSuccessListener { drawable ->
//                    binding.shopBannerDetailIconIconIv.setImageDrawable(drawable)
//                }
//            )
//
//            imageLoader.enqueue(
//                imageRequest,
//                ImageLoader.OnSuccessListener { drawable ->
//                    binding.shopBannerDetailIconApplyIconIv.setImageDrawable(drawable)
//                }
//            )

//
//            binding.textViewDescription.text = it.description ?: "Description not available"
//            binding.imageView.load(it.imageUrl) // 이는 이미지 로딩 라이브러리를 사용한다고 가정한 예시입니다. 예: Glide or Picasso
            // 기타 필요한 UI 업데이트 로직 추가
        }
    }

}
