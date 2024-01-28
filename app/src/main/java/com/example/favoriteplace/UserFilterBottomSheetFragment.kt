package com.example.favoriteplace

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.favoriteplace.databinding.SortBottomSheetBinding
import com.example.favoriteplace.databinding.UserFilterBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UserFilterBottomSheetFragment : BottomSheetDialogFragment() {
    lateinit var binding: UserFilterBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UserFilterBottomSheetBinding.inflate(inflater, container, false)

        // cancelButton에 클릭 리스너 설정
        binding.userFilterBottomSheetCancelIv.setOnClickListener {
            dismiss() // 바텀시트 닫기
        }

        // radioGroup에 체인지 리스너 설정
        binding.userFilterOptionsRg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.userFilter_block_rb -> {
                    // "제목" 정렬 선택 시 처리
                }
                R.id.userFilter_complaint_rb -> {
                    // "내용" 정렬 선택 시 처리
                }
            }
        }

        return binding.root
    }


}