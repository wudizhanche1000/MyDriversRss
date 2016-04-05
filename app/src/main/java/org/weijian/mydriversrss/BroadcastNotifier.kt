package org.weijian.mydriversrss

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager

/**
 * Created by weijian on 16-4-5.
 */
class BroadcastNotifier(val context: Context) {
    private var mBroadcastManager: LocalBroadcastManager = LocalBroadcastManager.getInstance(context)
    fun broadcastIntentWithStatus(status: Int) {
        var localIntent = Intent();
        localIntent.action = Constants.BROADCAST_ACTION
        localIntent.putExtra(Constants.EXTENDED_DATA_STATUS, status)
        localIntent.addCategory(Intent.CATEGORY_DEFAULT)
        mBroadcastManager.sendBroadcast(localIntent)
    }

}