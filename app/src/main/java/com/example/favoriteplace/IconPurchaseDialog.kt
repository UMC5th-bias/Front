package com.example.favoriteplace

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.favoriteplace.databinding.DialogShopDetailPurchaseIconBinding

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

        Log.d("FamePurchaseDialog", "User Point: $userPoint, Item Point: $itemPoint")

        binding.dialogShopDetailPurchaseIconCurrentTv.text = userPoint.toString()

        val remainingPoint = userPoint?.minus(itemPoint!!)
        binding.dialogShopDetailPurchaseIconAfterTv.text =remainingPoint.toString()

        //팝업창 모서리 둥글게 만들기
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.dialogShopDetailPurchaseNoBtn.setOnClickListener{
            dismiss()
        }

        binding.dialogShopDetailPurchaseYesBtn.setOnClickListener {
            popupIconApplyClick()
            dismiss()
        }

        return view
    }

    private fun popupIconApplyClick() {
        FameApplyDialog().show(parentFragmentManager,"")
    }
}