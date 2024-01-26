package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentRallyhomeBinding

class RallyHomeFragment : Fragment() {

    lateinit var binding: FragmentRallyhomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRallyhomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateLoginStatusView()

        binding.interestedRallyCl.setOnClickListener {
            showToast("로그인 후 이용 가능한 메뉴입니다.")
        }

        binding.certificationBoardCl.setOnClickListener {
            showToast("로그인 후 이용 가능한 메뉴입니다.")
        }
    }

    private fun updateLoginStatusView() {
        if (isLoggedIn()) {
            binding.rallyHomeYesLoginCl.visibility = View.VISIBLE
            binding.rallyHomeNotLoginCl.visibility = View.GONE
        } else {
            binding.rallyHomeYesLoginCl.visibility = View.GONE
            binding.rallyHomeNotLoginCl.visibility = View.VISIBLE
        }
    }

    private fun isLoggedIn(): Boolean {
        // 로그인 상태 확인 로직 구현
        // 예를 들어, SharedPreferences, 사용자 세션 확인 등
        return true // 임시로 true 반환
    }

    private fun showToast(message: String) {
        val layout = layoutInflater.inflate(R.layout.custom_toast, binding.root as ViewGroup, false)
        val textView: TextView = layout.findViewById(R.id.custom_toast_message)
        textView.text = message

        with(Toast(requireContext())) {
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }

}