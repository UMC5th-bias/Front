package com.example.favoriteplace

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.favoriteplace.databinding.MyFilterBottomSheetBinding
import com.example.favoriteplace.databinding.SortBottomSheetBinding
import com.example.favoriteplace.databinding.UserFilterBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MyFilterBottomSheetFragment : BottomSheetDialogFragment() {
    lateinit var binding: MyFilterBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyFilterBottomSheetBinding.inflate(inflater, container, false)

        // cancelButton에 클릭 리스너 설정
        binding.myFilterBottomSheetCancelIv.setOnClickListener {
            dismiss() // 바텀시트 닫기
        }

        // radioGroup에 체인지 리스너 설정
        binding.myFilterOptionsRg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.myFilter_modify_rb -> {
                    // "제목" 정렬 선택 시 처리
                }
                R.id.myFilter_delete_rb -> {
                    // "내용" 정렬 선택 시 처리
                }
            }
        }

        return binding.root
    }

    companion object {
        const val TAG = "MyFilterBottomModalSheet"
    }


}