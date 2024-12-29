package com.example.favoriteplace

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.favoriteplace.databinding.MyFilterBottomSheetBinding
import com.example.favoriteplace.databinding.SortBottomSheetBinding
import com.example.favoriteplace.databinding.UserFilterBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MyFilterBottomSheetFragment : BottomSheetDialogFragment() {
    lateinit var binding: MyFilterBottomSheetBinding

    // 작성 여부를 결정하는 변수 (true: 내가 쓴 글/댓글, false: 다른 사람이 쓴 글/댓글)
    private var isMyContent: Boolean = false
    private var commentId: String? = null   // 댓글 ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isMyContent = it.getBoolean("isMyContent", false) // 기본값 false
            commentId = it.getString("commentId") // 댓글 ID
            Log.d("MyFilterBottomSheet", "isMyContent: $isMyContent") // 디버깅 로그 추가
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyFilterBottomSheetBinding.inflate(inflater, container, false)

        // UI 동적 설정
        setupOptions()

        // cancelButton에 클릭 리스너 설정
        binding.myFilterBottomSheetCancelIv.setOnClickListener {
            dismiss() // 바텀시트 닫기
        }

        // radioGroup에 체인지 리스너 설정
        binding.myFilterOptionsRg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.myFilter_modify_rb -> {
                    // 게시글/댓글 수정 팝업 호출
//                    val isPost = arguments?.getBoolean("isPost") ?: false
//                    val modifyDialog = ModifyDialogFragment.newInstance(isPost)
//                    modifyDialog.show(parentFragmentManager, "ModifyDialog")
                }
                R.id.myFilter_delete_rb -> {
                    /// 게시글/댓글 삭제 팝업 호출
                    val isPost = arguments?.getBoolean("isPost") ?: false
                    val deleteDialog = DeleteDialogFragment.newInstance(isPost)
                    deleteDialog.show(parentFragmentManager, "DeleteDialog")
                }
                R.id.myFilter_block_rb -> {
                    // 차단 팝업 호출
                    val blockDialog = BlockDialogFragment() // 차단용 팝업 클래스
                    blockDialog.show(parentFragmentManager, "BlockDialog")
                }
                R.id.myFilter_report_rb -> {
                    // 신고 팝업 호출
                    val notifyDialog = NotifyDialogFragment() // 신고용 팝업 클래스
                    notifyDialog.show(parentFragmentManager, "NotifyDialog")
                }
            }
        }

        return binding.root
    }

    private fun setupOptions() {
        if (isMyContent) {
            // 내가 작성한 경우: 수정/삭제 표시
            binding.myFilterModifyRb.visibility = View.VISIBLE
            binding.myFilterDeleteRb.visibility = View.VISIBLE
            binding.myFilterBlockRb.visibility = View.GONE
            binding.myFilterReportRb.visibility = View.GONE
        } else {
            // 다른 사람이 작성한 경우: 차단/신고 표시
            binding.myFilterModifyRb.visibility = View.GONE
            binding.myFilterDeleteRb.visibility = View.GONE
            binding.myFilterBlockRb.visibility = View.VISIBLE
            binding.myFilterReportRb.visibility = View.VISIBLE
        }
    }

    companion object {
        const val TAG = "MyFilterBottomModalSheet"
    }
}