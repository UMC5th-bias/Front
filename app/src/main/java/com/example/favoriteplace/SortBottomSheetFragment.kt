package com.example.favoriteplace

import android.app.SearchManager
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.favoriteplace.databinding.SortBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortBottomSheetFragment : BottomSheetDialogFragment() {
    // 이전에 선택한 값을 저장할 변수(초기값: 제목)
    private var previousOption: Int=2131297427
    lateinit var binding: SortBottomSheetBinding

    private var onDismissListener: SearchManager.OnDismissListener? = null

    //onDismissListener이 null이 아닐 때 사용되는 함수
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.onDismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SortBottomSheetBinding.inflate(inflater, container, false)

        // 이전 선택 값이 있으면 UI에 설정
        updateUI(previousOption)

        // cancelButton에 클릭 리스너 설정
        binding.bottomSheetCancelIv.setOnClickListener {
            dismiss() // 바텀시트 닫기
        }

        // radioGroup에 체인지 리스너 설정
        binding.sortOptionsRg.setOnCheckedChangeListener { group, checkedId ->
            previousOption=checkedId
            when (checkedId) {
                R.id.sort_by_title_rb -> {
                    sendSortOption("제목")    // 선택한 정렬 옵션을 부모 Fragment로 전달하는 함수
                }
                R.id.sort_by_content_rb -> {
                    sendSortOption("내용")    // 선택한 정렬 옵션을 부모 Fragment로 전달하는 함수
                }
                R.id.sort_by_nickname_rb -> {
                    sendSortOption("닉네임")    // 선택한 정렬 옵션을 부모 Fragment로 전달하는 함수
                }
            }
        }

        return binding.root
    }

    companion object {
        const val TAG = "SortBottomModalSheet"
    }

    // 선택한 정렬 옵션을 부모 Fragment로 전달하는 함수
    private fun sendSortOption(option: String) {
        val parentFragment = parentFragment
        if (parentFragment is OnSortOptionSelectedListener) {
            parentFragment.onSortOptionSelected(option)
        } // 바텀 시트 닫기
    }

    // 부모 Fragment가 정렬 옵션을 처리할 수 있도록 하는 인터페이스
    interface OnSortOptionSelectedListener {
        fun onSortOptionSelected(option: String)
    }

    // 이전 선택 값을 바탕으로 UI를 업데이트
    private fun updateUI(selectedOption: Int) {
        // 이전에 선택한 라디오 버튼을 체크
        binding.sortOptionsRg.check(selectedOption)
    }
}