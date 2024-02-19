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
import com.example.favoriteplace.databinding.FragmentShopDetailUnlimitedFameBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopMainUnlimitedFameFragment : Fragment() {
    lateinit var binding: FragmentShopDetailUnlimitedFameBinding
    private var alreadyBought: Boolean = false
    private var userPoint: Int = 0 // 사용자 포인트를 저장할 변수
    private var itemPoint: Int = 0 // 아이템 가격을 저장할 변수
    private var itemName: String="" // 아이템 이름을 저장할 변수

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopDetailUnlimitedFameBinding.inflate(inflater, container, false)

        val itemId = arguments?.getInt("ITEM_ID", 0) ?: 0

        fetchItemDetails(itemId)

        binding.shopBannerDetailFameIb.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, ShopMainFragment())
                .commitAllowingStateLoss()
        }

        binding.shopBannerDetailFamePurchaseBn.setOnClickListener {
            popupFamePurchaseClick()
        }

        return binding.root
    }

    //칭호 구매 팝업창 띄우기
    private fun popupFamePurchaseClick() {
        val itemId = arguments?.getInt("ITEM_ID", 0) ?: 0  // 이 줄은 예시일 뿐, 실제로는 클래스 변수를 사용할 것입니다.

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
                putString("ITEM_NAME",itemName)
                putInt("ITEM_ID", itemId)
            }
            famePurchaseDialog.arguments = args // Bundle을 Dialog에 설정

            // Dialog를 표시
            famePurchaseDialog.show(parentFragmentManager, "FamePurchaseDialog")
        }
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

                    // 여기서 itemDetails를 기반으로 alreadyBought 값을 업데이트
                    alreadyBought = itemDetails?.alreadyBought ?: false
                    Log.d("ShopMainFragment", "alreadyBought: $alreadyBought")

                    // 여기서 userPoint와 itemPoint 값을 업데이트
                    userPoint = itemDetails?.userPoint ?: 0
                    itemPoint = itemDetails?.point ?: 0
                    itemName= itemDetails?.name.toString()

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
