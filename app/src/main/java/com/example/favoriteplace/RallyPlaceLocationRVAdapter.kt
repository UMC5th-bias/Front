package com.example.favoriteplace

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favoriteplace.databinding.ItemRallyplaceLocationlistBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RallyPlaceLocationRVAdapter(
    private val context: Context,
    private val locationInfoList:  List<RallyPlaceAnimation>,

) : RecyclerView.Adapter<RallyPlaceLocationRVAdapter.viewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): RallyPlaceLocationRVAdapter.viewHolder {
        val binding: ItemRallyplaceLocationlistBinding = ItemRallyplaceLocationlistBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
        return viewHolder(binding)
    }

    override fun getItemCount(): Int {
        return locationInfoList.size
    }

    override fun onBindViewHolder(holder: RallyPlaceLocationRVAdapter.viewHolder, position: Int) {
        holder.bind(locationInfoList[position])

        holder.itemView.setOnClickListener {
            val itemId = locationInfoList[position].id  // 클릭한 아이템의 ID 가져오기


            if(!isLoggedIn()){
                // 비회원인 경우
                Toast.makeText(context, "로그인 후 이용 가능한 메뉴입니다.", Toast.LENGTH_SHORT).show()

            }else{
                val fragment = RallyLocationDetailFragment()
                val bundle = Bundle().apply {
                    putLong("rallyAnimationId", itemId) // 아이템의 ID를 번들에 추가
                }
                fragment.arguments = bundle

                // Fragment 이동
                val fragmentManager = (context as AppCompatActivity).supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.main_frameLayout, fragment)
                    .addToBackStack(null)  // 뒤로 가기 스택에 추가
                    .commit()
            }
        }
    }


    inner class viewHolder(val binding: ItemRallyplaceLocationlistBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(info: RallyPlaceAnimation){
            binding.rallyplaceLocationAni.text = info.title
            binding.rallyplaceLocationLoc.text = info.detailAddress
            Glide.with(context)
                .load(info.image)
                .into(binding.rallyplaceLocationImg)
            binding.rallyplaceLocationImg.clipToOutline = true
        }
    }


    private fun isLoggedIn(): Boolean {
        // SharedPreferences에서 액세스 토큰 가져오기
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }
}