package com.example.favoriteplace

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import com.example.favoriteplace.databinding.ActivityFreeWritePostBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

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

    private fun uploadPost() {
        val imageParts = selectedImages.map { uri ->
            val inputStream = contentResolver.openInputStream(uri)
            val requestBody = inputStream?.readBytes()?.toRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("images", "image.jpg", requestBody!!)
        }

        val title = "도쿄 성지순례 장소 추천해주세요 :)"
        val content = "이번 3월에 도쿄 여행을 계획 중인데, 도쿄 주변 가볼만한 혹은 꼭!! 가야만 하는 성지순례 장소있을까요? 애니메이션 장르 상관 없이 추천 부탁드려요😊"
        val postData = PostData(title, content)
        val accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0MzI5MjlAbmF2ZXIuY29tIiwiaWF0IjoxNzA3NzU3NzE5LCJleHAiOjE3MTAzNDk3MTl9.CHnXELf6b-vPC--rmZAnwRY6aAvUKt0iPy9Wq_1QYLo"
        val authorizationHeader = "Bearer $accessToken"

        postService.uploadPost(authorizationHeader, postData, imageParts)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // 업로드 성공
                        Log.d("FreeWritePostActivity", "Post upload successful")
                        // 업로드 성공 시에 수행할 작업을 여기에 추가합니다.
                    } else {
                        // 업로드 실패
                        // 실패 시에 수행할 작업을 여기에 추가합니다.
                        Log.d("FreeWritePostActivity", "API Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // 네트워크 오류 등으로 실패
                    // 실패 시에 수행할 작업을 여기에 추가합니다.
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
