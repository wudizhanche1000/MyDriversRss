package org.weijian.mydriversrss

/**
 * Created by weijian on 16-4-5.
 */

object Constants {
    const val RSS_ADDRESS = "http://rss.mydrivers.com/rss.aspx?Tid=1"
    const val EXTENDED_DATA_STATUS = "org.weijian.mydriversrss.STATUS"
    const val BROADCAST_ACTION = "org.weijian.mydriversrss.BROADCAST"


    const val RSS_ITEM = "ITEM"
    const val RSS_ITEM_TITLE = "TITLE"
    const val RSS_ITEM_LINK = "LINK"
    const val RSS_ITEM_DESCRIPTION = "DESCRIPTION"
    const val RSS_ITEM_AUTHOR = "AUTHOR"
    const val RSS_ITEM_CATEGORY = "CATEGORY"
    const val RSS_ITEM_COMMENTS = "COMMENTS"
    const val RSS_ITEM_GUID = "GUID"
    const val RSS_ITEM_PUBDATE = "PUBDATE"

    const val STATE_ACTION_STARTING = 1
    const val STATE_ACTION_RETRIEVED = 2
    const val STATE_ACTION_WRITING = 3
    const val STATE_ACTION_COMPLETE = 4
}