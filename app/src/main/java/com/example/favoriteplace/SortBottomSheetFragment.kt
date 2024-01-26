package com.example.favoriteplace

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.favoriteplace.databinding.SortBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortBottomSheetFragment : BottomSheetDialogFragment() {
    lateinit var binding: SortBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SortBottomSheetBinding.inflate(inflater, container, false)

        // cancelButton에 클릭 리스너 설정
        binding.bottomSheetCancelIv.setOnClickListener {
            dismiss() // 바텀시트 닫기
        }

        // radioGroup에 체인지 리스너 설정
        binding.sortOptionsRg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.sort_by_title_rb -> {
                    // "제목" 정렬 선택 시 처리
                }
                R.id.sort_by_content_rb -> {
                    // "내용" 정렬 선택 시 처리
                }
                R.id.sort_by_nickname_rb -> {
                    // "닉네임" 정렬 선택 시 처리
                }
            }
        }

        return binding.root
    }

    companion object {
        const val TAG = "SortBottomModalSheet"
    }


}