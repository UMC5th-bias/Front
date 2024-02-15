package com.example.favoriteplace

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import com.example.favoriteplace.databinding.ActivityFreeWritePostBinding
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

class FreeWritePostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFreeWritePostBinding
    private lateinit var postService: PostService

    private val REQUEST_CODE_GALLERY = 100
    private val REQUEST_IMAGE_CAPTURE = 1
    private val selectedImages = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFreeWritePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postService = RetrofitClient.postService

        binding.writePostGalleryIv.setOnClickListener {
            openGallery()
        }

        binding.writePostCameraIv.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_IMAGE_CAPTURE
                )
            } else {
                dispatchTakePictureIntent()
            }
        }

        binding.writePostArrowIv.setOnClickListener {
            finish()
        }

        binding.writePostRegisterBtn.setOnClickListener {
            uploadPost()
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_IMAGE_CAPTURE && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            dispatchTakePictureIntent()
        }
    }

    private fun logSelectedImages() {
        selectedImages?.let { images ->
            for ((index, uri) in images.withIndex()) {
                Log.d("FreeWritePostActivity", "Selected image $index: $uri")
            }
            if (images.isEmpty()) {
                Log.d("FreeWritePostActivity", "No images selected")
            }
        } ?: run {
            Log.d("FreeWritePostActivity", "selectedImages is null")
        }
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
                    if (i == 4) break // 최대 5개의 이미지만 허용
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

    private fun addImageToLayout(uri: Uri) {
        if (selectedImages.size >= 5) return // 최대 5개의 이미지만 허용

        val frameLayout = FrameLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginEnd = 10
            }
        }

        val imageView = ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(100, 100)
            scaleType = ImageView.ScaleType.CENTER_CROP
            setImageURI(uri)
        }

        val deleteButton = ImageButton(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                50, // 버튼 크기 조정
                50
            ).apply {
                gravity = Gravity.TOP or Gravity.END
                setMargins(10)
            }
            setImageResource(R.drawable.ic_delete_btn) // 커스텀 이미지 사용
            background = null // 배경을 투명하게 설정
            setOnClickListener {
                removeImageFromLayout(frameLayout, uri)
            }
        }

        frameLayout.addView(imageView)
        frameLayout.addView(deleteButton)
        binding.imageContainer.addView(frameLayout)
        selectedImages.add(uri)
    }

    // 이미지 Uri와 Context를 받아 해당 이미지의 크기를 확인하는 함수
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


    private fun getAccessToken(): String? {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences?.getString(LoginActivity.ACCESS_TOKEN_KEY, null)
    }

    private fun urisToMultipartBodyParts(uris: List<Uri>, contentResolver: ContentResolver): List<MultipartBody.Part> {
        return uris.mapNotNull { uri ->
            // ContentResolver를 사용하여 Uri에서 InputStream을 얻음
            contentResolver.openInputStream(uri)?.use { inputStream ->
                // InputStream에서 바이트 배열을 읽음
                val byteArray = inputStream.readBytes()
                // 바이트 배열을 사용하여 RequestBody 생성
                val requestBody = byteArray.toRequestBody("image/*".toMediaTypeOrNull())
                // 파일 이름을 생성. 실제 앱에서는 더 의미 있는 이름을 생성할 필요가 있을 수 있음
                val filename = "file_${System.currentTimeMillis()}.jpg"

                // MultipartBody.Part 생성 및 반환
                MultipartBody.Part.createFormData("images", filename, requestBody)
            }
        }
    }

    // Retrofit 호출에 사용할 이미지 파트 리스트 생성
    private fun createImageParts(): List<MultipartBody.Part> {
        return urisToMultipartBodyParts(selectedImages, contentResolver)
    }

    private fun uploadPost() {

        val title = binding.writePostTitleEdt.text.toString().trim()
        val content = binding.writePostContentEdt.text.toString().trim()

        // 제목, 내용 비워져 있으면-> toast 메세지
        if (title.isEmpty() || content.isEmpty()) {
            val inflater = LayoutInflater.from(this)
            val customToastView = inflater.inflate(R.layout.custom_toast, null)
            val textViewMessage: TextView = customToastView.findViewById(R.id.custom_toast_message)
            textViewMessage.text = "제목과 내용을 입력해주세요."

            val toast = Toast(applicationContext)
            toast.duration = Toast.LENGTH_SHORT
            toast.view = customToastView
            toast.show()

            return
        }


        logSelectedImages()

        // 이미지 크기 검사
        for (uri in selectedImages) {
            if (!checkImageSize(this, uri)) {
                // 이미지 크기가 4MB를 초과하는 경우, 사용자에게 알림
                Toast.makeText(this, "모든 이미지는 4MB 미만이어야 합니다.", Toast.LENGTH_SHORT).show()
                return // 업로드 중단
            }
        }

        // 게시글 데이터를 JSON으로 변환
        val postData = PostData(title, content)
        // 게시글 데이터를 JSON 문자열로 변환
        val postJson = Gson().toJson(postData)
        // JSON 문자열을 RequestBody로 변환
        val postRequestBody = postJson.toRequestBody("application/json".toMediaTypeOrNull())
        Log.d("FreeWritePostActivity", "postJson : $postJson")

        // 이미지 파트 생성
        val imageParts = createImageParts()
        Log.d("FreeWritePostActivity", "ImageParts : $imageParts")

        // AccessToken 가져오기
        val accessToken = getAccessToken()
        // 헤더에 AccessToken 추가
        val authorizationHeader = "Bearer $accessToken"

        postService.uploadPost(authorizationHeader, postRequestBody, imageParts)
            .enqueue(object : Callback<PostResponse> {
                override fun onResponse(
                    call: Call<PostResponse>,
                    response: Response<PostResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseData = response.body()
                        if (responseData != null) {
                            Log.d("FreeWritePostActivity", "게시글을 성공적으로 등록했습니다. 메시지: ${response.body()?.message}")

                        }
                    } else {
                        Log.e("FreeWritePostActivity", "실패: ${response.body()?.message}")
                        Log.d("FreeWritePostActivity", "API Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    Log.d("FreeWritePostActivity", "Network Error: ${t.message}")
                }
            })
    }

    private fun removeImageFromLayout(frameLayout: FrameLayout, uri: Uri) {
        binding.imageContainer.removeView(frameLayout)
        selectedImages.remove(uri)
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }
}
