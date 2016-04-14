package org.weijian.mydriversrss

import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Message
import android.util.LruCache
import java.lang.ref.WeakReference
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
    const val MAXIMUM_POOL_SIZE = 8
    const val KEEP_LIVE_TIME = 30L

    val mCache: LruCache<String, ByteArray>
    val mDownloadQueue: LinkedBlockingQueue<Runnable>
    val mThreadPool: ThreadPoolExecutor
    val mHandler: DownloadHandler

    class DownloadHandler constructor(manager: ImageManager) : Handler() {
        private val mImageManager: WeakReference<ImageManager>

        init {
            mImageManager = WeakReference(manager)
        }

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
        }
    }

    init {
        mHandler = DownloadHandler(this)
        mCache = LruCache(MEMORY_CACHE_SIZE)
        mDownloadQueue = LinkedBlockingQueue()
        mThreadPool = ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_LIVE_TIME, TimeUnit.SECONDS,
                mDownloadQueue)
    }


    fun handleState(task: ImageTask, state: Int) {
        when (state) {
            ImageTask.TASK_DOWNLOAD_FAILED -> {
            }
            ImageTask.TASK_DOWNLOAD_COMPLETE -> {
                mCache.put(task.url, task.getImageData())
                mHandler.obtainMessage(state, task)
            }
            ImageTask.TASK_DOWNLOAD_INTERRUPTED -> {
            }
        }
    }

    fun getDrawable(url: String): Drawable {

        return Drawable.createFromPath(null)
    }

    fun startDownload(url: String) {
        val task = ImageTask(this)
        mCache.get(url)
        task.initializeTask(url)
        mThreadPool.execute(task)
    }
}