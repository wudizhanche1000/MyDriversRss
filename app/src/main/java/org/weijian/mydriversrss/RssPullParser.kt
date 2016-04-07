package org.weijian.mydriversrss

/**
 * Created by weijian on 16-3-30.
 */
import android.content.ContentValues
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

data class News(var title: String? = null, var link: String? = null, var description: String? = null,
                var author: String? = null, var category: String? = null, var comment: String? = null,
                var guid: String? = null, var pubDate: Date? = null) {
}


class RssPullParser {
    companion object {
        const val ITEM = "ITEM"
        const val TITLE = "TITLE"
        const val LINK = "LINK"
        const val DESCRIPTION = "DESCRIPTION"
        const val AUTHOR = "AUTHOR"
        const val CATEGORY = "CATEGORY"
        const val COMMENTS = "COMMENTS"
        const val GUID = "GUID"
        const val PUBDATE = "PUB_DATE"
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
                    if (eventName.equals(ITEM, true)) {
                        contentValues = ContentValues(9)
                    }
                    if (eventName.equals(TITLE, true)) {
                        contentValues?.put(TITLE, parser.nextText())
                    } else if (eventName.equals(LINK, true) ) {
                        contentValues?.put(LINK, parser.nextText())
                    } else if (eventName.equals(DESCRIPTION, true)) {
                        contentValues?.put(DESCRIPTION, parser.nextText())
                    } else if (eventName.equals(AUTHOR, true) ) {
                        contentValues?.put(AUTHOR, parser.nextText())
                    } else if (eventName.equals(CATEGORY, true) ) {
                        contentValues?.put(CATEGORY, parser.nextText())
                    } else if (eventName.equals(COMMENTS, true) ) {
                        contentValues?.put(COMMENTS, parser.nextText())
                    } else if (eventName.equals(GUID, true) ) {
                        contentValues?.put(GUID, parser.nextText())
                    } else if (eventName.equals(PUBDATE, true)) {
                        var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        val date = dateFormat.parse(parser.nextText())
                        contentValues?.put(PUBDATE, dateFormat.format(date))
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (eventName.equals(ITEM, true)) {
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
