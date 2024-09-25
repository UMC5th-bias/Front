package com.example.favoriteplace

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
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
import java.io.InputStream


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

        // 뒤로가기
        binding.backIv.setOnClickListener {
            val intent = Intent(this@SignUpFinishActivity, SignUpProfileSettingActivity::class.java)
            startActivity(intent)
            finish()
        }


        // 이전 화면에서 전달받은 데이터 가져오기
        val snsAllow = intent.getBooleanExtra("snsAllow", false)
        val email = intent.getStringExtra("email") ?: ""
        val password = intent.getStringExtra("password") ?: ""
        val nickname = intent.getStringExtra("nickname") ?: ""
        val introduction = intent?.getStringExtra("introduction") ?: ""
        // 이미지 파일의 경로를 받아옴
        val imageFilePath: String? = intent.getStringExtra("imageFilePath")
        Log.d("Sign up", ">> Image File Path : $imageFilePath")


        intent.putExtra("nickname", nickname)
        intent.putExtra("introduction", introduction)
        intent.putExtra("imageUri",imageUri)


        Log.d("SignUp", ">> finish >> { $snsAllow, $email, $password, $nickname, $introduction, $imageUri }")


        binding.nicknameTv.text = nickname
        binding.introductionTv.text = introduction


        Glide.with(this)
            .load(imageUri)
            .placeholder(null)
            .into(binding.finishProfileIv)

        if (!imageFilePath.isNullOrEmpty()) {
            // 이미지 파일 경로가 있는 경우, 해당 경로를 사용하여 이미지를 로드 또는 표시
            // 예를 들어, Glide를 사용하여 이미지를 로드
            Glide.with(this)
                .load(imageFilePath)
                .into(binding.finishProfileIv)
        } else {
            // 이미지 파일 경로가 없는 경우, 기본 이미지를 로드하거나 표시
            // 예를 들어, 기본 이미지를 설정
            binding.finishProfileIv.setImageResource(R.drawable.memberimg)
        }


        Log.d("SignUp", ">> Glide Image success")



        // 이미지 URI가 null이 아닌 경우에만 Glide를 사용하여 이미지 표시
//        if (imageUri != null) {
//            Log.d("SignUp", ">> Image URI: $imageUri")
//
//            Glide.with(this)
//                .load(imageUri)
//                .skipMemoryCache(true)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .override(150, 150)
//                .into(binding.finishProfileIv)
//            Log.d("SignUp", ">> Glide Image success")
//        } else {
//            Log.d("SignUp", ">> Image URI is null")
//        }


        binding.saveBtn.setOnClickListener {

            // SignUpService.SignUpRequest 객체 생성
            val signUpRequest = SignUpService.SignUpRequest(nickname, email, password, snsAllow, introduction)
            // JSON 데이터를 RequestBody로 변환
            val jsonRequestBody = Gson().toJson(signUpRequest).toRequestBody("application/json".toMediaTypeOrNull())
            // 이미지 URI를 MultipartBody.Part로 변환
            val imagePart = uriToMultipartBodyPart(imageFilePath)

            // 이미지가 null이 아닌 경우에만 서버에 전송
            if (imagePart != null) {
                sendSignUpRequest(jsonRequestBody, imagePart)
            } else {
                // 이미지가 null일 때도 서버에 null을 전송
                sendSignUpRequest(jsonRequestBody, null)
            }
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
                            finish() // 현재 화면 종료
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

    private fun uriToMultipartBodyPart(imageFilePath: String?): MultipartBody.Part? {
        // 이미지 파일 경로가 null이 아니고 빈 문자열이 아닌 경우에만 처리
        if (!imageFilePath.isNullOrEmpty()) {
            val file = File(imageFilePath)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            return MultipartBody.Part.createFormData("images", file.name, requestFile)
        }
        return null
    }

}