package com.example.favoriteplace

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentSignupFinishBinding
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

class SignUpFinishFragment: Fragment() {

    lateinit var binding: FragmentSignupFinishBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignupFinishBinding.inflate(inflater, container,false)




        return binding.root
    }


    private var imageUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = arguments?.getString("email")
        val password = arguments?.getString("password")
        val nickname = arguments?.getString("nickname","아이")
        val introduction =arguments?.getString("introduction","한번 최애는 영원한 최애")

        Log.d("SignUp", "____________________________")

        Log.d("SignUp", "Received Nickname: $nickname")
        Log.d("SignUp", "Received Introduction: $introduction")

        val bundle = Bundle().apply {
            putString("email", email)
            putString("password", password)
            putString("nickname", nickname)
            putString("introduction", introduction)
        }


        binding.nicknameTv.text = nickname.toString()
        binding.introductionTv.text=introduction.toString()

        binding.saveBtn.setOnClickListener {
            val nickname = arguments?.getString("nickname", "최애의아이")
            val email = arguments?.getString("email", "aaa@naver.com")
            val password = arguments?.getString("password", "shdksfdkl")
            val snsAllow = arguments?.getBoolean("snsAllow", true)
            val introduction = arguments?.getString("introduction", "한줄소개입니다아~~")

            if(nickname.isNullOrEmpty() || introduction.isNullOrEmpty()){
                return@setOnClickListener
            }

            // 코루틴 범위 내에서 호출
            GlobalScope.launch {
                sendDataToServer(nickname, email, password, snsAllow, introduction,imageUri)
            }





        }


        //뒤로가기
        binding.backIv.setOnClickListener {
            val profileFragment = SignUpProfileSettingFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frameLayout, profileFragment)
            transaction.addToBackStack(null)
            val currentFragment = parentFragmentManager.findFragmentById(R.id.main_frameLayout)
            currentFragment?.let { transaction.hide(it) }
            transaction.commit()
        }
    }


    }


    fun prepareImagePart(fileUri: Uri, partName: String): MultipartBody.Part {
        val file = File(fileUri.path)
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }


    private suspend fun sendDataToServer(
        nickname: String,
        email: String?,
        password: String?,
        snsAllow: Boolean?,
        introduction: String,
        imageUri: Uri?
    ) {

        try {
//            val signUpService= RetrofitAPI.signUpService


            // Prepare user data
            val userData  = SignUpBody(
                nickname,
                email ?: "",
                password ?: "",
                snsAllow ?: true,
                introduction,
//                if (imageUri != null) listOf(prepareImagePart(imageUri, "images")) else null
                null
            )

            val userDataJson = Gson().toJson(userData)
            val userDataRequestBody = RequestBody.create("application/json".toMediaTypeOrNull(), userDataJson)

            // Prepare image parts
            val images: List<MultipartBody.Part> = if (imageUri != null) listOf(prepareImagePart(imageUri, "images")) else emptyList()



            // Retrofit call
//            val response = signUpService.addSignup(userDataRequestBody, images)

            // 서버 응답 처리
//            handleResponse(response)


        } catch (e:Exception){
            Log.d("SignUp", "Error sending data to server")
        }
    }


// Response를 처리하는 함수
    private fun handleResponse(response: Response<SignUpResponse>) {
        if (response.isSuccessful) {
            // 성공적인 응답 처리
            val signUpResponse = response.body()
            Log.d("SignUp", "Data sent successfully. Response: $signUpResponse")
        } else {
            // 실패한 응답 처리
            Log.d("SignUp", "Failed to send data. Response code: ${response.code()}, message: ${response.message()}")
        }
    }