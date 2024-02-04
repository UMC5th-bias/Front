package com.example.favoriteplace


import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.favoriteplace.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    //login
    private val userViewModel by viewModels<UserViewModel>()

    companion object{
        const val LOGIN_REQUEST_CODE=101
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)



        //광고 배너
        val bannerAdapter = BannerVPAdapter(this)
        binding.homeBannerVp.adapter=bannerAdapter
        binding.homeBannerVp.orientation=ViewPager2.ORIENTATION_HORIZONTAL
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_banner1))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_banner1))




        //로그인 버튼
        binding.homeLoginBtn.setOnClickListener {
//            val intent = Intent(requireActivity(), LoginActivity::class.java)
//            try {
//                startActivityForResult(intent, LOGIN_REQUEST_CODE)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }


            // SharedPreferences에서 토큰 읽어오기
//            val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//            val token = sharedPreferences.getString("token", "")
//
//            // 토큰이 비어있다면 로그인되지 않은 상태로 처리
//            if (token.isNullOrEmpty()) {
//                binding.userLayout.visibility = View.GONE
//                binding.unUserLayout.visibility = View.VISIBLE
//            } else {
//                // 토큰이 존재한다면 로그인된 상태로 처리
//                binding.userLayout.visibility = View.VISIBLE
//                binding.unUserLayout.visibility = View.GONE
//            }
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        return binding.root
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
//            // 로그인이 성공한 경우
//            showLoggedInLayout()
//        }
//    }


//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//
////        if(isUserLoggedIn()){
////            showLoggedInLayout()
////        }else{
////            showNonLoggedInLayout()
////        }
//    }

    private fun isUserLoggedIn(): Boolean {
        return userViewModel.isLogin || checkLogin()
    }

    private fun checkLogin(): Boolean {
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        return !token.isNullOrBlank()
    }


    private fun showLoggedInLayout() {
         binding.userLayout.visibility = View.VISIBLE
         binding.unUserLayout.visibility = View.GONE
    }

    private fun showNonLoggedInLayout() {
         binding.userLayout.visibility = View.GONE
         binding.unUserLayout.visibility = View.VISIBLE
    }



}