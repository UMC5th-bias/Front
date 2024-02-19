package com.example.favoriteplace

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favoriteplace.databinding.ItemRallyHeartVerifyBinding
import java.util.ArrayList

class MyLikeRallyRVAdapter (private val context: Context, private val myLikeRallyList: List<MyRally>): RecyclerView.Adapter<MyLikeRallyRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): MyLikeRallyRVAdapter.ViewHolder {
        val binding: ItemRallyHeartVerifyBinding = ItemRallyHeartVerifyBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=myLikeRallyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(myLikeRallyList[position])
    }

    inner class ViewHolder(val binding: ItemRallyHeartVerifyBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(myLikeRally: MyRally){
            binding.itemRallyHeartVerifyTitleTv.text=myLikeRally.title
            binding.itemRallyHeartVerifyRallyNowTv.text= myLikeRally.myPilgrimageNumber.toString()
            binding.itemRallyHeartVerifyRallyTotalTv.text= myLikeRally.pilgrimageNumber.toString()
            Glide.with(context)
                .load(myLikeRally.imageUrl)
                .placeholder(null)
                .into(binding.itemRallyHeartVerifyRallyIv)
        }

    }

}