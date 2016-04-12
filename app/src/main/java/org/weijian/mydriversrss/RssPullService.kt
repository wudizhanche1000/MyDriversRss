package org.weijian.mydriversrss

import android.app.IntentService
import android.content.Intent
import java.io.BufferedInputStream
import java.net.URL
import java.nio.charset.Charset

/**
 * Created by weijian on 16-4-1.
 */
class RssPullService constructor() : IntentService("RssPullService") {
    companion object {

        const val CONNECTION_TIMEOUT = 20000 // 20000 milliseconds
        const val READ_TIMEOUT = 30000 // 30000 milliseconds
    }

    private lateinit var mBroadcastNotifier: BroadcastNotifier
    override fun onHandleIntent(intent: Intent) {

        val signId = intent.getStringExtra(Constants.RSS_SIGN_ID)
        val xaId = intent.getStringExtra(Constants.RSS_XAID)
        val udId = intent.getStringExtra(Constants.RSS_UDID)
        val minId = intent.getStringExtra(Constants.RSS_MINID)

        mBroadcastNotifier = BroadcastNotifier(this)
        val url = URL(Constants.RSS_ADDRESS.format(signId, xaId, udId, minId))
        mBroadcastNotifier.broadcastIntentWithStatus(Constants.STATE_ACTION_STARTING)
        val httpConnection = url.openConnection()
        httpConnection.connectTimeout = CONNECTION_TIMEOUT
        httpConnection.readTimeout = READ_TIMEOUT
        try {
            val inputStream = BufferedInputStream(httpConnection.inputStream)
            var json = String(inputStream.readBytes(), Charset.forName("UTF-8"))
            val parser = JsonPullParser()
            val newsList = parser.parse(json)
        } catch(e: Exception) {
            // TODO Handle SocketTimeoutException and IOException
            throw e
        }
        mBroadcastNotifier.broadcastIntentWithStatus(Constants.STATE_ACTION_RETRIEVED)
        mBroadcastNotifier.broadcastIntentWithStatus(Constants.STATE_ACTION_COMPLETE)
    }
}
