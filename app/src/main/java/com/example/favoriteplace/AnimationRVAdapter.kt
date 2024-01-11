package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemAnimationBinding
import java.util.ArrayList

class AnimationRVAdapter(private val animationList: ArrayList<Animation>): RecyclerView.Adapter<AnimationRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): AnimationRVAdapter.ViewHolder {
        val binding: ItemAnimationBinding=ItemAnimationBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimationRVAdapter.ViewHolder, position: Int) {
        holder.bind(animationList[position])
    }

    override fun getItemCount(): Int=animationList.size

    inner class ViewHolder(val binding: ItemAnimationBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(animation: Animation){
            binding.rallyCategoryAnimationIv.setImageResource(animation.coverImg!!)
            binding.rallyCategoryAnimationTitleTv.text=animation.title
            binding.rallyCategoryAnimationProgressTv.text=animation.progress
        }

    }
}