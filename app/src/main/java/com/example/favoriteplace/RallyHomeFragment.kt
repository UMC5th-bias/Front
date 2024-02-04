package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteplace.databinding.FragmentRallydetailBinding
import com.example.favoriteplace.databinding.FragmentRallyhomeBinding

class RallyHomeFragment : Fragment() {

    lateinit var binding: FragmentRallyhomeBinding
    lateinit var rallybinding : FragmentRallydetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRallyhomeBinding.inflate(inflater, container, false)


        val interestedRallyItems = listOf(
            InterestedRallyItem("시간을 달리는 소녀", R.drawable.interested_rally_img),
            InterestedRallyItem("센과 치히로의 행방불명", R.drawable.interested_rally_img2),
            InterestedRallyItem("주술회전", R.drawable.interested_rally_img3)
        )

        // 데이터 리스트 생성
        val certificationRallyItems = listOf(
            CertifiedRallyItem(
                "날씨의 아이 덕후 투어 다녀왔어요:)",
                "#날씨의아이",
                "#도쿄",
                R.drawable.certificated_rally_img,
                "1시간 전"
            ),
            CertifiedRallyItem(
                "시타 가쿠슈인역에 다녀왔습니..",
                "#시간을달리는소녀",
                "#도쿄",
                R.drawable.certificated_rally_img2,
                "어제"
            )
            // 추가 데이터...
        )

        // 어댑터 생성 및 설정
        val certifiedAdapter = RallyHomeCertificationRVAdapter(certificationRallyItems)
        binding.rallyHomeCertificatedRallyRv.layoutManager = LinearLayoutManager(context)
        binding.rallyHomeCertificatedRallyRv.adapter = certifiedAdapter

        val interestedAdapter = RallyHomeInterestedRVAdapter(interestedRallyItems)
        binding.rallyHomeInterestedRallyRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rallyHomeInterestedRallyRv.adapter = interestedAdapter

        binding.rallyPlaceBlackBoxCl.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, RallyPlaceFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        binding.animationRallyBlackBoxCl.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, RallyCategoryFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        binding.recommendRallyCv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, RallyDetailFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateLoginStatusView()

        if (!isLoggedIn()) {
            binding.interestedRallyCl.setOnClickListener {
                showToast("로그인 후 이용 가능한 메뉴입니다.")
            }

            binding.certificationBoardCl.setOnClickListener {
                showToast("로그인 후 이용 가능한 메뉴입니다.")
            }
        }
    }

    private fun updateLoginStatusView() {
        if (isLoggedIn()) {
            binding.interestedRallyYesLoginCl.visibility = View.VISIBLE
            binding.interestedRallyNotLoginCl.visibility = View.GONE
            binding.certificatedRallyYesLoginCl.visibility = View.VISIBLE
            binding.certificatedRallyNotLoginCl.visibility = View.GONE
        } else {
            binding.interestedRallyYesLoginCl.visibility = View.GONE
            binding.interestedRallyNotLoginCl.visibility = View.VISIBLE
            binding.certificatedRallyYesLoginCl.visibility = View.GONE
            binding.certificatedRallyNotLoginCl.visibility = View.VISIBLE
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