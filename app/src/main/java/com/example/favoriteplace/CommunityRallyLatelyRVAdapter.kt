package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemCommunityRallyLatelyRecommendBinding
import java.util.ArrayList

class CommunityRallyLatelyRVAdapter (private val rallyLatelyList: ArrayList<RallyLatelyWrite>): RecyclerView.Adapter<CommunityRallyLatelyRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): CommunityRallyLatelyRVAdapter.ViewHolder {
        val binding: ItemCommunityRallyLatelyRecommendBinding = ItemCommunityRallyLatelyRecommendBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=rallyLatelyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(rallyLatelyList[position])
    }

    inner class ViewHolder(val binding: ItemCommunityRallyLatelyRecommendBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(rallyLately: RallyLatelyWrite){
            binding.itemCommunityRallyIv.setImageResource(rallyLately.coverImg!!)
            binding.itemCommunityRallyTitleTv.text=rallyLately.title
            binding.itemCommunityRallyWriterTv.text=rallyLately.writer
            binding.itemCommunityRallyEyeTv.text= rallyLately.eye.toString()
            binding.itemCommunityRallyLikeTv.text=rallyLately.like.toString()
            binding.itemCommunityRallyClockTv.text= rallyLately.time
            binding.itemCommunityRallyCommentTv.text= rallyLately.commentNum.toString()
        }

    }

}