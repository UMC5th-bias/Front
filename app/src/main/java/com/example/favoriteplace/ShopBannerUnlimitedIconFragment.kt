package com.example.favoriteplace

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    private var alreadyBought: Boolean = false
    private var userPoint: Int = 0 // 사용자 포인트를 저장할 변수
    private var itemPoint: Int = 0 // 아이템 가격을 저장할 변수
    private var itemName: String="" // 아이템 이름을 저장할 변수

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
//        val itemId = arguments?.getInt("ITEM_ID", 0) ?: 0  // 이 줄은 예시일 뿐, 실제로는 클래스 변수를 사용할 것입니다.
//
//        if (getAccessToken() == null) {
//            Toast.makeText(requireActivity(), "로그인이 필요한 기능입니다. 로그인을 해주세요.", Toast.LENGTH_SHORT).show()
//        } else if(alreadyBought) {
//            Toast.makeText(requireActivity(), "이미 구매한 아이템입니다.", Toast.LENGTH_SHORT).show()
//        } else if (userPoint < itemPoint) {
//            Toast.makeText(requireActivity(), "포인트가 부족합니다.", Toast.LENGTH_SHORT).show()
//        } else {
//            // 구매 팝업창 띄우기
//            val iconPurchaseDialog =  IconPurchaseDialog()
//
//            // Bundle 객체 생성 및 userPoint와 itemPoint 값 추가
//            val args = Bundle().apply {
//                putInt("userPoint", userPoint)
//                putInt("itemPoint", itemPoint)
//                putString("ITEM_NAME",itemName)
//                putInt("ITEM_ID", itemId)
//            }
//            iconPurchaseDialog.arguments = args // Bundle을 Dialog에 설정
//
//            // Dialog를 표시
//            iconPurchaseDialog.show(parentFragmentManager, "FamePurchaseDialog")
//        }

//        신상품 페이지 상시 아이콘 RVA로부터 아이템 아이디를 gson으로 가져오는 코드
        val itemIdJson = arguments?.getString("unlimitedIcon")
        val itemId: Int = gson.fromJson(itemIdJson, Int::class.java)

        if (getAccessToken()==null){
            Toast.makeText(requireActivity(), "로그인이 필요한 기능입니다. 로그인을 해주세요.", Toast.LENGTH_SHORT).show()
        } else if (alreadyBought) {
            Toast.makeText(requireActivity(), "이미 구매한 아이템입니다.", Toast.LENGTH_SHORT).show()
        } else if (userPoint < itemPoint) {
            Toast.makeText(requireActivity(), "포인트가 부족합니다.", Toast.LENGTH_SHORT).show()
        } else {
            val args = Bundle().apply {
                userPoint.let { putInt("userPoint", it) }
                putInt("itemPoint", itemPoint)
                putInt("ITEM_ID", itemId)
                putString("ITEM_NAME", itemName)
                Log.d("itemName", itemName)
            }
            val dialog = IconPurchaseDialog()
            dialog.arguments = args
            dialog.show(parentFragmentManager, "")
        }
    }

    private fun callApi() {

        //신상품 페이지 상시 아이콘 RVA로부터 아이템 아이디를 gson으로 가져오는 코드
        val itemIdJson = arguments?.getString("unlimitedIcon")
        val itemId: Int = gson.fromJson(itemIdJson, Int::class.java)

        val accessToken = getAccessToken() // 액세스 토큰 가져오기
        val authorizationHeader = "Bearer $accessToken"

        //서버에서 해당 아이템의 데이터를 가져오는 코드
        RetrofitClient.shopService.getDetailItem(authorizationHeader, itemId)
            .enqueue(object : Callback<ShopDetailsResponse> {
                override fun onResponse(
                    call: Call<ShopDetailsResponse>,
                    response: Response<ShopDetailsResponse>
                ) {

                    //서버에서 데이터를 가져오는 걸 성공할 경우
                    if (response.isSuccessful) {
                        val detailsResponse = response.body()

                        detailsResponse?.let {
                            unlimitedIconData.clear()
                            unlimitedIconData.add(it)

                            // 여기서 itemDetails를 기반으로 alreadyBought 값을 업데이트
                            alreadyBought = detailsResponse.alreadyBought

                            // 여기서 userPoint와 itemPoint 값을 업데이트
                            userPoint = detailsResponse.userPoint ?: 0
                            itemPoint = detailsResponse.point ?: 0
                            itemName=detailsResponse.name
                            setView(it)   //데이터를 반영하여 화면에 보여주는 함수
                        }
                    }
                }

                override fun onFailure(call: Call<ShopDetailsResponse>, t: Throwable) {
                    Log.d("ShopBannerUnlimitedIconFragment","Network Error: ${t.message}")
                }
            })
    }

    //데이터를 반영하여 화면에 보여주는 함수
    private fun setView(detail: ShopDetailsResponse) {

        ShopBannerLimitedFameFragment().bind(binding.root.context,detail.imageCenterUrl, binding.shopBannerDetailIconIconIv)
        ShopBannerLimitedFameFragment().bind(binding.root.context,detail.imageUrl, binding.shopBannerDetailIconApplyIconIv)
        binding.shopBannerDetailIconCostTv.text = detail.point.toString()
        binding.shopBannerDetailIconBodyTv.text = detail.description
        binding.shopBannerDetailIconTitleTv.text = detail.name
        binding.shopBannerDetailIconUmcTv.text=detail.category
        binding.shopBannerDetailIconUmcTv.text=detail.category
    }

    // sharePreferences에 저장된 액세스 토큰 반환하는 메소드
    private fun getAccessToken(): String? {
        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences?.getString(LoginActivity.ACCESS_TOKEN_KEY, null)
    }
}
