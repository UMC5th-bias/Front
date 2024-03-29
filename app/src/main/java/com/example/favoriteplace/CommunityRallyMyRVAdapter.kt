package com.example.favoriteplace

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favoriteplace.databinding.ItemCommunityRallyMyBinding
import java.util.ArrayList

class CommunityRallyMyRVAdapter(private val rallyMyWriteList: ArrayList<GuestMy>,
                                private val listener: CommunityRallyMyRVAdapter.OnItemClickListener
): RecyclerView.Adapter<CommunityRallyMyRVAdapter.ViewHolder>(){

    // 클릭 이벤트를 처리할 리스너 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(guestBookId: Long)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCommunityRallyMyBinding = ItemCommunityRallyMyBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=rallyMyWriteList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(rallyMyWriteList[position])
    }
    inner class ViewHolder(val binding: ItemCommunityRallyMyBinding): RecyclerView.ViewHolder(binding.root){
        init {
            // ViewHolder 객체가 생성될 때 실행
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(rallyMyWriteList[position].id)
                }
            }
        }

        fun bind(freeRecommend: GuestMy){
            binding.itemCommunityRallyMyTitleTv.text=freeRecommend.title
            binding.itemCommunityRallyMyWriterTv.text=freeRecommend.nickname
            binding.itemCommunityRallyMyEyeTv.text= freeRecommend.views.toString()
            binding.itemCommunityRallyMyLikeTv.text=freeRecommend.likes.toString()
            binding.itemCommunityRallyMyCommentNumTv.text= freeRecommend.comments.toString()
            binding.itemCommunityRallyMyClockTv.text= freeRecommend.passedTime

            // 설정한 TextView에 ... 줄임 처리 설정
            binding.itemCommunityRallyMyTitleTv.ellipsize = TextUtils.TruncateAt.END
            binding.itemCommunityRallyMyTitleTv.maxLines = 1 // 최대 표시할 줄 수
        }


    }
}