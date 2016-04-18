package org.weijian.mydriversrss

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Message
import android.util.LruCache
import java.lang.ref.WeakReference
import java.security.MessageDigest
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by weijian on 16-4-1.
 */
object ImageManager {
    interface ImageChangedListener {
        fun onComplete(url: String, bitmap: Bitmap)
        fun onFailed(reason: Int)
        fun onImageChange(url: String, bitmap: Bitmap)
    }

    // 8MB for memory cache
    const val MEMORY_CACHE_SIZE = 8 * 1024 * 1024
    const val CORE_POOL_SIZE = 4
    const val MAXIMUM_POOL_SIZE = 8
    const val KEEP_LIVE_TIME = 30L

    val mCache: LruCache<String, Bitmap>
    //    val mDiskCache:DiskLruCache
    val mDownloadQueue: LinkedBlockingQueue<Runnable>
    val mThreadPool: ThreadPoolExecutor
    val mHandler: DownloadHandler
    val mListeners: HashMap<String, ImageChangedListener>

    init {
        mHandler = DownloadHandler(this)
        mListeners = HashMap()
        mCache = object : LruCache<String, Bitmap>(MEMORY_CACHE_SIZE) {
            override fun sizeOf(key: String, value: Bitmap): Int {
                return value.byteCount
            }
        }
        mDownloadQueue = LinkedBlockingQueue()
        mThreadPool = ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_LIVE_TIME, TimeUnit.SECONDS,
                mDownloadQueue)
    }

    fun handleState(task: ImageTask, state: Int) {
        when (state) {
            ImageTask.TASK_DOWNLOAD_FAILED -> {
            }
            ImageTask.TASK_DOWNLOAD_COMPLETE -> {
                val message = mHandler.obtainMessage(state, task)
                message.sendToTarget()
            }
            ImageTask.TASK_INTERRUPTED -> {
            }
        }
    }

    fun getHexString(s: String, algorithm: String = "MD5"): String {
        val digester = MessageDigest.getInstance(algorithm)
        digester.update(s.toByteArray())
        val hash = digester.digest()
        val stringBuilder = StringBuilder()
        for (byte in hash) {
            if (byte < 0x10)
                stringBuilder.append('0')
            stringBuilder.append(Integer.toHexString(byte.toInt()))
        }
        return stringBuilder.toString()
    }

    fun setImageChangedListener(url: String, listener: ImageChangedListener) {
        // If already have a Listener
        if (mListeners[url] != null) {
            return
        }
        mListeners[url] = listener
    }

    fun removeImageChangedListener(url: String) {
        mListeners.remove(url)
    }

    fun getBitmap(url: String): Bitmap? {
        val md5Hex = getHexString(url)
        val bitmap = mCache.get(md5Hex)
        if (bitmap != null) {
            return bitmap
        } else {
            startDownload(url)
            return null
        }
    }

    fun startDownload(url: String) {
        val task = ImageTask(this)
        mCache.get(url)
        task.initializeTask(url)
        mThreadPool.execute(task.downloadRunnable)
    }

    class DownloadHandler constructor(manager: ImageManager) : Handler() {
        private val mImageManager: WeakReference<ImageManager>

        init {
            mImageManager = WeakReference(manager)
        }

        override fun handleMessage(msg: Message) {
            val task = msg.obj as ImageTask
            when (msg.what) {
                ImageTask.TASK_DOWNLOAD_COMPLETE -> {
                    if (mListeners.containsKey(task.url)) {
                        val listener = mListeners[task.url]
                        val imageData = task.getImageData()
                        val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                        mCache.put(getHexString(task.url), bitmap)
                        listener?.onComplete(task.url, bitmap)
                    }
                }
            }
            super.handleMessage(msg)
        }
    }
}