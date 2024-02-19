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
import com.example.favoriteplace.databinding.DialogShopDetailPurchaseFameBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FamePurchaseDialog : DialogFragment() {
    private lateinit var binding: DialogShopDetailPurchaseFameBinding
    private var fragmentAttached = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentAttached = true
    }

    override fun onDetach() {
        super.onDetach()
        fragmentAttached = false
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogShopDetailPurchaseFameBinding.inflate(inflater, container, false)
        val view = binding.root

        // 프래그먼트가 활성화되어 있는 경우에만 코드 실행
        if (fragmentAttached) {
            // 이하 코드 생략
            super.onViewCreated(view, savedInstanceState)

            // 사용자의 보유 포인트 정보를 저장할 변수
            var userPoint = arguments?.getInt("userPoint") // 기본값 0
            var itemPoint = arguments?.getInt("itemPoint") // 기본값 0
            var itemId = arguments?.getInt("ITEM_ID")
            var itemName=arguments?.getString("ITEM_NAME")

            //itemId가 0일 때 (신상품 페이지에서 이동했을 때 변수 지정)
            if(itemId==0){
                userPoint=arguments?.getInt("newUserPoint")
                itemPoint=arguments?.getInt("newItemPoint")
                itemId=arguments?.getInt("NewItemID")
                itemName=arguments?.getString("NewItemName")
            }
            // 로그 출력
            Log.d(
                "ShopMainFragment",
                "User Point: $userPoint, Item Point: $itemPoint, Item ID : $itemId"
            )

            //유저 포인트와 아이템 이름 적용
            binding.dialogShopDetailPurchaseFameCurrentTv.text = userPoint.toString()
            binding.dialogShopDetailPurchaseFameNameTv.text=itemName

            val remainingPoint = userPoint?.minus(itemPoint!!)
            binding.dialogShopDetailPurchaseFameAfterTv.text = remainingPoint.toString()

            //팝업창 모서리 둥글게 만들기
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            //구매하지 않기 버튼을 클릭했을 때 팝업창 삭제
            binding.dialogShopDetailPurchaseNoBtn.setOnClickListener {
                dismiss()
            }

            //구매하기 버튼을 클릭했을 때 아이템 적용 팝업창 띄우기, 이 팝업창은 삭제
            binding.dialogShopDetailPurchaseYesBtn.setOnClickListener {
                val token = "Bearer ${getAccessToken()}"

                Log.d("ShopMainFragment", "Item ID : $itemId")

                itemId?.let { it1 ->
                    RetrofitClient.shopService.purchaseItem(
                        token,
                        it1
                    )
                }?.enqueue(object : Callback<PurchaseResponse> {
                    override fun onResponse(
                        call: Call<PurchaseResponse>,
                        response: Response<PurchaseResponse>
                    ) {
                        if (response.isSuccessful) {
                            val purchaseResponse = response.body()
                            Log.d("ShopMainFragment", "canBuy : $purchaseResponse")

                            if (itemName != null) {
                                popupFameApplyClick(itemId, itemName)
                            }
                            dismiss()
                        } else {
                            // 요청 실패 처리, 로그 출력
                            Log.d("ShopMainFragment", "요청 실패: ${response.errorBody()?.string()}")
                            dismiss()
                        }
                    }

                    override fun onFailure(call: Call<PurchaseResponse>, t: Throwable) {
                        // 네트워크 오류 등의 실패 처리, 로그 출력
                        Log.d("ShopMainFragment", "네트워크 오류: ${t.message}")
                        dismiss()
                    }
                })
            }
        } else {
            Log.d("DEBUG", "Fragment not attached to a context.")
        }


        return view
    }

    // sharePreferences에 저장된 액세스 토큰 반환하는 메소드
    private fun getAccessToken(): String? {
        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences?.getString(LoginActivity.ACCESS_TOKEN_KEY, null)
    }

    private fun popupFameApplyClick(itemId: Int, itemName: String) {
        if (isAdded && !isRemoving) {
            val dialog = FameApplyDialog()
            val args = Bundle().apply {
                putInt("ITEM_ID", itemId)
                putString("ITEM_NAME",itemName)
            }
            dialog.arguments = args
            dialog.show(parentFragmentManager, "")
        } else {
            Log.d("DEBUG", "Fragment not attached to a context.")
        }
    }

}