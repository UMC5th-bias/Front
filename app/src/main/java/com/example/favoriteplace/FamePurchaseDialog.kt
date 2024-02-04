package com.example.favoriteplace

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.favoriteplace.databinding.DialogShopDetailPurchaseFameBinding

class FamePurchaseDialog : DialogFragment(){
    private lateinit var binding: DialogShopDetailPurchaseFameBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DialogShopDetailPurchaseFameBinding.inflate(inflater,container,false)
        val view=binding.root

        //팝업창 모서리 둥글게 만들기
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //구매하지 않기 버튼을 클릭했을 때 팝업창 삭제
        binding.dialogShopDetailPurchaseNoBtn.setOnClickListener{
            dismiss()
        }

        //구매하기 버튼을 클릭했을 때 아이템 적용 팝업창 띄우기, 이 팝업창은 삭제
        binding.dialogShopDetailPurchaseYesBtn.setOnClickListener {
            popupFameApplyClick()
            dismiss()
        }

        return view
    }

    private fun popupFameApplyClick() {
        FameApplyDialog().show(parentFragmentManager,"")
    }
}