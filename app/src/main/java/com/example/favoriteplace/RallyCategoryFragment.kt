package com.example.favoriteplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentRallycategoryBinding

class RallyCategoryFragment : Fragment() {

    lateinit var binding: FragmentRallycategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentRallycategoryBinding.inflate(inflater,container,false)

        binding.rallyCategorySortTv.setOnClickListener {
            showPopup(binding.rallyCategorySortTv)
        }
        return binding.root
    }

    private fun showPopup(v: View){
        val popup=PopupMenu(this.context, v)
        popup.menuInflater.inflate(R.menu.popup, popup.menu)
        popup.show()
    }
}