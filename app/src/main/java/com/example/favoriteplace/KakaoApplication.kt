package com.example.favoriteplace

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class KakaoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "1bf8db2ce08e2ae1484687b0e9aba6f1")
    }

    companion object {
        @Volatile
        private var instance: KakaoApplication? = null

        fun getInstance(): KakaoApplication {
            return instance ?: synchronized(this) {
                val newInstance = instance ?: KakaoApplication().also { instance = it }
                newInstance
            }
        }
    }
}
