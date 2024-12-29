package com.example.favoriteplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class NotifyDialogFragment : DialogFragment() {
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
        val view = inflater.inflate(R.layout.dialog_community_notify, container, false)

        val cancelButton = view.findViewById<Button>(R.id.dialog_community_delete_no_btn)
        val confirmButton = view.findViewById<Button>(R.id.dialog_community_delete_yes_btn)

        // 버튼 동작 설정
        cancelButton.setOnClickListener { dismiss() }
        confirmButton.setOnClickListener {
            notifyUser()
            dismiss()
        }

        return view
    }

    private fun notifyUser() {
        // 사용자 신고 로직
        Log.d("NotifyDialog", "신고가 완료됐습니다.")
    }

}