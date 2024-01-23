package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemCommunityRallyLatelyRecommendBinding
import java.util.ArrayList

class CommunityRallyRecommendRVAdapter (private val rallyRecommendList: ArrayList<RallyRecommend>): RecyclerView.Adapter<CommunityRallyRecommendRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): CommunityRallyRecommendRVAdapter.ViewHolder {
        val binding: ItemCommunityRallyLatelyRecommendBinding = ItemCommunityRallyLatelyRecommendBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=rallyRecommendList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(rallyRecommendList[position])
    }

    inner class ViewHolder(val binding: ItemCommunityRallyLatelyRecommendBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(rallyRecommend: RallyRecommend){
            binding.itemCommunityRallyIv.setImageResource(rallyRecommend.coverImg!!)
            binding.itemCommunityRallyTitleTv.text=rallyRecommend.title
            binding.itemCommunityRallyWriterTv.text=rallyRecommend.writer
            binding.itemCommunityRallyEyeTv.text= rallyRecommend.eye.toString()
            binding.itemCommunityRallyLikeTv.text=rallyRecommend.like.toString()
            binding.itemCommunityRallyClockTv.text= rallyRecommend.time
            binding.itemCommunityRallyCommentTv.text= rallyRecommend.commentNum.toString()
        }

    }

}