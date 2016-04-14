package org.weijian.mydriversrss

import android.util.LruCache
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by weijian on 16-4-1.
 */
object ImageManager {

    // 8MB for memory cache
    const val MEMORY_CACHE_SIZE = 8 * 1024 * 1024

    const val CORE_POOL_SIZE = 4
    const val MAXIMIUM_POOL_SIZE = 8
    const val KEEP_LIVE_TIME = 30L

    val mCache: LruCache<String, ByteArray>
    val mDownloadQueue: LinkedBlockingQueue<Runnable>
    val mThreadPool: ThreadPoolExecutor

    init {
        mCache = LruCache(MEMORY_CACHE_SIZE)
        mDownloadQueue = LinkedBlockingQueue()
        mThreadPool = ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMIUM_POOL_SIZE, KEEP_LIVE_TIME, TimeUnit.SECONDS,
                mDownloadQueue)
    }

    fun startDownload(url: String) {
        val task = ImageDownloadTask()
        mCache.get(url)
        task.url = url
        task.downloadRunnable = ImageDownloadRunnable(task)
        mThreadPool.execute(task.downloadRunnable)


    }
}