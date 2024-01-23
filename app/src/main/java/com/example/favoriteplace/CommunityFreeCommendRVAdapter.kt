package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemCommunityFreeCommendBinding
import com.example.favoriteplace.databinding.ItemCommunityFreeLatelyBinding

class CommunityFreeCommendRVAdapter (private val commendList: ArrayList<FreeCommend>):RecyclerView.Adapter<CommunityFreeCommendRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCommunityFreeCommendBinding=ItemCommunityFreeCommendBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=commendList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(commendList[position])
    }

    inner class ViewHolder(val binding: ItemCommunityFreeCommendBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(commend: FreeCommend){
            binding.itemCommunityFreeCommendDayTv.text=commend.day
            binding.itemCommunityFreeCommendTimeTv.text=commend.commendTime
            binding.itemCommunityFreeCommendTv.text=commend.commend
            binding.itemCommunityFreeCommendTitleTv.text=commend.title
            binding.itemCommunityFreeCommendWriterTv.text=commend.writer
            binding.itemCommunityFreeCommendEyeTv.text= commend.eye.toString()
            binding.itemCommunityFreeCommendLikeTv.text= commend.like.toString()
            binding.itemCommunityFreeCommendClockTv.text= commend.time
            binding.itemCommunityFreeCommendCommentNumTv.text= commend.commentNum.toString()
        }

    }
}