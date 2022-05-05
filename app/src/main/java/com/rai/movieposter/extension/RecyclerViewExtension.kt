package com.rai.movieposter.extension

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addCardDecoration(bottomSpace: Int) {

    addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State,
        ) {
            val count = parent.adapter?.itemCount ?: return
            val position = getChildAdapterPosition(view)
            if (position != count - 1) {
                outRect.bottom = bottomSpace
            }
        }
    })

}