package org.weijian.mydriversrss

/**
 * Created by weijian on 16-3-30.
 */
import android.content.ContentValues
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.text.SimpleDateFormat


class RssPullParser {
    companion object {
    }

    fun parse(input: InputStream, encoding: String = "UTF-8"): List<ContentValues> {
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = false
        var parser = factory.newPullParser()
        parser.setInput(input, encoding)
        var eventType = parser.eventType
        var newsList = mutableListOf<ContentValues>()
        var contentValues: ContentValues? = null
        loop@while (true) {
            var eventName = parser.name
            when (eventType) {
                XmlPullParser.START_DOCUMENT -> {
                }
                XmlPullParser.START_TAG -> {
                    if (eventName.equals(Constants.RSS_ITEM, true)) {
                        contentValues = ContentValues(9)
                    }
                    if (eventName.equals(Constants.RSS_ITEM_TITLE, true)) {
                        contentValues?.put(Constants.RSS_ITEM_TITLE, parser.nextText())
                    } else if (eventName.equals(Constants.RSS_ITEM_LINK, true) ) {
                        contentValues?.put(Constants.RSS_ITEM_LINK, parser.nextText())
                    } else if (eventName.equals(Constants.RSS_ITEM_DESCRIPTION, true)) {
                        contentValues?.put(Constants.RSS_ITEM_DESCRIPTION, parser.nextText())
                    } else if (eventName.equals(Constants.RSS_ITEM_AUTHOR, true) ) {
                        contentValues?.put(Constants.RSS_ITEM_AUTHOR, parser.nextText())
                    } else if (eventName.equals(Constants.RSS_ITEM_CATEGORY, true) ) {
                        contentValues?.put(Constants.RSS_ITEM_CATEGORY, parser.nextText())
                    } else if (eventName.equals(Constants.RSS_ITEM_COMMENTS, true) ) {
                        contentValues?.put(Constants.RSS_ITEM_COMMENTS, parser.nextText())
                    } else if (eventName.equals(Constants.RSS_ITEM_GUID, true) ) {
                        contentValues?.put(Constants.RSS_ITEM_GUID, parser.nextText())
                    } else if (eventName.equals(Constants.RSS_ITEM_PUBDATE, true)) {
                        var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        val date = dateFormat.parse(parser.nextText())
                        contentValues?.put(Constants.RSS_ITEM_PUBDATE, dateFormat.format(date))
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (eventName.equals(Constants.RSS_ITEM, true)) {
                        if (contentValues != null) {
                            newsList.add(contentValues)
                            contentValues = null
                        }
                    }
                }
                XmlPullParser.END_DOCUMENT -> break@loop
            }
            eventType = parser.next()
        }
        return newsList
    }
}
