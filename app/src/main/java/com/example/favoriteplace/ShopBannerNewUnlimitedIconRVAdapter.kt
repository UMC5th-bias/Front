package com.example.favoriteplace

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.favoriteplace.databinding.ItemShopBannerNewIconBinding

class ShopBannerNewUnlimitedIconRVAdapter (private val unlimitedIconList: ArrayList<UnlimitedIcon>):
    RecyclerView.Adapter<ShopBannerNewUnlimitedIconRVAdapter.ViewHolder>(){

    //RVA에서 setOnClickListener을 쓸 수 있도록 하는 인터페이스
    interface MyItemClickListener {
        fun onItemUnlimitedIconClick(unlimitedIcon: UnlimitedIcon)
    }

    //전달받은 리스너 객체를 저장하는 변수
    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener=itemClickListener
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ShopBannerNewUnlimitedIconRVAdapter.ViewHolder {
        val binding: ItemShopBannerNewIconBinding= ItemShopBannerNewIconBinding.inflate(
            LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int=unlimitedIconList.size

    override fun onBindViewHolder(
        holder: ShopBannerNewUnlimitedIconRVAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(unlimitedIconList[position])
        holder.itemView.setOnClickListener{
            mItemClickListener.onItemUnlimitedIconClick(unlimitedIconList[position])
        }
    }

    inner class ViewHolder(val binding: ItemShopBannerNewIconBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(unlimitedIcon: UnlimitedIcon){
            try {
                Log.w("test", "id: ${unlimitedIcon.id}, img: ${unlimitedIcon.iconImg}")
                val imageLoader = ImageLoader.Builder(binding.root.context)
                    .componentRegistry {
                        add(SvgDecoder(binding.root.context)) // SVG 이미지 처리를 위해 SvgDecoder 추가
                    }
                    .build()

                val imageRequest = ImageRequest.Builder(binding.root.context)
                    .crossfade(true)
                    .crossfade(300)  //애니메이션 처리
                    .data(unlimitedIcon.iconImg)
                    .target(binding.itemShopBannerNewIconIv) //해당 이미지뷰를 타겟으로 svg 삽입
                    .build()
                imageLoader.enqueue(imageRequest)

                binding.itemShopBannerNewIconCostTv.text=unlimitedIcon.cost
                if(unlimitedIcon.cost == "사용중" || unlimitedIcon.cost == "") {  // 마이페이지 프로필 카드 편집용
                    binding.itemShopBannerPTv.text = ""
                }
                binding.itemShopBannerNewIconTitleTv.text=unlimitedIcon.title

            } catch (e: Exception) {
                Log.e("ViewHolder", "Error loading image: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}