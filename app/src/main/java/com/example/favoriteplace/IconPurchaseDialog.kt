package com.example.favoriteplace

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.favoriteplace.databinding.DialogShopDetailPurchaseIconBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IconPurchaseDialog : DialogFragment(){
    private lateinit var binding: DialogShopDetailPurchaseIconBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DialogShopDetailPurchaseIconBinding.inflate(inflater,container,false)
        val view=binding.root

        super.onViewCreated(view, savedInstanceState)

        // 사용자의 보유 포인트 정보를 저장할 변수
        val userPoint = arguments?.getInt("userPoint", 0) // 기본값 0
        val itemPoint = arguments?.getInt("itemPoint", 0) // 기본값 0
        val itemId = arguments?.getInt("ITEM_ID", 0) ?: 0

        Log.d("ShopMainFragment", "User Point: $userPoint, Item Point: $itemPoint,  Item ID : $itemId")

        binding.dialogShopDetailPurchaseIconCurrentTv.text = userPoint.toString()

        val remainingPoint = userPoint?.minus(itemPoint!!)
        binding.dialogShopDetailPurchaseIconAfterTv.text =remainingPoint.toString()

        //팝업창 모서리 둥글게 만들기
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.dialogShopDetailPurchaseNoBtn.setOnClickListener{
            dismiss()
        }

        binding.dialogShopDetailPurchaseYesBtn.setOnClickListener {
            val token = "Bearer ${getAccessToken()}"

            Log.d("ShopMainFragment", "Item ID : $itemId")

            val call = RetrofitClient.shopService.purchaseItem(token, itemId)
            call.enqueue(object : Callback<PurchaseResponse> {
                override fun onResponse(
                    call: Call<PurchaseResponse>,
                    response: Response<PurchaseResponse>
                ) {
                    if (response.isSuccessful) {
                        val purchaseResponse = response.body()
                        Log.d("ShopMainFragment", "canBuy : ${response.body()}")

                        if (purchaseResponse?.canBuy == true) {
                            // 구매 가능한 경우, 아이템 적용 팝업창 표시
                            popupIconApplyClick()
                        } else {
                            // 구매 불가능한 경우, 로그 출력
                            Log.d("ShopMainFragment", "아이템 구매가 불가능합니다.")
                        }
                    } else {
                        // 요청 실패 처리, 로그 출력
                        Log.d("ShopMainFragment", "요청 실패: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<PurchaseResponse>, t: Throwable) {
                    // 네트워크 오류 등의 실패 처리, 로그 출력
                    Log.d("ShopMainFragment", "네트워크 오류: ${t.message}")
                }
            })
            dismiss()
        }

        return view
    }

    // sharePreferences에 저장된 액세스 토큰 반환하는 메소드
    private fun getAccessToken(): String? {
        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences?.getString(LoginActivity.ACCESS_TOKEN_KEY, null)
    }
    private fun popupIconApplyClick() {
        if (isAdded && !isRemoving) {
            FameApplyDialog().show(parentFragmentManager, "")
        } else {
            Log.d("DEBUG", "Fragment not attached to a context.")
        }
    }
}