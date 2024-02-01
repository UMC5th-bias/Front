package com.example.favoriteplace

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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

        //팝업창 모서리 둥글게 만들기
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return view
    }
}