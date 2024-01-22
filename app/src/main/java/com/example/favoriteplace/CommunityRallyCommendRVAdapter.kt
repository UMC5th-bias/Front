package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemCommunityRallyCommendBinding

class CommunityRallyCommendRVAdapter (private val commendList: ArrayList<RallyCommend>):
    RecyclerView.Adapter<CommunityRallyCommendRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCommunityRallyCommendBinding = ItemCommunityRallyCommendBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=commendList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(commendList[position])
    }

    inner class ViewHolder(val binding: ItemCommunityRallyCommendBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(commend: RallyCommend){
            binding.itemCommunityRallyCommendDayTv.text=commend.day
            binding.itemCommunityRallyCommendTimeTv.text=commend.commendTime
            binding.itemCommunityRallyCommendTv.text=commend.commend
            binding.itemCommunityRallyCommendTitleTv.text=commend.title
            binding.itemCommunityRallyCommendWriterTv.text=commend.writer
            binding.itemCommunityRallyCommendEyeTv.text= commend.eye.toString()
            binding.itemCommunityRallyCommendLikeTv.text= commend.like.toString()
            binding.itemCommunityRallyCommendClockTv.text= commend.time
            binding.itemCommunityRallyCommendCommentNumTv.text= commend.commentNum.toString()
        }

    }
}