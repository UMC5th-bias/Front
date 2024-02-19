package com.example.favoriteplace

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.favoriteplace.Comment
import com.example.favoriteplace.databinding.ItemCommentBinding

class CommentAdapter(private val comments: List<Comment>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCommentBinding.inflate(inflater, parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentComment = comments[position]
        holder.bind(currentComment)
    }

    override fun getItemCount(): Int=comments.size

    inner class CommentViewHolder(private val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Comment) {
            binding.apply {
                if(comment.isWrite) {
                    binding.commentBackgroundCl.setBackgroundColor(Color.parseColor("#FFF2F8"))
                }
                commentNicknameTv.text = comment.userInfo.nickname

                Glide.with(commentNicknameTv.context)
                    .load(comment.userInfo.profileImageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // 이미지 캐싱 전략
                    .apply(RequestOptions().circleCrop()) // RequestOptions를 사용하여 circleCrop 적용
                    .error(R.drawable.memberimg) // 로딩 실패 시 표시할 이미지
                    .transition(DrawableTransitionOptions.withCrossFade()) // 크로스페이드 효과 적
                    .into(binding.profileImgIv) // profileImageIv는 PNG 이미지를 로드할 ImageView의 ID입니다.

                // Coil을 사용하여 SVG 이미지 로딩 - 프로필 타이틀
                val imageLoader = ImageLoader.Builder(commentNicknameTv.context)
                    .availableMemoryPercentage(0.25) // 사용할 수 있는 메모리 비율 설정
                    .crossfade(true) // 크로스페이드 효과 활성화
                    .componentRegistry { add(SvgDecoder(commentNicknameTv.context)) }
                    .build()

                val request = ImageRequest.Builder(commentNicknameTv.context)
                    .crossfade(true)
                    .crossfade(300)
                    .data(comment.userInfo.profileTitleUrl)
                    .target(binding.commentTag) // profileTitleIv는 SVG 이미지를 로드할 ImageView의 ID입니다.
                    .build()
                imageLoader.enqueue(request)

                // Coil을 사용하여 SVG 이미지 로딩 - 프로필 아이콘
                val iconRequest = ImageRequest.Builder(commentNicknameTv.context)
                    .crossfade(true)
                    .crossfade(300)
                    .data(comment.userInfo.profileIconUrl)
                    .target(binding.commentDetailMyIconIv) // profileIconIv는 SVG 이미지를 로드할 ImageView의 ID입니다.
                    .build()
                imageLoader.enqueue(iconRequest)

                commentTv.text = comment.content
                commentTimeTv.text = comment.passedTime

                // 로그 추가
                Log.d("CommentAdapter", "Comment bound: $comment")
            }
        }
    }
}
