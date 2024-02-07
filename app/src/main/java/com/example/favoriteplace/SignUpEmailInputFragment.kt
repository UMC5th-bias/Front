package com.example.favoriteplace

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentSignupEmailInputBinding


class SignUpEmailInputFragment: Fragment() {

    lateinit var binding: FragmentSignupEmailInputBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignupEmailInputBinding.inflate(inflater, container,false)

        val checkboxValue  = arguments?.getBoolean("checkbox", true)
        Log.d("SignUp", "Value received: $checkboxValue ")


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // 이메일 유효성 검사
        binding.emailCheckBtn.setOnClickListener {
            if(isEmailValid(binding.emailInputEt.text.toString())){

                binding.emailCheckBtn.visibility=View.GONE
                binding.codeBtn.visibility=View.VISIBLE

                binding.emailErrorIv.visibility=View.GONE
                binding.emailErrorTv.visibility=View.GONE
                binding.emailCheckedIv.visibility=View.VISIBLE


                binding.codeInputEt.visibility=View.VISIBLE




            }
            else{
                binding.emailErrorIv.visibility=View.VISIBLE
                binding.emailErrorTv.visibility=View.VISIBLE
            }


        }

        binding.codeBtn.setOnClickListener {
            val email  = binding.emailInputEt.text.toString()

            binding.nextBtn.visibility=View.VISIBLE
            binding.codeBtn.visibility = View.GONE

            binding.nextBtn.isEnabled=true
            binding.nextBtn.backgroundTintList=
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.main))

        }


        binding.nextBtn.setOnClickListener {
            val email  = binding.emailInputEt.text.toString()

            val bundle: Bundle = Bundle().apply {
                putString("email",email)
            }
            Log.d("SignUp", "email Response: $email")

            Log.d("SignUp", "-----------------")

            val pwdInputFragment = SignUpPwdInputFragment()
            pwdInputFragment.arguments=bundle
            Log.d("SignUp", "Value sent: $pwdInputFragment")


            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, pwdInputFragment)
                .addToBackStack(null)
                .commit()
        }


        //뒤로가기
        binding.signupEmailBackIv.setOnClickListener {
            val conditionFragment = SignUpConditionConfirmFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frameLayout, conditionFragment)
            transaction.addToBackStack(null)
            val currentFragment = parentFragmentManager.findFragmentById(R.id.main_frameLayout)
            currentFragment?.let { transaction.hide(it) }
            transaction.commit()
        }
    }






    // email 유효성 검사
    fun isEmailValid(email:String):Boolean{
        val emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$".toRegex()
        return emailPattern.matches(email)
    }


}

