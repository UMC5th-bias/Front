package com.example.favoriteplace

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.favoriteplace.databinding.ActivitySignupFinishBinding
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


class SignUpFinishActivity: AppCompatActivity() {

    lateinit var binding: ActivitySignupFinishBinding
    lateinit var retrofit: Retrofit
    lateinit var signUpService: SignUpService

    private var imageUri: Uri? = null

    companion object {
        private const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 100
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("SignUp", ">> Starting SignUpFinishActivity")


        // Retrofit 객체 생성
        retrofit = Retrofit.Builder()
            .baseUrl("http://favoriteplace.store:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        signUpService = retrofit.create(SignUpService::class.java)

        // 저장소 권한 확인
        checkStoragePermission()


        // 이전 화면에서 전달받은 데이터 가져오기
        val snsAllow = intent.getBooleanExtra("snsAllow", false)
        val email = intent.getStringExtra("email") ?: ""
        val password = intent.getStringExtra("password") ?: ""
        val nickname = intent.getStringExtra("nickname") ?: ""
        val introduction = intent?.getStringExtra("introduction") ?: ""
        val imageUri: Uri? = intent.getParcelableExtra("imageUri")

        Log.d("SignUp", ">> finish >> { $snsAllow, $email, $password, $nickname, $introduction, $imageUri }")


        binding.nicknameTv.text = nickname
        binding.introductionTv.text = introduction


        binding.saveBtn.setOnClickListener {

            // SignUpService.SignUpRequest 객체 생성
            val signUpRequest = SignUpService.SignUpRequest(nickname, email, password, snsAllow, introduction)
            // JSON 데이터를 RequestBody로 변환

            val jsonRequestBody = Gson().toJson(signUpRequest).toRequestBody("application/json".toMediaTypeOrNull())


            // 이미지 URI를 MultipartBody.Part로 변환
            val imagePart = imageUri?.let { uriToMultipartBodyPart(it) }

            // 이미지가 null이 아닌 경우에만 서버에 전송
            if (imagePart != null) {
                sendSignUpRequest(jsonRequestBody, imagePart)
            } else {
                // 이미지가 null일 때도 서버에 null을 전송
                sendSignUpRequest(jsonRequestBody, null)
            }
        }
    }

    private fun checkStoragePermission() {
        // 외부 저장소 읽기 권한 확인
        if(ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_MEDIA_IMAGES
        ) != PackageManager.PERMISSION_GRANTED){

            // 권한이 없는 경우 권한 요청
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                READ_EXTERNAL_STORAGE_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== READ_EXTERNAL_STORAGE_REQUEST_CODE){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //권한이 부여된 경우 이미지 표시
                imageUri?.let {
                    Glide.with(this)
                        .load(it)
                        .into(binding.finishProfileIv)
                }
            }
        }
    }



        private fun sendSignUpRequest(signUpRequest: RequestBody, imagePart: MultipartBody.Part?) {
        signUpService.signUp(signUpRequest, imagePart?.let { listOf(it) } ?: emptyList())
            .enqueue(object : Callback<SignUpService.SignUpResponse?> {
                override fun onResponse(
                    call: Call<SignUpService.SignUpResponse?>,
                    response: Response<SignUpService.SignUpResponse?>
                ) {
                    if (response.isSuccessful) {
                        val responseData = response.body()
                        if (responseData != null) {
                            Log.d("SignUp", ">> 회원가입 성공!")
                            val intent = Intent(this@SignUpFinishActivity, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.d("SignUp", ">> 회원가입 실패: $errorBody")
                    }
                }

                override fun onFailure(call: Call<SignUpService.SignUpResponse?>, t: Throwable) {
                    Log.d("SignUp", ">> 회원가입 연결 오류", t)
                }
            })
    }

    private fun uriToMultipartBodyPart(uri: Uri): MultipartBody.Part {
        // URI를 파일 경로로 변환하여 RequestBody로 생성
        val file = File(uri.path ?: "")
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())

        // 이미지 파일의 이름을 파라미터로 지정하여 MultipartBody.Part 생성
        return MultipartBody.Part.createFormData("images", file.name, requestFile)
    }

}