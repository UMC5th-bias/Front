package com.example.favoriteplace

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface EmailCodeService {


    //인증번호 요청
    data class AuthResponse(
        val authNum:Int
    )


    //인증번호 실패시
    data class ErrorResponse(
        val success: Boolean,
        val httpStatus: String,
        val code: Int,
        val message: String
    )



    data class EmailRequest(
        val email: String
    )



    // 이메일 중복확인
    data class EmailExistRequest(
        val email : String
    )


    data class EmailExistResponse(
        val isExists: Boolean
    )


    // 인증번호 확인
    data class EmailCode(
        val email: String,
        val authNum: Int
    )

    @POST("/auth/signup/email")
    fun getAuthNumber(
        @Body request: EmailRequest
    ): Call<AuthResponse>


    @POST("/auth/signup/email/duplicate")
    fun checkEmailExistence(
        @Body request: EmailExistRequest
    ): Call<EmailExistResponse>


    @POST("/auth/signup/email/check")
    fun checkEmail(
        @Body request: EmailCode
    ):Call<Void>


}