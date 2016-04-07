package org.weijian.mydriversrss

import android.content.Context
import android.database.Cursor
import android.database.DataSetObserver
import android.provider.BaseColumns
import android.support.v7.widget.RecyclerView

/**
 * Created by wudiz on 2016/4/7.
 */

abstract class CursorRecyclerViewAdapter<VH : RecyclerView.ViewHolder> constructor(context: Context, cursor: Cursor?) : RecyclerView.Adapter<VH>() {
    internal var mDataValid = false
    internal val context: Context
    internal var cursor: Cursor?
        get
    internal var mRowIdColumn = -1;
    internal var mObserver: DataSetObserver

    init {
        this.context = context
        this.cursor = cursor
        if (this.cursor != null) {
            mRowIdColumn = this.cursor?.getColumnIndex(BaseColumns._ID)!!
            mDataValid = true
        }
        mObserver = NotifyingDataSetObserver()
        cursor?.registerDataSetObserver(mObserver)
    }

    override fun getItemCount(): Int {
        if (mDataValid && cursor != null) {
            return cursor.count
        }
    }

    private inner class NotifyingDataSetObserver : DataSetObserver() {
        override fun onChanged() {
            super.onChanged()
            mDataValid = true
            notifyDataSetChanged()
        }

        override fun onInvalidated() {
            super.onInvalidated()
            mDataValid = false
            notifyDataSetChanged()
        }
    }

}