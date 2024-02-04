package com.example.favoriteplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.favoriteplace.databinding.FragmentRallydetailBinding
import com.example.favoriteplace.databinding.FragmentRallyhomeBinding
import com.google.android.gms.common.api.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.lang.Exception


class RallyDetailFragment : Fragment() {

    lateinit var binding:FragmentRallydetailBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentRallydetailBinding.inflate(inflater,container,false)



        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Retrofit을 사용하여 HTTP 통신을 위한 초기화
//        val retrofit = Retrofit.Builder()
//            .baseUrl("")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val apiService = retrofit.create(ApiService::class.java)


        //서버에서 데이터를 비동기적으로 가져오기
//        lifecycleScope.launch {
//            try {
//                val response = withContext(Dispatchers.IO){
//                    apiService.fetchData().execute()
//                }
//
//                if (response.isSuccessful){
//                    //성공적으로 데이터를 받아온 경우
//                    val dataModel = response.body()
//                    binding.rallydetailTitleTv.text = dataModel?.name ?: "No data available"
//
//                }else{
//                    //서버로 부터 실패 응답 받은 경우
//                    binding.rallydetailTitleTv.text= "Failed to fetch data. Response code: ${response.code()}"
//
//                }
//            } catch (e:Exception){
//                // 네트워크 오류 또는 기타 예외 처리
//                binding.rallydetailTitleTv.text = "Error: ${e.message}"
//            }
//        }
    }


}