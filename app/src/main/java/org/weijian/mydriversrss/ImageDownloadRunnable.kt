package org.weijian.mydriversrss

import java.io.EOFException
import java.net.URL
import java.net.URLConnection

/**
 * Created by weijian on 16-4-14.
 */

class ImageDownloadRunnable constructor(downloadTask: ImageDownloadTask) : Runnable {
    var mDownloadTask: ImageDownloadTask get set

    init {
        mDownloadTask = downloadTask
    }

    override fun run() {
        // Set current thread to background
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND)
        // Get buffer from DownloadTask
        var buffer = mDownloadTask.byteBuffer
        var bufferLeft = buffer.size
        if (Thread.interrupted()) {
            mDownloadTask.taskFinish(ImageDownloadTask.TASK_DOWNLOAD_INTERRUPTED)
            return
        }
        var httpConnection: URLConnection? = null
        try {
            httpConnection = URL(mDownloadTask.url).openConnection()
            httpConnection.setRequestProperty("User-Agent", Constants.USER_AGENT)
            var inputStream = httpConnection.inputStream
            val contentSize = httpConnection.contentLength
            if (contentSize == -1) {
                outer@do {
                    while (bufferLeft > 0) {
                        val count = inputStream.read(buffer, mDownloadTask.byteOffset, bufferLeft)
                        if (count < 0) {
                            break@outer
                        }
                        mDownloadTask.byteOffset += count
                        bufferLeft -= count
                        if (Thread.interrupted()) {
                            mDownloadTask.taskFinish(ImageDownloadTask.TASK_DOWNLOAD_INTERRUPTED)
                            return
                        }
                    }
                    if (Thread.interrupted()) {
                        mDownloadTask.taskFinish(ImageDownloadTask.TASK_DOWNLOAD_INTERRUPTED)
                        return
                    }
                    buffer = mDownloadTask.expandBuffer()
                    bufferLeft = buffer.size - mDownloadTask.byteOffset
                } while (true)
            } else {
                // Expanded buffer if buffer isn't big enough
                if (buffer.size < contentSize) {
                    buffer = ByteArray(contentSize)
                    mDownloadTask.byteBuffer = buffer
                }
                mDownloadTask.byteOffset = 0
                bufferLeft = contentSize
                while (mDownloadTask.byteOffset < contentSize) {
                    val count = inputStream.read(buffer, mDownloadTask.byteOffset, bufferLeft)
                    if (count < 0) {
                        throw EOFException()
                    }
                    mDownloadTask.byteOffset += count
                    bufferLeft -= count
                    if (Thread.interrupted()) {
                        mDownloadTask.taskFinish(ImageDownloadTask.TASK_DOWNLOAD_INTERRUPTED)
                        return
                    }
                }
            }
            mDownloadTask.taskFinish(ImageDownloadTask.TASK_DOWNLOAD_COMPLETE)
        } catch (e: Exception) {
            mDownloadTask.taskFinish(ImageDownloadTask.TASK_DOWNLOAD_FAILED)
        } finally {
            httpConnection?.inputStream?.close()
        }
    }


}