package com.example.favoriteplace

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favoriteplace.databinding.ItemRallyHeartVerifyBinding
import java.util.ArrayList

class MyVerifyRallyRVAdapter (private val context: Context, private val myVerifyRallyList: List<MyRally>): RecyclerView.Adapter<MyVerifyRallyRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): MyVerifyRallyRVAdapter.ViewHolder {
        val binding: ItemRallyHeartVerifyBinding = ItemRallyHeartVerifyBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=myVerifyRallyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(myVerifyRallyList[position])
    }

    inner class ViewHolder(val binding: ItemRallyHeartVerifyBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(myVerifyRally: MyRally){
            binding.itemRallyHeartVerifyTitleTv.text=myVerifyRally.title
            binding.itemRallyHeartVerifyRallyNowTv.text= myVerifyRally.myPilgrimageNumber.toString()
            binding.itemRallyHeartVerifyRallyTotalTv.text= myVerifyRally.pilgrimageNumber.toString()
            Glide.with(context)
                .load(myVerifyRally.imageUrl)
                .placeholder(null)
                .into(binding.itemRallyHeartVerifyRallyIv)
        }

    }

}