package com.example.favoriteplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class DeleteDialogFragment : DialogFragment() {
    private var isPost: Boolean = false // 게시글 여부

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isPost = it.getBoolean("isPost", false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_community_delete, container, false)

        val messageTextView = view.findViewById<TextView>(R.id.dialog_community_delete_question_tv)
        val titleTextView = view.findViewById<TextView>(R.id.dialog_community_delete_title_tv)
        val cancelButton = view.findViewById<Button>(R.id.dialog_community_delete_no_btn)
        val confirmButton = view.findViewById<Button>(R.id.dialog_community_delete_yes_btn)

        // 타이틀 설정
        titleTextView.text = if (isPost) {
            "게시글 삭제하기"
        } else {
            "댓글 삭제하기"
        }

        // 메시지 설정
        messageTextView.text = if (isPost) {
            "해당 게시글을\n삭제하시겠어요?"
        } else {
            "해당 댓글을\n삭제하시겠어요?"
        }

        // 버튼 동작 설정
        cancelButton.setOnClickListener { dismiss() }
        confirmButton.setOnClickListener {
            if (isPost) {
                deletePost()
            } else {
                deleteComment()
            }
            dismiss()
        }

        return view
    }

    private fun deletePost() {
        // 게시글 삭제 로직
        Log.d("DeleteDialog", "게시글이 삭제되었습니다.")
    }

    private fun deleteComment() {
        // 댓글 삭제 로직
        Log.d("DeleteDialog", "댓글이 삭제되었습니다.")
    }

    companion object {
        fun newInstance(isPost: Boolean): DeleteDialogFragment {
            val fragment = DeleteDialogFragment()
            val args = Bundle().apply {
                putBoolean("isPost", isPost)
            }
            fragment.arguments = args
            return fragment
        }
    }
}