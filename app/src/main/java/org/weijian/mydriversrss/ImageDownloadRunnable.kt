package org.weijian.mydriversrss

import java.io.EOFException
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by weijian on 16-4-14.
 */

class ImageDownloadRunnable constructor(task: ImageTask) : Runnable {
    var mTask: ImageTask get set

    var buffer: ByteArray
    var url: String

    init {
        mTask = task
        buffer = task.byteBuffer
        url = task.url
    }


    override fun run() {
        // Set current thread to background
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND)
        // Get buffer from DownloadTask
        var bufferLeft = buffer.size
        if (Thread.interrupted()) {
            mTask.taskFinish(ImageTask.TASK_INTERRUPTED)
            return
        }
        var httpConnection: HttpURLConnection? = null
        try {
            httpConnection = URL(url).openConnection() as HttpURLConnection
            httpConnection.setRequestProperty("User-Agent", Constants.USER_AGENT)
            var inputStream = httpConnection.inputStream
            val contentSize = httpConnection.contentLength
            if (contentSize == -1) {
                outer@do {
                    while (bufferLeft > 0) {
                        val count = inputStream.read(buffer, mTask.byteOffset, bufferLeft)
                        if (count < 0) {
                            break@outer
                        }
                        mTask.byteOffset += count
                        bufferLeft -= count
                        if (Thread.interrupted()) {
                            mTask.taskFinish(ImageTask.TASK_INTERRUPTED)
                            return
                        }
                    }
                    if (Thread.interrupted()) {
                        mTask.taskFinish(ImageTask.TASK_INTERRUPTED)
                        return
                    }
                    buffer = mTask.expandBuffer()
                    bufferLeft = buffer.size - mTask.byteOffset
                } while (true)
            } else {
                // Expanded buffer if buffer isn't big enough
                if (buffer.size < contentSize) {
                    buffer = ByteArray(contentSize)
                    mTask.byteBuffer = buffer
                }
                mTask.byteOffset = 0
                bufferLeft = contentSize
                while (mTask.byteOffset < contentSize) {
                    val count = inputStream.read(buffer, mTask.byteOffset, bufferLeft)
                    if (count < 0) {
                        throw EOFException()
                    }
                    mTask.byteOffset += count
                    bufferLeft -= count
                    if (Thread.interrupted()) {
                        mTask.taskFinish(ImageTask.TASK_INTERRUPTED)
                        return
                    }
                }
            }
            mTask.taskFinish(ImageTask.TASK_DOWNLOAD_COMPLETE)
        } catch (e: Exception) {
            mTask.taskFinish(ImageTask.TASK_DOWNLOAD_FAILED)
        } finally {
            httpConnection?.disconnect()
        }
    }
}