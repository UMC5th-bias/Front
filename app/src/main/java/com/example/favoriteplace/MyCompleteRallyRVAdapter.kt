package com.example.favoriteplace

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favoriteplace.databinding.ItemRallyHeartVerifyBinding
import java.util.ArrayList

class MyCompleteRallyRVAdapter (private val context: Context, private val myCompleteRally: List<MyRally>): RecyclerView.Adapter<MyCompleteRallyRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): MyCompleteRallyRVAdapter.ViewHolder {
        val binding: ItemRallyHeartVerifyBinding = ItemRallyHeartVerifyBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=myCompleteRally.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(myCompleteRally[position])
    }

    inner class ViewHolder(val binding: ItemRallyHeartVerifyBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(myCompleteRally: MyRally){
            binding.itemRallyHeartVerifyTitleTv.text=myCompleteRally.title
            binding.itemRallyHeartVerifyRallyNowTv.text= myCompleteRally.myPilgrimageNumber.toString()
            binding.itemRallyHeartVerifyRallyTotalTv.text= myCompleteRally.pilgrimageNumber.toString()
            Glide.with(context)
                .load(myCompleteRally.imageUrl)
                .placeholder(null)
                .into(binding.itemRallyHeartVerifyRallyIv)
        }

    }

}