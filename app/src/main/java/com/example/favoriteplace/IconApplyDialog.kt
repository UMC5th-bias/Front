package com.example.favoriteplace

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.favoriteplace.databinding.DialogShopDetailApplyIconBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IconApplyDialog : DialogFragment(){
    private lateinit var binding: DialogShopDetailApplyIconBinding
    private var itemId: Int = 0 // 아이템 ID를 저장할 변수

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DialogShopDetailApplyIconBinding.inflate(inflater,container,false)
        val view=binding.root

        // Bundle에서 아이템 ID를 가져옴
        itemId = requireArguments().getInt("ITEM_ID")


        //팝업창 모서리 둥글게 만들기
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.dialogShopDetailApplyNoBtn.setOnClickListener{
            Toast.makeText(requireContext(),"취소되었습니다", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        binding.dialogShopDetailApplyYesBtn.setOnClickListener {
            applyItem(itemId)
        }

        return view
    }

    private fun applyItem(itemId: Int) {
        // 아이템 적용 요청 보내기
        val token = "Bearer ${getAccessToken()}"
        val call = RetrofitClient.shopService.applyItem(token, itemId)
        Log.d("ApplyItem", "Item ID : $itemId")

        call.enqueue(object : Callback<ApplyResponse> {
            override fun onResponse(call: Call<ApplyResponse>, response: Response<ApplyResponse>) {
                if (response.isSuccessful) {
                    Log.d("ApplyItem", "아이템이 성공적으로 적용되었습니다")
                    showToast(requireContext(), "아이템이 성공적으로 적용되었습니다")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ApplyItem", "아이템 적용에 실패했습니다. 오류 내용: $errorBody")
                    showToast(requireContext(), "아이템 적용에 실패했습니다")
                }
                dismiss() // 팝업 닫기
            }

            override fun onFailure(call: Call<ApplyResponse>, t: Throwable) {
                Log.e("ApplyItem", "네트워크 오류가 발생했습니다: ${t.message}")
                showToast(requireContext(), "네트워크 오류가 발생했습니다")
                dismiss() // 팝업 닫기
            }
        })
    }

    // SharedPreferences에서 액세스 토큰을 가져오는 메소드
    private fun getAccessToken(): String? {
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString(LoginActivity.ACCESS_TOKEN_KEY, null)
    }

    // 커스텀 토스트 메시지 표시
    private fun showToast(context: Context, message: String) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.custom_toast, null)

        val textView = layout.findViewById<TextView>(R.id.custom_toast_message)
        textView.text = message

        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }
}