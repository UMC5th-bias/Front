package com.example.favoriteplace

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentSignupProfileSettingBinding


class SignUpProfileSettingFragment: Fragment() {

    lateinit var binding: FragmentSignupProfileSettingBinding

    //사용자 갤러리에 -> 프로필 이미지 선택
    lateinit var profileImage : ImageView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignupProfileSettingBinding.inflate(inflater, container,false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.profileBackground.setOnClickListener {

        }


        binding.nextBtn.setOnClickListener {


            if(binding.nameTet.text.toString().isEmpty()){
                Log.d("SignUp","Value received : noInput name")
            }
            else{


                val email = arguments?.getString("email")
                val pwd = arguments?.getString("pwd")
                val nickname = binding.nameTet.text.toString()
                val introduction = binding.introductionTet.text.toString()


                Log.d("SignUp", "Received Email: $email")
                Log.d("SignUp", "Received Password: $pwd")
                Log.d("SignUp", "Received Nickname: $nickname")
                Log.d("SignUp", "Received Introduction: $introduction")


                val bundle = Bundle().apply {
                    putString("email", email)
                    putString("pwd", pwd)
                    putString("nickname", nickname)
                    putString("introduction", introduction)
                }



                val finishFragment = SignUpFinishFragment()
                finishFragment.arguments=bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frameLayout,finishFragment)
                    .addToBackStack(null)
                    .commit()

            }


        }



    }


}