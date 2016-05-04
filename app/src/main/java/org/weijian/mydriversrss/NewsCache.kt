package org.weijian.mydriversrss

import android.content.Context
import android.os.Environment
import android.util.LruCache
import com.jakewharton.disklrucache.DiskLruCache

/**
 * Created by weijian on 5/3/16.
 */
class NewsCache constructor(val context: Context) {
    companion object {
        const val MEMORY_CACHE_SIZE = 8 * 1024 * 1024
        const val DISK_CACHE_SIZE = 128L * 1024L * 1024L
    }

    val mCache = object : LruCache<Int, ByteArray> (MEMORY_CACHE_SIZE) {
        override fun sizeOf(key: Int, value: ByteArray): Int {
            return value.size
        }
    }
    val mDiskCache: DiskLruCache

    init {
        var cachePath = context.cacheDir
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = context.externalCacheDir
        }
        mDiskCache = DiskLruCache.open(cachePath, 1, 1, DISK_CACHE_SIZE)
    }

    fun getNews(id: Int): News? {
        val data = mCache.get(id)
        if (data != null) {

        } else {
            val cache = mDiskCache.get(id.toString())
        }
        return null
    }

}
