package org.weijian.mydriversrss

import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.database.DataSetObserver
import android.net.Uri
import android.os.Handler
import android.provider.BaseColumns
import android.support.v7.widget.RecyclerView

/**
 * Created by wudiz on 2016/4/7.
 */

abstract class CursorRecyclerViewAdapter<VH : RecyclerView.ViewHolder>
constructor(context: Context, cursor: Cursor?) : RecyclerView.Adapter<VH>() {
    var mDataValid = false
    val mContext: Context
    private var mCursor: Cursor? = null
        get() = field
    var mRowIdColumn = -1;
    var mDataSetObserver: DataSetObserver
    var mContentObserver: ContentObserver

    init {
        mContext = context
        mCursor = cursor
        mDataValid = mCursor != null
        mDataSetObserver = NotifyingDataSetObserver()
        mContentObserver = CursorContentObserver()
        if (mCursor != null) {
            mRowIdColumn = mCursor!!.getColumnIndex(BaseColumns._ID)
            mCursor!!.registerContentObserver(mContentObserver)
        }
    }

    override fun getItemCount(): Int {
        if (mCursor != null)
            return mCursor!!.count
        else return 0
    }

    override fun getItemId(position: Int): Long {
        if (mDataValid && mCursor != null && mCursor!!.moveToPosition(position))
            return mCursor!!.getLong(mRowIdColumn)
        return 0
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(true)
    }

    abstract fun onBindViewHolder(viewHolder: VH, cursor: Cursor?)

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (!mDataValid) {
            throw  IllegalStateException("This function should be called when the cursor is valid")
        }
        if (mCursor !== null && !mCursor!!.moveToPosition(position)) {
            throw IllegalStateException("Couldn't move cursor to position $position")
        }
        onBindViewHolder(holder, mCursor)
    }

    fun changeCursor(newCursor: Cursor?) {
        val oldCursor = swapCursor(newCursor)
        oldCursor?.close()
    }

    fun swapCursor(newCursor: Cursor?): Cursor? {
        if (newCursor === mCursor) return null
        val oldCursor = mCursor
        oldCursor?.unregisterDataSetObserver(mDataSetObserver)
        mCursor = newCursor
        mCursor?.registerDataSetObserver(mDataSetObserver)
        mRowIdColumn = mCursor?.getColumnIndex(BaseColumns._ID) ?: -1
        mDataValid = mCursor != null
        notifyDataSetChanged()
        return oldCursor
    }

    private inner class CursorContentObserver : ContentObserver(Handler()) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
        }

        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)
        }

        override fun deliverSelfNotifications(): Boolean {
            return super.deliverSelfNotifications()
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