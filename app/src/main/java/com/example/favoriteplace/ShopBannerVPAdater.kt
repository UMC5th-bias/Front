package com.example.favoriteplace

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter

class ShopBannerVPAdapter(fragmentActivity: FragmentActivity, private val items: List<BannerItemV2>) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = items.size
    override fun createFragment(position: Int): Fragment {
        val item = items[position]
        return ShopBannerFragment.newInstance(item.imageRes, item.fragmentClassName)
    }

}
