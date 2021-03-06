package org.weijian.mydriversrss

import android.os.Build
import java.util.*

/**
 * Created by weijian on 16-4-5.
 */

object Constants {
    val USER_AGENT = "Mozilla/5.0 (Linux; Android ${android.os.Build.VERSION.RELEASE}; ${Locale.getDefault().toString()}; ${Build.DEVICE} Build/${Build.ID})"
    const val RSS_SIGN_ID = "RSS_SIGN_ID"
    const val RSS_XAID = "RSS_XAID"
    const val RSS_UDID = "RSS_UDID"
    const val RSS_MINID = "RSS_MINID"
    const val RSS_TID = "RSS_TID"
    const val RSS_CID = "RSS_CID"
    const val RSS_ADDRESS =
            "http://dt.kkeji.com/api/2/contents?istop=0&sign=%s&xaid=%s&udid=%s&minid=%s&tid=1&cid=0"

    const val EXTENDED_DATA_STATUS = "org.weijian.mydriversrss.STATUS"
    const val BROADCAST_ACTION = "org.weijian.mydriversrss.BROADCAST"

    const val NEWS_ITEM_TYPE_NO_IMAGE = 0
    const val NEWS_ITEM_TYPE_IMAGE = 1
    const val NEWS_ITEM_TYPE_MULTI_IMAGES = 3

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
    const val STATE_ACTION_FAILED = 5

    const val NEWS_ITEM_RESULT = "NEWS_ITEM_RESULT" // represents result tag in RssPullService

    const val STATE_FETCH_COMPLETE = 0
    const val STATE_FETCH_FAILED = 1

    const val RECYCLER_ACTION = "RECYCLER_ACTION"
    const val RECYCLER_ACTION_GET = 0
    const val RECYCLER_ACTION_REFRESH = 1
}