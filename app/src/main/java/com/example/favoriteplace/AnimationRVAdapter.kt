package com.example.favoriteplace

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favoriteplace.databinding.ItemAnimationBinding
import java.util.ArrayList

class AnimationRVAdapter(private val animationList: MutableList<Animation>, val context: Context): RecyclerView.Adapter<AnimationRVAdapter.ViewHolder>() {
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
            binding.rallyCategoryAnimationTitleTv.text=animation.title
            Glide.with(context)
                .load(animation.coverImg)
                .into(binding.rallyCategoryAnimationIv)
            binding.rallyCategoryAnimationProgressTv.text=animation.progress
            binding.rallyCategoryAnimationIv.setOnClickListener {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frameLayout, RallyDetailFragment())
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
            }
        }

    }
}