package org.weijian.mydriversrss

/**
 * Created by weijian on 16-4-5.
 */

object Constants {
    const val RSS_ADDRESS = "http://rss.mydrivers.com/rss.aspx?Tid=1"
    const val EXTENDED_DATA_STATUS = "org.weijian.mydriversrss.STATUS"
    const val BROADCAST_ACTION = "org.weijian.mydriversrss.BROADCAST"


    const val STATE_ACTION_STARTING = 1
    const val STATE_ACTION_RETRIEVED = 2
    const val STATE_ACTION_WRITING = 3
    const val STATE_ACTION_COMPLETE = 4
}