package com.example.favoriteplace

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.favoriteplace.databinding.FragmentCommunityRallyBinding
import com.google.android.material.tabs.TabLayoutMediator

class CommunityRallyFragment : Fragment(), SortBottomSheetFragment.OnSortOptionSelectedListener {

    lateinit var binding: FragmentCommunityRallyBinding
    private val information= arrayListOf("최신 글","추천 많은 글","내가 작성한 글","내 댓글")
    private var searchText: String=""
    private var searchType: String=""

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCommunityRallyBinding.inflate(inflater,container,false)

        val communityRallyAdapter=CommunityRallyVPAdapter(this)
        binding.communityRallyVp.adapter=communityRallyAdapter
        TabLayoutMediator(binding.communityRallyTb,binding.communityRallyVp){
                tab,position->
            tab.text=information[position]
        }.attach()

        binding.communityRallySortIb.setOnClickListener {
            val sortBottomSheet = SortBottomSheetFragment()
            sortBottomSheet.show(parentFragmentManager, sortBottomSheet.tag)
        }

        //검색 바에서 엔터를 누르고 떼었을 때
        binding.searchTextView.setOnKeyListener { v, keyCode, event ->
            if(keyCode== KeyEvent.KEYCODE_ENTER && event.action== KeyEvent.ACTION_UP){
                goToSearch()    //서치바로 이동하는 함수
                return@setOnKeyListener true
            }
            false
        }

        //검색 바에서 검색 아이콘을 누르고 떼었을 때
        binding.searchTextView.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT=2
            if (event.action== MotionEvent.ACTION_UP){
                if (event.rawX>=(binding.searchTextView.right-binding.searchTextView.compoundDrawables[DRAWABLE_RIGHT].bounds.width())){
                    goToSearch()    //서치바로 이동하는 함수
                    return@setOnTouchListener true
                }
            }
            false
        }

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.communityRallySortIb.setOnClickListener {
            modalWithRoundCorner()
        }
    }

    private fun modalWithRoundCorner() {
        val modal = SortBottomSheetFragment().apply {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
        }
        modal.show(childFragmentManager, SortBottomSheetFragment.TAG)
    }

    //서치바로 이동하는 함수
    private fun goToSearch(){

        //서치바로 이동할 때 키보드를 숨김
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.searchTextView.windowToken, 0)

        //사용자가 검색한 내용 저장
        searchText= binding.searchTextView.text.toString()
        searchType=binding.communityRallySortTv.text.toString()

        //CommunityFreeSearchFragment()로 전환
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frameLayout, CommunityRallySearchFragment().apply {
                arguments = Bundle().apply {
                    putString("SEARCH_TEXT", searchText) // SEARCH_TEXT 키로 사용자가 검색한 내용 저장
                    putString("SEARCH_TYPE", searchType)
                }
            })
            .commitAllowingStateLoss()
    }

    //선택한 정렬 옵션에 따른 처리
    override fun onSortOptionSelected(option: String) {
        when (option) {
            "제목" -> {
                // 제목으로 정렬하는 작업 수행
                binding.communityRallySortTv.text="제목"
            }
            "내용" -> {
                // 날짜로 정렬하는 작업 수행
                binding.communityRallySortTv.text="내용"
                arguments = Bundle().apply {
                    putString("SEARCH_TYPE", option) // SEARCH_TEXT 키로 사용자가 검색한 내용 저장
                }
            }
            "닉네임" -> {
                // 닉네임으로 정렬하는 작업 수행
                binding.communityRallySortTv.text="닉네임"
                arguments = Bundle().apply {
                    putString("SEARCH_TYPE", option) // SEARCH_TEXT 키로 사용자가 검색한 내용 저장
                }
            }
        }
    }
}