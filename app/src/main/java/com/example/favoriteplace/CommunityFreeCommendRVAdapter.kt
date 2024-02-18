package com.example.favoriteplace

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemCommunityFreeCommendBinding


class CommunityFreeCommendRVAdapter (private val commendList: ArrayList<Comments>,
                                     private val listener: OnItemClickListener
):RecyclerView.Adapter<CommunityFreeCommendRVAdapter.ViewHolder>() {

    // 클릭 이벤트를 처리할 리스너 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(postId: Int)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCommunityFreeCommendBinding =ItemCommunityFreeCommendBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)

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

            // 설정한 TextView에 ... 줄임 처리 설정
            binding.itemCommunityFreeCommendTitleTv.ellipsize = TextUtils.TruncateAt.END
            binding.itemCommunityFreeCommendTitleTv.maxLines = 1 // 최대 표시할 줄 수

            // 설정한 TextView에 ... 줄임 처리 설정
            binding.itemCommunityFreeCommendTv.ellipsize = TextUtils.TruncateAt.END
            binding.itemCommunityFreeCommendTv.maxLines = 1 // 최대 표시할 줄 수
        }

    }
}