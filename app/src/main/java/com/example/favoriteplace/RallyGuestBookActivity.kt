package com.example.favoriteplace

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide

import com.example.favoriteplace.databinding.FragmentRallyGuestbookBinding
import com.naver.maps.geometry.LatLng
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

class RallyGuestBookActivity : AppCompatActivity() {
    private lateinit var binding : FragmentRallyGuestbookBinding
    private lateinit var retrofit: Retrofit
    private lateinit var rallyGuestBookService: RallyGuestBookService
    lateinit var rallyLocationDetailService: RallyLocationDetailService

    private var rallyAnimationId: Long = -1 // rallyAnimationId 인스턴스 변수 추가
    private lateinit var sharedPreferences: SharedPreferences


    private val REQUEST_CODE_GALLERY = 100
    private val REQUEST_IMAGE_CAPTURE = 1

    private val selectedImages = mutableListOf<Uri>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentRallyGuestbookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardview.visibility = View.VISIBLE

        val rallyAnimationId = intent.getLongExtra("rallyAnimationId", -1)

        // Retrofit 객체 생성
        retrofit = Retrofit.Builder()
            .baseUrl("http://favoriteplace.store:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        rallyGuestBookService = retrofit.create(RallyGuestBookService::class.java)
        rallyLocationDetailService = retrofit.create(RallyLocationDetailService::class.java)


        binding.openGalleryIb.setOnClickListener {
            openGallery()
            Log.d("openGallery()", "사진 선택 완료!")
            binding.cardview.visibility = View.GONE
        }


        binding.guestbookUploadBtn.setOnClickListener {
            uploadPost()
        }


    }

    private fun uploadPost() {
        val title = binding.guestbookTitleEt.text.toString().trim()
        val content = binding.guestbookContentEt.text.toString().trim()
        val hashtags = binding.rallyGuestbookTag1Et.toString()


        // 제목, 내용 비워져 있으면-> toast 메세지
        if (title.isEmpty() || content.isEmpty()) {
            showToast(this, "제목과 내용을 입력해주세요.")
            return
        }

        // pilgrimageId를 여기에서 설정
        val pilgrimageId: Long = 1 // 예시로 설정


        val jsonObject = JSONObject().apply {
            put("title", title)
            put("content", content)
            put("hashtags", JSONArray(hashtags))
        }
        val jsonRequestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())


        // 이미지 크기 검사
        for (uri in selectedImages) {
            if (!checkImageSize(this, uri)) {
                // 이미지 크기가 4MB를 초과하는 경우, 사용자에게 알림
                Toast.makeText(this, "모든 이미지는 4MB 미만이어야 합니다.", Toast.LENGTH_SHORT).show()
                return // 업로드 중단
            }
        }


        // 이미지 Uri를 MultipartBody.Part로 변환
        val imageParts = selectedImages.mapNotNull { uri ->
            uriToMultipartBodyPart(uri, "images")
        }


        // 헤더에 AccessToken 추가
        val authorizationHeader = "Bearer ${getAccessToken()}"
        Log.e("RallyGuestBookActivity", ">> $authorizationHeader")


