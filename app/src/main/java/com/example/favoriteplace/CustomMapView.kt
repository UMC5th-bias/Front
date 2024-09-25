package com.example.favoriteplace

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.naver.maps.map.MapView

//NestedScrollView 안에서 MapView 사용할 때 발생하는 스크롤 문제 해결용
class CustomMapView(context: Context, attributeSet: AttributeSet) : MapView(context, attributeSet) {

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_UP -> parent.requestDisallowInterceptTouchEvent(false)
            MotionEvent.ACTION_DOWN -> parent.requestDisallowInterceptTouchEvent(true)
        }
        return super.dispatchTouchEvent(ev)
    }
}