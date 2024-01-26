package com.example.favoriteplace

import android.app.Activity
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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

class FreeWritePostActivity : AppCompatActivity() {
    lateinit var binding: ActivityFreeWritePostBinding

    private val REQUEST_CODE_GALLERY = 100
    private val REQUEST_IMAGE_CAPTURE = 1
    private val selectedImages = mutableListOf<Uri>()
    private val selectedBitmaps = mutableListOf<Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFreeWritePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(this.packageManager)?.also {
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

        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한이 부여되었으면 카메라 앱 시작
                    dispatchTakePictureIntent()
                }
            }
            // 필요한 경우 다른 requestCode에 대한 처리 추가
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
            val clipData = data?.clipData
            if (clipData != null) {
                // 여러 이미지 처리
                for (i in 0 until clipData.itemCount) {
                    val imageUri = clipData.getItemAt(i).uri
                    addImageToLayout(imageUri)
                    if (i == 4) break // 최대 5개의 이미지만 허용
                }
            } else {
                // 단일 이미지 처리
                data?.data?.let { uri ->
                    addImageToLayout(uri)
                }
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // 결과 데이터에서 Bitmap 추출
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                addImageToLayout(it)
            }
        }
    }

    private fun addImageToLayout(uri: Uri) {
        if (selectedImages.size >= 5) return // 이미 5개의 이미지가 선택된 경우 추가하지 않음

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

    private fun addImageToLayout(bitmap: Bitmap) {
        if (selectedBitmaps.size >= 5) return // 이미 5개의 이미지가 선택된 경우 추가하지 않음

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
            setImageBitmap(bitmap) // Bitmap 설정
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
                // removeImage
                removeImageFromLayout(frameLayout, bitmap)
            }
        }

        frameLayout.addView(imageView)
        frameLayout.addView(deleteButton)
        binding.imageContainer.addView(frameLayout)
        selectedBitmaps.add(bitmap) // selectedBitmaps는 선택된 Bitmap 객체를 추적하는 리스트
    }

    private fun removeImageFromLayout(frameLayout: FrameLayout, uri: Uri) {
        binding.imageContainer.removeView(frameLayout)
        selectedImages.remove(uri)
    }

    private fun removeImageFromLayout(frameLayout: FrameLayout, bitmap: Bitmap) {
        binding.imageContainer.removeView(frameLayout) // FrameLayout을 메인 레이아웃에서 제거
        selectedBitmaps.remove(bitmap) // 선택된 Bitmap 리스트에서 해당 Bitmap 제거
    }

}