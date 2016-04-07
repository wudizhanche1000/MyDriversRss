package org.weijian.mydriversrss

import android.content.*
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.provider.BaseColumns
import org.weijian.mydriversrss.NewsProviderContract.*


class NewsProvider constructor() : ContentProvider() {

    private var mProviderHelper: SQLiteOpenHelper? = null


    companion object {
        const val DATABASE_NAME = "NewsData"
        const val DATABASE_VERSION = 6
        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_TITLE = RssPullParser.TITLE
        const val COLUMN_LINK = RssPullParser.LINK
        const val COLUMN_DESCRIPTION = RssPullParser.DESCRIPTION
        const val COLUMN_AUTHOR = RssPullParser.AUTHOR
        const val COLUMN_CATEGORY = RssPullParser.CATEGORY
        const val COLUMN_COMMENTS = RssPullParser.COMMENTS
        const val COLUMN_GUID = RssPullParser.GUID
        const val COLUMN_PUBDATE = RssPullParser.PUBDATE

        private const val CREATE_NEWS_TABLE_SQL = """CREATE TABLE $NEWS_TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_GUID TEXT UNIQUE,
                $COLUMN_TITLE TEXT,
                $COLUMN_LINK TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_AUTHOR TEXT,
                $COLUMN_CATEGORY TEXT,
                $COLUMN_COMMENTS TEXT,
                $COLUMN_PUBDATE DATE)"""
        private const val DROP_NEWS_TABLE = "DROP TABLE IF EXISTS $NEWS_TABLE_NAME"
        val mUriMatcher: UriMatcher

        init {
            mUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            mUriMatcher.addURI(AUTHORITY, NEWS_TABLE_NAME, NEWS_ALL)
            mUriMatcher.addURI(AUTHORITY, NEWS_TABLE_NAME + "/#", NEWS_ONE)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        // Implement this to handle requests to delete one or more rows.
        var db = mProviderHelper?.writableDatabase
        var _id = -1L
        when (mUriMatcher.match(uri)) {
            NewsProviderContract.NEWS_ONE -> {
                _id = ContentUris.parseId(uri)
            }
        }
        val count = db?.delete(NEWS_TABLE_NAME, selection, selectionArgs)
        if (count != null)
            return count!!
        else return -1
    }

    override fun getType(uri: Uri): String? {
        when (mUriMatcher.match(uri)) {
            NEWS_ALL -> return MIME_TYPE_NEWS
            NEWS_ONE -> return MIME_TYPE_NEWS_SINGLE
            else -> throw IllegalArgumentException("Unknown URI:" + uri)
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        // TODO: Implement this to handle requests to insert a new row.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun bulkInsert(uri: Uri, newsArray: Array<out ContentValues>): Int {
        val id = mUriMatcher.match(uri)
        var count = 0
        when (id) {
            NEWS_ALL -> {
                var db = mProviderHelper!!.writableDatabase
                db.beginTransaction()
                var count = 0
                try {
                    for (news in newsArray) {
                        val id = db.insertWithOnConflict(NEWS_TABLE_NAME, null, news, SQLiteDatabase.CONFLICT_IGNORE)
                        if (id != -1L)
                            count++
                    }
                    db.setTransactionSuccessful()
                } finally {
                    db.endTransaction()
                }
            }
            else -> {
                count = -1
            }
        }
        context.contentResolver.notifyChange(NEWS_CONTENT_URI, null)
        return count
    }

    override fun onCreate(): Boolean {
        // TODO: Implement this to initialize your content provider on startup.
        mProviderHelper = NewsProviderHelper(context)
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        // TODO: Implement this to handle query requests from clients.
        var db = mProviderHelper!!.readableDatabase
        val cursor = db.query(NEWS_TABLE_NAME, projection, selection, selectionArgs, null, null, COLUMN_ID)
        return cursor
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        // TODO: Implement this to handle requests to update one or more rows.
        throw UnsupportedOperationException("Not yet implemented")
    }

    inner class NewsProviderHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,
            null, DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(CREATE_NEWS_TABLE_SQL)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL(DROP_NEWS_TABLE)
            db.execSQL(CREATE_NEWS_TABLE_SQL)
        }

    }
}
