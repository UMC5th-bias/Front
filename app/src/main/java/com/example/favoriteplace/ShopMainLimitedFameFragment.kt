package com.example.favoriteplace

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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
    private var alreadyBought: Boolean = false
    private var userPoint: Int = 0 // 사용자 포인트를 저장할 변수
    private var itemPoint: Int = 0 // 아이템 가격을 저장할 변수

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
        val itemId = arguments?.getInt("ITEM_ID", 0) ?: 0  // 이 줄은 예시일 뿐, 실제로는 클래스 변수를 사용할 것입니다.

        // 이미 구매한 경우 토스트 메시지 표시
        if (getAccessToken() == null) {
            Toast.makeText(requireActivity(), "로그인이 필요한 기능입니다. 로그인을 해주세요.", Toast.LENGTH_SHORT).show()
        } else if (alreadyBought) {
            Toast.makeText(requireActivity(), "이미 구매한 아이템입니다.", Toast.LENGTH_SHORT).show()
        } else if (userPoint < itemPoint) {
            Toast.makeText(requireActivity(), "포인트가 부족합니다.", Toast.LENGTH_SHORT).show()
        } else {
            // 구매 팝업창 띄우기
            val famePurchaseDialog = FamePurchaseDialog()

            // Bundle 객체 생성 및 userPoint와 itemPoint 값 추가
            val args = Bundle().apply {
                putInt("userPoint", userPoint)
                putInt("itemPoint", itemPoint)
                putInt("ITEM_ID", itemId)
            }
            famePurchaseDialog.arguments = args // Bundle을 Dialog에 설정

            // Dialog를 표시
            famePurchaseDialog.show(parentFragmentManager, "FamePurchaseDialog")
        }
    }

    // sharePreferences에 저장된 액세스 토큰 반환하는 메소드
    private fun getAccessToken(): String? {
        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences?.getString(LoginActivity.ACCESS_TOKEN_KEY, null)
    }

    private fun fetchItemDetails(itemId: Int) {
        // 아이템 ID 로그 출력
        Log.d("ShopMainLimitedFameFragment", "Fetching details for item ID: $itemId")

        val accessToken = getAccessToken() // 액세스 토큰 가져오기
        val authorizationHeader = "Bearer $accessToken"

        // 네트워크 라이브러리를 사용하여 /shop/detail/{item_id} 엔드포인트 호출
        // 예: Retrofit 사용 예시
        val itemDetail = RetrofitClient.shopService.getItemDetails(authorizationHeader,itemId)
        itemDetail.enqueue(object : Callback<ItemDetails> {
            override fun onResponse(
                call: Call<ItemDetails>,
                response: Response<ItemDetails>
            ) {
                if (response.isSuccessful) {
                    // 성공적으로 데이터를 받아온 경우, UI 업데이트

                    val itemDetails = response.body()
                    Log.d("ShopMainFragment", "detail item data received: $itemDetails")

                    // 여기서 itemDetails를 기반으로 alreadyBought 값을 업데이트
                    alreadyBought = itemDetails?.alreadyBought ?: false
                    Log.d("ShopMainFragment", "alreadyBought: $alreadyBought")

                    // 여기서 userPoint와 itemPoint 값을 업데이트
                    userPoint = itemDetails?.userPoint ?: 0
                    itemPoint = itemDetails?.point ?: 0

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
                .target(binding.shopBannerDetailFameIv)
                .build()
            imageLoader.enqueue(imageRequest)
        }
    }

}

