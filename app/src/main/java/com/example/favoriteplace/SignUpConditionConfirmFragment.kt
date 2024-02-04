package com.example.favoriteplace

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.favoriteplace.databinding.FragmentCommunityMainBinding
import com.example.favoriteplace.databinding.FragmentRallyplaceBinding
import com.example.favoriteplace.databinding.FragmentSignupConditionConfirmBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class SignUpConditionConfirmFragment: Fragment() {

    lateinit var binding: FragmentSignupConditionConfirmBinding

    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignupConditionConfirmBinding.inflate(inflater, container,false)

        bottomNavigationView = requireActivity().findViewById(R.id.main_bnv)
        bottomNavigationView.visibility=View.
        GONE



        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // 전체동의 클릭 시
        binding.checkbox1.setOnClickListener {
            val isChecked = binding.checkbox1.isChecked
            binding.checkbox2.isChecked = isChecked
            binding.checkbox3.isChecked = isChecked
            binding.checkbox4.isChecked = isChecked


            if (binding.checkbox2.isChecked || binding.checkbox3.isChecked){
                binding.nextBtn.isEnabled=true
                binding.nextBtn.backgroundTintList=ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.main))
                binding.nextBtn.setOnClickListener {
                    val emailFragment = SignUpEmailInputFragment()

                    val bundle=Bundle()
                    bundle.putBoolean("checkbox",true)
                    emailFragment.arguments=bundle
                    Log.d("SignUp", "Value sent: $bundle")


                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.main_frameLayout, emailFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
            } else{
                binding.nextBtn.isEnabled=false
            }
        }








    }



}

