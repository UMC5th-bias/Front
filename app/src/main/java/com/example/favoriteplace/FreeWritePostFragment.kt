package com.example.favoriteplace

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentFreeWritePostBinding

class FreeWritePostFragment : Fragment() {
    lateinit var binding: FragmentFreeWritePostBinding

    private val REQUEST_CODE_GALLERY = 100
    private val selectedImages = mutableListOf<Uri>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFreeWritePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.writePostGalleryIv.setOnClickListener {
            openGallery()
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

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == android.app.Activity.RESULT_OK) {
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
    }

    private fun addImageToLayout(uri: Uri) {
        if (selectedImages.size >= 5) return // 이미 5개의 이미지가 선택된 경우 추가하지 않음

        val frameLayout = FrameLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginEnd = 10
            }
        }

        val imageView = ImageView(requireContext()).apply {
            layoutParams = FrameLayout.LayoutParams(100, 100)
            scaleType = ImageView.ScaleType.CENTER_CROP
            setImageURI(uri)
        }

        val deleteButton = ImageButton(requireContext()).apply {
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
    private fun removeImageFromLayout(frameLayout: FrameLayout, uri: Uri) {
        binding.imageContainer.removeView(frameLayout)
        selectedImages.remove(uri)
    }
}