        if (selectedImages.isNotEmpty()) {
            uploadPostRequest(authorizationHeader, jsonRequestBody, imageParts, pilgrimageId)
        } else {
            uploadPostRequest(authorizationHeader,jsonRequestBody, emptyList(), pilgrimageId)
        }


    }

    private fun getAccessToken(): String? {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences?.getString(LoginActivity.ACCESS_TOKEN_KEY, null)
    }

    private fun uriToMultipartBodyPart(uri: Uri, name: String): MultipartBody.Part? {
        contentResolver.openInputStream(uri)?.use { inputStream ->
            val byteArray = inputStream.readBytes()
            val requestFile = byteArray.toRequestBody(
                contentResolver.getType(uri)?.toMediaTypeOrNull(), 0, byteArray.size
            )
            return MultipartBody.Part.createFormData(name, File(uri.path).name, requestFile)
        }
        return null
    }

    private fun uploadPostRequest(
        authorizationHeader: String,
        jsonRequestBody: RequestBody,
        imageParts: List<MultipartBody.Part>,
        pilgrimageId:Long) {

        rallyGuestBookService.guestBookUploadPost(authorizationHeader,jsonRequestBody,imageParts,pilgrimageId)
            .enqueue(object :Callback<RallyGuestBookService.GuestbookResponse>{
                override fun onResponse(
                    call: Call<RallyGuestBookService.GuestbookResponse>,
                    response: Response<RallyGuestBookService.GuestbookResponse>
                ) {
                    if(response.isSuccessful){
                        val responseData = response.body()
                        if(responseData!=null){
                            Log.d("RallyGuestBookActivity", " 게시글을 성공적으로 등록했습니다. \n" +
                                    "메시지: ${response.body()?.message}")

                            val intent = Intent(this@RallyGuestBookActivity, MyGuestBookActivity::class.java)
                            // 여기에 게시글 ID 등의 데이터를 전달할 수 있음
                            startActivity(intent)

                            // 현재 액티비티를 종료
                            finish()
                        }
                        else{
                            Log.e("RallyGuestBookActivity", "API Error: 실패")
                        }
                    }
                }

                override fun onFailure(
                    call: Call<RallyGuestBookService.GuestbookResponse>,
                    t: Throwable
                ) {
                    Log.e("RallyGuestBookActivity", "Network Error: ${t.message}")
                }
            })
    }


    private fun fetchRallyInfo(rallyAnimationId: Long){
        //토큰 유효성
        val token = sharedPreferences.getString("token", null)
        val rallyAnimationId = intent.getLongExtra("rallyAnimationId", -1)

        val call = rallyLocationDetailService.getRallyInfo("Bearer $token",rallyAnimationId.toLong())
        Log.d("RallyGuestBookActivity", ">> Bearer : $token ")

        call.enqueue(object : Callback<RallyLocationDetailService.RallyInfo> {
            override fun onResponse(
                call: Call<RallyLocationDetailService.RallyInfo>,
                response: Response<RallyLocationDetailService.RallyInfo>
            ) {
                if(response.isSuccessful){
                    val rallyInfo=response.body()
                    if (rallyInfo != null) {
                        Log.d("RallyGuestBookActivity", ">> rallyInfo: $rallyInfo")
                        rallyInfo.let {
                            Log.e("RallyGuestBookActivity", ">> displayRallyInfo() 성공 ")
                            displayRallyInfo(rallyInfo)

                        }
                    }else{
                        // 응답 바디가 null인 경우
                        Log.e("RallyGuestBookActivity", "Response body is null")
                    }

                }else{
                    // 응답 실패
                    Log.e("RallyGuestBookActivity", "RallyLocationDetail failed with error code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RallyLocationDetailService.RallyInfo>, t: Throwable) {
                Log.d("RallyGuestBookActivity", ">> server error")
            }
        })
    }

    private fun displayRallyInfo(rallyInfo: RallyLocationDetailService.RallyInfo) {
        binding.rallyGuestbookNameTv.text = rallyInfo.rallyName
        binding.rallyGuestbookCheckTv.text= rallyInfo.myPilgrimageNumber.toString()
        binding.rallyGuestbookTotalTv.text = rallyInfo.pilgrimageNumber.toString()

        Glide.with(this)
            .load(rallyInfo.image)
            .into(binding.rallyLocationdetailPlaceIv)

    }

    fun checkImageSize(context: Context, imageUri: Uri): Boolean {
        val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
        val imageSize = inputStream?.available() ?: 0

        // 4MB 미만인지 확인
        if (imageSize > 4 * 1024 * 1024) {
            // 이미지 크기가 4MB를 초과하는 경우
            return false
        }
        return true
    }

    fun showToast(context: Context, message: String) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.custom_toast, null)

        val textView = layout.findViewById<TextView>(R.id.custom_toast_message)
        textView.text = message

        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        startActivityForResult(intent, REQUEST_CODE_GALLERY)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
            data?.clipData?.let { clipData ->
                for (i in 0 until clipData.itemCount) {
                    val imageUri = clipData.getItemAt(i).uri
                    addImageToLayout(imageUri)
                    if (i == 0) break // 최대 1개의 이미지만 허용
                }
            } ?: data?.data?.let { uri ->
                addImageToLayout(uri)
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                val uri = getImageUri(this, it)
                addImageToLayout(uri)

            }
        }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun addImageToLayout(uri: Uri) {
        if (selectedImages.size >= 1) return // 최대 1개의 이미지만 허용

        val frameLayout = FrameLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginEnd = 10
            }
        }

        val imageView = ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(800, 1000)
            setImageURI(uri)
        }


        frameLayout.addView(imageView)
        binding.framelayout.addView(frameLayout)
        selectedImages.add(uri)
    }
}