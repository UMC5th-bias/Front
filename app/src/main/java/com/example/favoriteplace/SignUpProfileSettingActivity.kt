package com.example.favoriteplace

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.favoriteplace.databinding.ActivitySignupProfileSettingBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File

class SignUpProfileSettingActivity: AppCompatActivity() {

    lateinit var binding: ActivitySignupProfileSettingBinding


    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    // 이미지 파일 리스트
    private val imageFiles = mutableListOf<File>()

    // 권한 요청에 사용될 코드 상수
    private val STORAGE_PERMISSION_REQUEST_CODE = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupProfileSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("SignUp", ">> Starting SignUpProfileSettingActivity")


        binding.nameTet.addTextChangedListener {
            updateButtonState()
        }

        // 프로필 사진 설정
        binding.profileEditIv.setOnClickListener {
            checkStoragePermission()

        }

        // 뒤로가기
        binding.backPageIv.setOnClickListener {
            val intent = Intent(this@SignUpProfileSettingActivity, SignUpPwdInputActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.nextBtn.setOnClickListener {
            val snsAllow = intent.getBooleanExtra("snsAllow", false)
            val email = intent.getStringExtra("email") ?:""
            val password = intent.getStringExtra("password") ?: ""
            val nickname = binding.nameTet.text.toString()
            val introduction = binding.introductionTet.text.toString()


            val imageUri = imageUri  // 이미지 URI 가져오기
            val profileImageUri = imageUri


            val intent = Intent(this@SignUpProfileSettingActivity, SignUpFinishActivity::class.java)
            intent.putExtra("snsAllow", snsAllow)
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            intent.putExtra("nickname", nickname)
            intent.putExtra("introduction", introduction)
            intent.putExtra("imageUri", imageUri)

            intent.putExtra("profileImageUri",profileImageUri.toString())

            goToSignUpFinishActivity(imageUri?.toString())

        }

    }

    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ){
            // 이미 권한이 부여되지 않은 경우 권한 요청
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                STORAGE_PERMISSION_REQUEST_CODE)
        }else{
            openGallery()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==STORAGE_PERMISSION_REQUEST_CODE){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                // 권한이 허용된 이후 작업 수행하기
                openGallery()
                Log.d("SignUp", "Storage permission granted")
            }else{
                Toast.makeText(
                    this,
                    "Storage permission denied. Unable to access images.",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }
    // 이미지 파일을 MultipartBody.Part로 변환하는 함수
    private fun prepareImageParts(): List<MultipartBody.Part> {
        val imageParts = mutableListOf<MultipartBody.Part>()
        for (imageFile in imageFiles) {
            val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("images", imageFile.name, requestBody)
            imageParts.add(imagePart)
        }
        return imageParts
    }
    // 이미지 파일을 추가하는 함수
    private fun addImageFile(file: File) {
        imageFiles.add(file)
    }

    private fun openGallery() {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun updateButtonState() {
        val nickname = binding.nameTet.text.toString()
        val introduction = binding.introductionTet.text.toString()

        if (nickname.isNotEmpty() && introduction.isNotEmpty()){
            binding.nextBtn.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this@SignUpProfileSettingActivity, R.color.main))
        } else {
            binding.nextBtn.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this@SignUpProfileSettingActivity, R.color.gray))
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("SignUp", ">> onActivityResult() ")


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage = data.data

            selectedImage?.let {
                // 이미지 선택 후 처리
                imageUri = Uri.parse(getRealPathFromURI(selectedImage))
                Log.d("SignUp", ">> imageUri: $imageUri")
                // 이미지 파일을 생성하고 리스트에 추가
                val imageFile = File(getRealPathFromURI(selectedImage))
                addImageFile(imageFile)

                intent.putExtra("imageFiles",imageFiles.toTypedArray())

//                val inputStream = contentResolver.openInputStream(it)
//                inputStream?.let { stream ->
//                    val bitmap = BitmapFactory.decodeStream(stream)
//                    val byteArrayOutputStream = ByteArrayOutputStream()
//                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
//                    binding.profileIv.setImageBitmap(bitmap)
//
//                }


                // CircleImageView에 이미지 설정
                binding.profileIv.setImageURI(imageUri)

                Log.d("SignUp", ">> 사진 선택 완료")
            }
        }
    }

    private fun goToSignUpFinishActivity(imageFilePath: String?) {
        val snsAllow = intent.getBooleanExtra("snsAllow", false)
        val email = intent.getStringExtra("email") ?: ""
        val password = intent.getStringExtra("password") ?: ""
        val nickname = binding.nameTet.text.toString()
        val introduction = binding.introductionTet.text.toString()
        val imageParts = prepareImageParts()

        val intent = Intent(this@SignUpProfileSettingActivity, SignUpFinishActivity::class.java)
        intent.putExtra("snsAllow", snsAllow)
        intent.putExtra("email", email)
        intent.putExtra("password", password)
        intent.putExtra("nickname", nickname)
        intent.putExtra("introduction", introduction)
        intent.putExtra("imageUri", imageUri)  // 이미지 URI를 SignUpFinishActivity로 전달
        intent.putExtra("imageFilePath", imageFilePath)

        Log.d("SignUp", ">> { $snsAllow, $email, $password, $nickname, $introduction,$imageFilePath }")
        startActivity(intent)
        finish() // 현재 화면 종료
    }

    private fun getRealPathFromURI(uri: Uri): String ? {

        var filePath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use { c ->
            if (c.moveToFirst()) {
                val columnIndex = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                filePath = c.getString(columnIndex)
            }
        }
        return filePath ?: ""

    }
}