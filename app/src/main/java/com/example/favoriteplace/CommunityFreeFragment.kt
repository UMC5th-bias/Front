package com.example.favoriteplace

import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
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
import com.example.favoriteplace.databinding.FragmentCommunityFreeBinding
import com.google.android.material.tabs.TabLayoutMediator

class CommunityFreeFragment : Fragment(), SortBottomSheetFragment.OnSortOptionSelectedListener{

    lateinit var binding: FragmentCommunityFreeBinding
    private val information= arrayListOf("최신 글","추천 많은 글","내가 작성한 글","내 댓글")
    private var searchText: String=""
    private var searchType: String=""

    @SuppressLint("ClickableViewAccessibility", "ServiceCast")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentCommunityFreeBinding.inflate(inflater,container,false)

        //탭 레이아웃 VPA 실행
        val communityFreeAdapter=CommunityFreeVPAdapter(this)
        binding.communityFreeVp.adapter=communityFreeAdapter
        TabLayoutMediator(binding.communityFreeTb,binding.communityFreeVp){
            tab,position->
            tab.text=information[position]
        }.attach()

        //검색 바에 포커스가 오면 글쓰기 버튼 비가시화
        binding.searchTextView.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                binding.communityFreeWriteBn.visibility=View.INVISIBLE
            }
        }

        //검색 바에서 엔터를 누르고 떼었을 때
        binding.searchTextView.setOnKeyListener { v, keyCode, event ->
            if(keyCode==KeyEvent.KEYCODE_ENTER && event.action==KeyEvent.ACTION_UP){
                goToSearch()    //서치바로 이동하는 함수
                return@setOnKeyListener true
            }
            false
        }

        //검색 바에서 검색 아이콘을 누르고 떼었을 때
        binding.searchTextView.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT=2
            if (event.action==MotionEvent.ACTION_UP){
                if (event.rawX>=(binding.searchTextView.right-binding.searchTextView.compoundDrawables[DRAWABLE_RIGHT].bounds.width())){
                    goToSearch()    //서치바로 이동하는 함수
                    return@setOnTouchListener true
                }
            }
            false
        }

        //글쓰기 버튼을 눌렀을 때 FreeWritePostActivity 실행
        binding.communityFreeWriteBn.setOnClickListener {
            val intent = Intent(activity, FreeWritePostActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.communityFreeSortIb.setOnClickListener {
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
        val inputMethodManager = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.searchTextView.windowToken, 0)

        //사용자가 검색한 내용 저장
        searchText= binding.searchTextView.text.toString()
        searchType=binding.communityFreeSortTv.text.toString()

        //CommunityFreeSearchFragment()로 전환
        //
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frameLayout, CommunityFreeSearchFragment().apply {
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
                binding.communityFreeSortTv.text="제목"
            }
            "내용" -> {
                // 날짜로 정렬하는 작업 수행
                binding.communityFreeSortTv.text="내용"
                arguments = Bundle().apply {
                    putString("SEARCH_TYPE", option) // SEARCH_TEXT 키로 사용자가 검색한 내용 저장
                }
            }
            "닉네임" -> {
                // 닉네임으로 정렬하는 작업 수행
                binding.communityFreeSortTv.text="닉네임"
                arguments = Bundle().apply {
                    putString("SEARCH_TYPE", option) // SEARCH_TEXT 키로 사용자가 검색한 내용 저장
                }
            }
        }
    }
}