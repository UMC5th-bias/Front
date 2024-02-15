package com.example.favoriteplace

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favoriteplace.databinding.ItemRallyplaceLocationlistBinding

class RallyPlaceLocationRVAdapter(
    private val context: Context,
    private val locationInfoList:  List<RallyPlaceAnimation>
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
            if(!isLoggedIn()){
                // 비회원인 경우
                Toast.makeText(context, "로그인 후 이용 가능한 메뉴입니다.", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "클릭 완료", Toast.LENGTH_SHORT).show()
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