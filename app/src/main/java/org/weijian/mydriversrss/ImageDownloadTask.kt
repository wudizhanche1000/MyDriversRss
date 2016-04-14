package org.weijian.mydriversrss

import android.os.Handler
import android.util.Log

/**
 * Created by weijian on 16-4-14.
 */
class ImageDownloadTask {
    class TaskHandler : Handler() {

    }

    companion object {
        // 32KB for buffer
        const val BUFFER_SIZE = 32 * 1024
        const val READ_BUFFER_SIZE = 4 * 1024
        const val MAXIMUM_BUFFER_SIZE = 128 * 1024

        const val TASK_DOWNLOAD_COMPLETE = 0
        const val TASK_DOWNLOAD_FAILED = 1
        const val TASK_DOWNLOAD_INTERRUPTED = 2
    }


    var byteBuffer: ByteArray get set
    var byteOffset: Int = 0
        get set
    var url: String? = null
        get set

    var downloadRunnable: ImageDownloadRunnable? = null

    init {
        byteBuffer = ByteArray(BUFFER_SIZE)
    }

    fun initializeTask(url: String, byteCache: ByteArray) {

    }

    /**
     * This method expand Task byteBuffer size to contain more data
     *
     * @return reference of new byteBuffer
     */
    fun expandBuffer(): ByteArray {
        val newBuffer = ByteArray(byteBuffer.size * 2)
        System.arraycopy(byteBuffer, 0, newBuffer, 0, byteOffset)
        byteBuffer = newBuffer
        return byteBuffer
    }


    fun taskFinish(state: Int) {

        Log.d("DOWNLOAD_TASK", "$state ${this.byteOffset}")
        if (byteBuffer.size > MAXIMUM_BUFFER_SIZE) {
            byteBuffer = ByteArray(BUFFER_SIZE)
        }
    }


}