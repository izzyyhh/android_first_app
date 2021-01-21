package com.example.mobilevo2.listeners

import android.view.View
//geek for geeks inspired
abstract class DoubleTapListener : View.OnClickListener {
    var lastClickTime: Long = 0
    override fun onClick(v: View?) {
        val clickTime = System.currentTimeMillis()
        val diffMs = 350

        if (clickTime - lastClickTime < diffMs) {
            onDoubleTap(v)
        }
        lastClickTime = clickTime
    }

    abstract fun onDoubleTap(v: View?)

}
