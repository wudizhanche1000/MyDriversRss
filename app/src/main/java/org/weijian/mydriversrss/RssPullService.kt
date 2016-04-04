package org.weijian.mydriversrss

import android.app.IntentService
import android.content.Intent
import java.io.BufferedInputStream
import java.net.URL

/**
 * Created by weijian on 16-4-1.
 */
class RssPullService constructor() : IntentService("RssPullService") {
    companion object{
        const val BROADCAST_ACTION="org.weijian.mydriversrss.BROADCAST"
        const val EXTENDED_DATA_STATUS="com.weijian.mydriversrss.STATUS"
        const val RSS_ADDRESS="http://rss.mydrivers.com/rss.aspx?Tid=1"
    }
    override fun onHandleIntent(intent: Intent) {
        val dataString = intent.dataString
        val url= URL(RSS_ADDRESS)
        val httpConnection=url.openConnection()
        val inputStream=BufferedInputStream(httpConnection.inputStream)
//        var localIntent=Intent(BROADCAST_ACTION).putExtra(EXTENDED_DATA_STATUS,status)
//        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);


    }

}
