package org.weijian.mydriversrss

import android.content.*
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.provider.BaseColumns
import org.weijian.mydriversrss.NewsProviderContract.*


class NewsProvider constructor() : ContentProvider() {

    private lateinit var mProviderHelper: SQLiteOpenHelper


    companion object {
        const val DATABASE_NAME = "NewsData.db"
        const val NEWS_TABLE_NAME = "News"
        const val DATABASE_VERSION = 9
        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_TITLE = Constants.RSS_ITEM_TITLE
        const val COLUMN_LINK = Constants.RSS_ITEM_LINK
        const val COLUMN_DESCRIPTION = Constants.RSS_ITEM_DESCRIPTION
        const val COLUMN_AUTHOR = Constants.RSS_ITEM_AUTHOR
        const val COLUMN_CATEGORY = Constants.RSS_ITEM_CATEGORY
        const val COLUMN_COMMENTS = Constants.RSS_ITEM_COMMENTS
        const val COLUMN_GUID = Constants.RSS_ITEM_GUID
        const val COLUMN_PUBDATE = Constants.RSS_ITEM_PUBDATE

        private const val CREATE_NEWS_TABLE_SQL = """CREATE TABLE $NEWS_TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_GUID TEXT UNIQUE,
                $COLUMN_TITLE TEXT,
                $COLUMN_LINK TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_AUTHOR TEXT,
                $COLUMN_CATEGORY TEXT,
                $COLUMN_COMMENTS TEXT,
                $COLUMN_PUBDATE TEXT)"""
        private const val DROP_NEWS_TABLE_SQL = "DROP TABLE IF EXISTS $NEWS_TABLE_NAME"
        val mUriMatcher: UriMatcher

        init {
            mUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            mUriMatcher.addURI(AUTHORITY, NEWS_TABLE_NAME, NEWS_ALL)
            mUriMatcher.addURI(AUTHORITY, NEWS_TABLE_NAME + "/#", NEWS_ONE)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        // Implement this to handle requests to delete one or more rows.
        var db = mProviderHelper.writableDatabase
        var _id = -1L
        when (mUriMatcher.match(uri)) {
            NewsProviderContract.NEWS_ONE -> {
                _id = ContentUris.parseId(uri)
            }
        }
        val count = db.delete(NEWS_TABLE_NAME, selection, selectionArgs)
        return count
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
        var count = 0
        when (mUriMatcher.match(uri)) {
            NEWS_ALL -> {
                var db = mProviderHelper.writableDatabase
                // rowsCount before insert
                val rowsCount = DatabaseUtils.queryNumEntries(db, NEWS_TABLE_NAME)
                db.beginTransaction()
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
                val rowsAfterInsert = DatabaseUtils.queryNumEntries(db, NEWS_TABLE_NAME)
                // if insert new rows, notify change
                if (rowsAfterInsert - rowsCount > 0) {
                    count = (rowsAfterInsert - rowsCount).toInt()
                    context.contentResolver.notifyChange(NEWS_CONTENT_URI, null)
                }
            }
            else -> {
                count = -1
            }
        }
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
        var db = mProviderHelper.readableDatabase
        val cursor = db.query(NEWS_TABLE_NAME, projection, selection, selectionArgs, null, null, "$COLUMN_PUBDATE DESC")
        cursor.setNotificationUri(context.contentResolver, NEWS_CONTENT_URI)
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
            db.execSQL(DROP_NEWS_TABLE_SQL)
            db.execSQL(CREATE_NEWS_TABLE_SQL)
        }

    }
}
