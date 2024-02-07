package com.example.favoriteplace

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.ActivitySignupEmailInputBinding
import com.example.favoriteplace.databinding.ActivitySignupPwdInputBinding


class SignUpPwdInputFragment: Fragment() {

    lateinit var binding: ActivitySignupPwdInputBinding
//    private val auth = FirebaseAuth.getInstance()


    lateinit var email : ActivitySignupEmailInputBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ActivitySignupPwdInputBinding.inflate(inflater, container,false)

        email = ActivitySignupEmailInputBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.nextBtn.setOnClickListener {
            //비밀번호 포함 여부
        if(isPasswordValid(binding.pwdInputEt.text.toString())){
            //유효한 경우
            binding.pwdCheckedIv.visibility =View.VISIBLE
            binding.pwdErrorIv.visibility=View.GONE
            binding.pwdErrorTv.visibility=View.GONE

            Log.d("SignUp", "Value received: ${binding.pwdInputEt.text.toString()}")
        }else{
            binding.pwdCheckedIv.visibility =View.GONE
            binding.pwdErrorIv.visibility=View.VISIBLE
            binding.pwdErrorTv.visibility=View.VISIBLE
        }


            //비밀번호 일치 여부
            if(binding.pwdInputEt.text.toString()!=""){
                if(binding.pwdInputEt.text.toString().equals(binding.pwdReinputEt.text.toString())){
                    Log.d("SignUp", "Value received: 일치")
                    binding.repwdCheckedIv.visibility= View.VISIBLE
                    binding.repwdErrorIv.visibility=View.GONE

                    binding.nextBtn.text = "다음 단계"
                    binding.nextBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.main))

                    val email = arguments?.getString("email")
                    val pwd = binding.pwdInputEt.text.toString()

                    Log.d("SignUp", "Received Email: $email")
                    Log.d("SignUp", "Received Password: $pwd")

                    val bundle = Bundle().apply {
                        putString("email", email)
                        putString("pwd", pwd)
                    }


//                    val profileFragment = SignUpProfileSettingActivity()
//                    profileFragment.arguments=bundle
//
//                    parentFragmentManager.beginTransaction()
//                        .replace(R.id.main_frameLayout,profileFragment)
//                        .addToBackStack(null)
//                        .commit()

                    // 다음 단계 클릭 활성화
                    binding.nextBtn.setOnClickListener {
                        val intent = Intent(requireContext(), SignUpProfileSettingActivity::class.java)
                        startActivity(intent)
                    }



                }
            }else{
                Log.d("SignUp", "Value received: 불일치")
                binding.repwdCheckedIv.visibility= View.GONE
                binding.repwdErrorIv.visibility=View.VISIBLE
            }
        }




    }


    // password 유효성 검사
    fun isPasswordValid(password:String):Boolean{
        val passwordPattern = "^(?=.*[0-9])(?=.*[!@#\$%^&*()-+=]).{8,}\$".toRegex()
        return passwordPattern.matches(password)
    }

}