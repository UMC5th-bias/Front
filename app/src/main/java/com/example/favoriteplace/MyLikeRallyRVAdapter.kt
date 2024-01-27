package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemRallyHeartVerifyBinding
import java.util.ArrayList

class MyLikeRallyRVAdapter (private val myLikeRallyList: ArrayList<MyLikeRally>): RecyclerView.Adapter<MyLikeRallyRVAdapter.ViewHolder>(){
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
        fun bind(myLikeRally: MyLikeRally){
            binding.itemRallyHeartVerifyTitleTv.text=myLikeRally.title
            binding.itemRallyHeartVerifyRallyNowTv.text= myLikeRally.progress.toString()
            binding.itemRallyHeartVerifyRallyTotalTv.text= myLikeRally.total.toString()
            binding.itemRallyHeartVerifyRallyIv.setImageResource(myLikeRally.coverImg!!)
        }

    }

}