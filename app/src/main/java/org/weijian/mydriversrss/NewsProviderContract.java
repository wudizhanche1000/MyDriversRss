package org.weijian.mydriversrss;

import android.net.Uri;

/**
 * Created by wudiz on 2016/4/7.
 */
public class NewsProviderContract {
    public static final String SCHEME = "content";
    public static final String AUTHORITY = "org.weijian.mydriversrss.NewsProvider";
    public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY);
    public static final String NEWS_TABLE_NAME = "NewsData";
    public static final Uri NEWS_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, NEWS_TABLE_NAME);
    public static final int NEWS_ONE = 1;
    public static final int NEWS_ALL = 2;
    public static final String MIME_TYPE_NEWS_SINGLE = "vnd.android.cursor.item/vnd.org.weijian.mydriversrss.News";
    public static final String MIME_TYPE_NEWS = "vnd.android.cursor.dir/vnd.org.weijian.mydriversrss.News";

}
