package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplace.databinding.ItemRallyHeartVerifyBinding
import java.util.ArrayList

class MyVerifyRallyRVAdapter (private val myVerifyRallyList: ArrayList<MyVerifyRally>): RecyclerView.Adapter<MyVerifyRallyRVAdapter.ViewHolder>(){
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
        fun bind(myVerifyRally: MyVerifyRally){
            binding.itemRallyHeartVerifyTitleTv.text=myVerifyRally.title
            binding.itemRallyHeartVerifyRallyNowTv.text= myVerifyRally.progress.toString()
            binding.itemRallyHeartVerifyRallyTotalTv.text= myVerifyRally.total.toString()
            binding.itemRallyHeartVerifyRallyIv.setImageResource(myVerifyRally.coverImg!!)
        }

    }

}