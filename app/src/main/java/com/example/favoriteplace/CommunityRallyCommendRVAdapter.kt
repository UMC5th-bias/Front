package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemCommunityRallyCommendBinding

class CommunityRallyCommendRVAdapter (private val commendList: ArrayList<MyComments>):
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
        fun bind(commend: MyComments){
            binding.itemCommunityRallyCommendDayTv.text=commend.passedTime
            binding.itemCommunityRallyCommendTv.text=commend.content
            binding.itemCommunityRallyCommendTitleTv.text=commend.myGuestBookInfo.title
            binding.itemCommunityRallyCommendWriterTv.text=commend.myGuestBookInfo.nickname
            binding.itemCommunityRallyCommendEyeTv.text= commend.myGuestBookInfo.views.toString()
            binding.itemCommunityRallyCommendLikeTv.text= commend.myGuestBookInfo.likes.toString()
            binding.itemCommunityRallyCommendClockTv.text= commend.myGuestBookInfo.passedTime
            binding.itemCommunityRallyCommendCommentNumTv.text= commend.myGuestBookInfo.comments.toString()
        }

    }
}