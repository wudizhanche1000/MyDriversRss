package org.weijian.mydriversrss

import android.support.v7.widget.RecyclerView

/**
 * Created by weijian on 16-4-15.
 */

abstract class EndlessOnScrollListener : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (!recyclerView.canScrollVertically(1)) {
            onScrolledToEnd(recyclerView)
        }
    }

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
    }

    abstract fun onScrolledToEnd(recyclerView: RecyclerView)
}