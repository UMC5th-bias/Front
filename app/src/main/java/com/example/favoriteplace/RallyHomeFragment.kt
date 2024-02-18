package com.example.favoriteplace

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.favoriteplace.databinding.FragmentRallydetailBinding
import com.example.favoriteplace.databinding.FragmentRallyhomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RallyHomeFragment : Fragment() {

    lateinit var binding: FragmentRallyhomeBinding
    private var userToken: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRallyhomeBinding.inflate(inflater, container, false)


        // searchEditText에 검색어 입력 후 drawableEnd로 설정되있는 이미지 버튼을 누르면 서버에서 정보를 가져와 해당 검색 결과를 반환
        binding.searchIv.setOnClickListener {
            
            val keyword = binding.searchEt.text.toString()
            if(keyword.isNotEmpty()){
                searchRally(keyword)
            }else{
                showToast("검색어를 입력하세요.")
            }
        }

        fun checkLoginStatus() {
            // SharedPreferences에서 액세스 토큰 가져오기
            val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            var isLoggedIn = false
            isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", true)

            if (isLoggedIn) {
                // 로그인 상태인 경우 사용자 정보를 가져옴
                userToken = sharedPreferences.getString(LoginActivity.ACCESS_TOKEN_KEY, "") ?: ""
                if (userToken.isNotEmpty()) {
                    Log.d("HomeFragment", ">> 로그인 상태인 경우 사용자 정보를 가져옴, $userToken")
                }
            }else{
                // 비회원 상태인 경우
                Log.d("HomeFragment", ">> 비회원 상태입니다., $isLoggedIn")

            }
        }

        //유저 인증상태 가져오기
        checkLoginStatus()

        binding.rallyPlaceBlackBoxCl.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, RallyPlaceFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        binding.animationRallyBlackBoxCl.setOnClickListener {
            if(userToken == "") {
                Toast.makeText(context, "로그인 후 이용 가능한 메뉴입니다.", Toast.LENGTH_SHORT).show()
            }
            else {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frameLayout, RallyCategoryFragment())
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
            }
        }

        fun setTrendingRally(rallyHomeTrending: RallyHomeTrending) {
            binding.recommendRallyTitleTv.text = rallyHomeTrending.name
            binding.recommendRallyProgressTv.text = "${rallyHomeTrending.myPilgrimageNumber}/${rallyHomeTrending.pilgrimageNumber}"
            Glide.with(this)
                .load(rallyHomeTrending.image)
                .into(binding.recommendRallyIv)

            //랠리 id전달
            val rallyDetailFragment = RallyDetailFragment()
            val bundle = Bundle().apply {
                putString("rallyId", rallyHomeTrending.id.toString())
            }
            rallyDetailFragment.arguments = bundle
            //렐리 상세페이지로 이동
            binding.recommendRallyCv.setOnClickListener {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frameLayout, rallyDetailFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
            }
        }

        fun setMyRally(rallyHomeResponseMyRally: RallyHomeResponseMyRally) {
            // 관심있는 렐리 모아보기 데이터 연동
            val interestedRallyItems = mutableListOf<InterestedRallyItem>()
            rallyHomeResponseMyRally.likedRally.forEach {
                interestedRallyItems.add(InterestedRallyItem(it.name, it.image))
            }
            //데이터 없을 때 에러메세지
            if(interestedRallyItems.isEmpty()) binding.interestedRallyNotLoginCl.visibility = View.VISIBLE
            else binding.interestedRallyNotLoginCl.visibility = View.GONE

            val interestedAdapter = RallyHomeInterestedRVAdapter(interestedRallyItems, context as MainActivity)
            binding.rallyHomeInterestedRallyRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.rallyHomeInterestedRallyRv.adapter = interestedAdapter

            // 내 성지순례 인증글 모아보기 데이터 연동
            // 데이터 리스트 생성
            val certificationRallyItems = mutableListOf<CertifiedRallyItem>()
            rallyHomeResponseMyRally.guestBook.forEach {
                certificationRallyItems.add(
                    CertifiedRallyItem(
                        title = it.title,
                        tag1 = if(it.hashTag.size >= 1) it.hashTag[0] else "",
                        tag2 = if(it.hashTag.size >= 2) it.hashTag[1] else "",
                        imageResId = it.image,
                        time = it.createdAt

                    )
                )
            }
            //데이터 없을 때 에러메세지
            if(certificationRallyItems.isEmpty()) binding.certificatedRallyNotLoginCl.visibility = View.VISIBLE
            else binding.certificatedRallyNotLoginCl.visibility = View.GONE

            // 어댑터 생성 및 설정
            val certifiedAdapter = RallyHomeCertificationRVAdapter(certificationRallyItems, context as MainActivity)
            binding.rallyHomeCertificatedRallyRv.layoutManager = LinearLayoutManager(context)
            binding.rallyHomeCertificatedRallyRv.adapter = certifiedAdapter
        }

        //이달의 추천 랠리 불러오기
        RetrofitAPI.rallyHomeService.getTrending("Bearer $userToken").enqueue(object: Callback<RallyHomeTrending> {
            override fun onResponse(call: Call<RallyHomeTrending>, response: Response<RallyHomeTrending>) {
                if(response.isSuccessful) {
                    val responseData = response.body()
                    if(responseData != null) {
                        Log.d("Retrofit:getTrending()", "Response: ${responseData}")
                        setTrendingRally(responseData)
                    }
                }
                else {
                    Log.e("Retrofit:getTrending()", "notSuccessful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RallyHomeTrending>, t: Throwable) {
                Log.e("Retrofit:getTrending()", "onFailure: $t")
            }

        })

        //관심있는 랠리, 성지순레 인증글 모아보기 불러오기
        RetrofitAPI.rallyHomeService.getMyRally("Bearer $userToken").enqueue(object: Callback<RallyHomeResponseMyRally> {
            override fun onResponse(call: Call<RallyHomeResponseMyRally>, response: Response<RallyHomeResponseMyRally>) {
                if(response.isSuccessful) {
                    val responseData = response.body()
                    if(responseData != null) {
                        Log.d("Retrofit:getMyRally()", "Response: ${responseData}")
                        setMyRally(responseData)
                    }
                }
                else {
                    Log.e("Retrofit:getMyRally()", "notSuccessful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RallyHomeResponseMyRally>, t: Throwable) {
                Log.e("Retrofit:getMyRally()", "onFailure: $t")
            }

        })

        return binding.root
    }

    private fun searchRally(keyword: String) {

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