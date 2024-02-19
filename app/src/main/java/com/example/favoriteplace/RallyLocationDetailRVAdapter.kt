package com.example.favoriteplace

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.example.favoriteplace.databinding.FragmentMyGuestbookBinding
import com.example.favoriteplace.databinding.ItemCommentBinding

class RallyLocationDetailRVAdapter(
    private val context: Context,
    private val commmentList: List<RallyLocationDetailComment>
): RecyclerView.Adapter<RallyLocationDetailRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): RallyLocationDetailRVAdapter.ViewHolder {
        val binding: ItemCommentBinding = ItemCommentBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RallyLocationDetailRVAdapter.ViewHolder, position: Int) {
        holder.bind(commmentList[position])
    }

    override fun getItemCount(): Int=commmentList.size

    inner class ViewHolder(val binding: ItemCommentBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(comment: RallyLocationDetailComment){
            if(comment.isWrite) {
                binding.commentBackgroundCl.setBackgroundColor(Color.parseColor("#FFF2F8"))
            }
            binding.commentNicknameTv.text = comment.userInfo.nickname


            Glide.with(context)
                .load(comment.userInfo.profileImageUrl)
                .placeholder(R.drawable.signup_default_profile_image)
                .into(binding.commentDetailMyIconIv)
            bindSvgImg(comment.userInfo.profileIconUrl, binding.commentDetailMyIconIv)
            bindSvgImg(comment.userInfo.profileTitleUrl, binding.commentTag)
            binding.commentTimeTv.text = comment.passedTime
            binding.commentTv.text = comment.content

        }

    }
    //svg이미지 로더
    val imageLoader = ImageLoader.Builder(context)
        .componentRegistry {
            add(SvgDecoder(context)) // SVG 이미지 처리를 위해 SvgDecoder 추가
        }
        .build()
    //Glide로 svg이미지 등록
    fun bindSvgImg(img: String, target: ImageView) {
        val imageRequest = ImageRequest.Builder(context)
            .data(img)
            .target(target)  // 해당 이미지뷰를 타겟으로 svg 삽입
            .build()
        imageLoader.enqueue(imageRequest)
    }
}