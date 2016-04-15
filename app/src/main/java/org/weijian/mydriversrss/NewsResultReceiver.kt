package org.weijian.mydriversrss

import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver

/**
 * Created by weijian on 16-4-15.
 */
class NewsResultReceiver constructor(handler: Handler, receiver: NewsReceiver) : ResultReceiver(handler) {
    companion object {
        const val NEWS_RESULT_RECEIVER = "NEWS_RESULT_RECEIVER"
    }

    interface NewsReceiver {
        fun onReceiveResult(resultCode: Int, resultData: Bundle?)
    }

    var receiver: NewsReceiver

    init {
        this.receiver = receiver
    }

    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
        super.onReceiveResult(resultCode, resultData)
        receiver.onReceiveResult(resultCode, resultData)
    }
}

