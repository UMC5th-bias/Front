package com.example.favoriteplace

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.favoriteplace.databinding.DialogRallylocationDetailBinding

class RallyLocationDialog(private val nickname: String, private val rallyAnimationId: Long) : DialogFragment() {

    private lateinit var binding : DialogRallylocationDetailBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogRallylocationDetailBinding.inflate(inflater,container,false)
        val view = binding.root



        //팝업창 모서리 둥글게 만들기
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        // "다음에 쓸게요" 버튼 클릭 시 팝업창 닫기
        binding.dialogNextBtn.setOnClickListener {
            dismiss()
        }

        // "바로 쓰러가기" 버튼 클릭 시 이동
        binding.dialogNowBtn.setOnClickListener {
            Log.d("rallyAnimationId", ">> $rallyAnimationId")
            Toast.makeText(context,"바로 쓰러가기 클릭 ",Toast.LENGTH_SHORT).show()
            dismiss()

            val intent = Intent(requireContext(), RallyGuestBookActivity::class.java)
            intent.putExtra("rallyAnimationId", rallyAnimationId)
            startActivity(intent)
        }

        // 닉네임 설정
        setNickname()


       return view
    }

    fun setNickname() {
        if (::binding.isInitialized) {
            binding.dialogNicknameTv.text = nickname
        } else {
            Log.e("RallyLocationDialog", "Binding is not initialized")
        }
    }



}