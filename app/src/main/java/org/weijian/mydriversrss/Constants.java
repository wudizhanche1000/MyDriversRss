package org.weijian.mydriversrss;

import android.os.Build;

import java.util.Locale;

/**
 * Created by weijian on 16-4-5.
 */

public class Constants {
    public static final String USER_AGENT = "Mozilla/5.0 (Linux; Android "
            + android.os.Build.VERSION.RELEASE + "; "
            + Locale.getDefault().toString() + "; "
            + Build.DEVICE + " Build/" + Build.ID + ")";

    public static final String RSS_SIGN_ID = "RSS_SIGN_ID";
    public static final String RSS_XAID = "RSS_XAID";
    public static final String RSS_UDID = "RSS_UDID";
    public static final String RSS_MINID = "RSS_MINID";
    public static final String RSS_TID = "RSS_TID";
    public static final String RSS_CID = "RSS_CID";

    public static final String RSS_ADDRESS =
            "http://dt.kkeji.com/api/2/contents?istop=0&sign=%s&xaid=%s&udid=%s&minid=%s&tid=1&cid=0";
    public static final String EXTENDED_DATA_STATUS = "org.weijian.mydriversrss.STATUS";
    public static final String BROADCAST_ACTION = "org.weijian.mydriversrss.BROADCAST";
    public static final int NEWS_ITEM_TYPE_NO_IMAGE = 0;
    public static final int NEWS_ITEM_TYPE_IMAGE = 1;
    public static final int NEWS_ITEM_TYPE_MULTI_IMAGES = 3;
    public static final String RSS_ITEM = "ITEM";
    public static final String RSS_ITEM_TITLE = "TITLE";
    public static final String RSS_ITEM_LINK = "LINK";
    public static final String RSS_ITEM_DESCRIPTION = "DESCRIPTION";
    public static final String RSS_ITEM_AUTHOR = "AUTHOR";
    public static final String RSS_ITEM_CATEGORY = "CATEGORY";
    public static final String RSS_ITEM_COMMENTS = "COMMENTS";
    public static final String RSS_ITEM_GUID = "GUID";
    public static final String RSS_ITEM_PUBDATE = "PUBDATE";
    public static final int STATE_ACTION_STARTING = 1;
    public static final int STATE_ACTION_RETRIEVED = 2;
    public static final int STATE_ACTION_WRITING = 3;
    public static final int STATE_ACTION_COMPLETE = 4;
    public static final int STATE_ACTION_FAILED = 5;
    public static final String NEWS_ITEM_RESULT = "NEWS_ITEM_RESULT"; // represents result tag in RssPullService
    public static final int STATE_FETCH_COMPLETE = 0;
    public static final int STATE_FETCH_FAILED = 1;
    public static final String RECYCLER_ACTION = "RECYCLER_ACTION";
    public static final int RECYCLER_ACTION_GET = 0;
    public static final int RECYCLER_ACTION_REFRESH = 1;
}