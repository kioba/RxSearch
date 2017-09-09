package com.kioba.rxsearch.util

import android.support.v7.widget.AppCompatEditText
import android.view.MotionEvent

fun AppCompatEditText.setRightDrawableOnTouchListener(func: AppCompatEditText.() -> Unit) {
  setOnTouchListener { _, event ->
    if (event.action == MotionEvent.ACTION_UP) {
      val drawable = compoundDrawables[2]
      if (event.rawX >= (right - drawable.bounds.width())) {
        func()
      }
    }
    false
  }
}
