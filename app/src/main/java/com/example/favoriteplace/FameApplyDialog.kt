package com.example.favoriteplace

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.favoriteplace.databinding.DialogShopDetailApplyFameBinding

class FameApplyDialog : DialogFragment(){
    private lateinit var binding: DialogShopDetailApplyFameBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DialogShopDetailApplyFameBinding.inflate(inflater,container,false)
        val view=binding.root

        //팝업창 모서리 둥글게 만들기
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.dialogShopDetailApplyNoBtn.setOnClickListener{
            Toast.makeText(requireContext(),"취소되었습니다",Toast.LENGTH_SHORT).show()
            dismiss()
        }

        binding.dialogShopDetailApplyYesBtn.setOnClickListener {
            Toast.makeText(requireContext(),"적용되었습니다", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        return view
    }
}