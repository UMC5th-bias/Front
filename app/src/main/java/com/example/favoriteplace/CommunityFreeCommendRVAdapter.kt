package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemCommunityFreeCommendBinding

class CommunityFreeCommendRVAdapter (private val commendList: ArrayList<Comments>):RecyclerView.Adapter<CommunityFreeCommendRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCommunityFreeCommendBinding=ItemCommunityFreeCommendBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=commendList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(commendList[position])
    }

    inner class ViewHolder(val binding: ItemCommunityFreeCommendBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(commend: Comments){
            binding.itemCommunityFreeCommendDayTv.text=commend.passedTime
            binding.itemCommunityFreeCommendTv.text=commend.content
            binding.itemCommunityFreeCommendTitleTv.text=commend.post.title
            binding.itemCommunityFreeCommendWriterTv.text=commend.post.nickname
            binding.itemCommunityFreeCommendEyeTv.text= commend.post.views.toString()
            binding.itemCommunityFreeCommendLikeTv.text= commend.post.likes.toString()
            binding.itemCommunityFreeCommendClockTv.text= commend.post.passedTime
            binding.itemCommunityFreeCommendCommentNumTv.text= commend.post.comments.toString()
        }

    }
}