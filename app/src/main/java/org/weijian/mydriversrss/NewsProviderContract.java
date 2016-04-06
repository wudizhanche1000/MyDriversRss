package org.weijian.mydriversrss;

import android.net.Uri;

/**
 * Created by wudiz on 2016/4/7.
 */
public class NewsProviderContract {
    public static final String SCHEME = "content";
    public static final String AUTHORITY = "org.weijian.mydriversrss.NewsProvider";
    public static final Uri COTENT_URI = Uri.parse(String.format("%s://%s", SCHEME, AUTHORITY));
    public static final String NEWS_TABLE_NAME = "NewsData";
    public static final int NEWS_URI_CODE = 1;

}
