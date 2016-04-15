package org.weijian.mydriversrss

/**
 * Created by weijian on 16-4-14.
 */
class ImageTask constructor(var imageManager: ImageManager) {

    companion object {
        // 16 KB for buffer
        const val BUFFER_SIZE = 16 * 1024
        const val MAXIMUM_BUFFER_SIZE = 64 * 1024
        const val TASK_DOWNLOAD_STARTING = 0
        const val TASK_DOWNLOAD_DOWNLODING = 1
        const val TASK_DOWNLOAD_COMPLETE = 2
        const val TASK_DOWNLOAD_FAILED = 3
        const val TASK_DECODE_START = 4
        const val TASK_DECODE_COMPLETE = 5
        const val TASK_DECODE_FAILED = 6
        const val TASK_INTERRUPTED = 7
    }

    lateinit var decodeRunnable: ImageDecodeRunnable
    lateinit var downloadRunnable: ImageDownloadRunnable
    lateinit var byteBuffer: ByteArray get set
    var byteOffset: Int = 0
        get set
    lateinit var url: String
        get set

    var targetHeight: Int = 0
    var targetWidth: Int = 0

    init {
    }


    fun initializeTask(url: String) {
        this.targetHeight = targetHeight
        this.targetWidth = targetWidth
        this.url = url
        byteBuffer = ByteArray(BUFFER_SIZE)
        downloadRunnable = ImageDownloadRunnable(this)
        decodeRunnable = ImageDecodeRunnable(this)
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

    fun getImageData(): ByteArray {
        val newBuffer = ByteArray(this.byteOffset)
        System.arraycopy(byteBuffer, 0, newBuffer, 0, byteOffset)
        return newBuffer
    }

    fun cleanTask() {
        byteOffset = 0
        // Clean the task
        if (byteBuffer.size > MAXIMUM_BUFFER_SIZE) {
            byteBuffer = ByteArray(BUFFER_SIZE)
        }
    }

    fun taskFinish(state: Int) {
        imageManager.handleState(this, state)
    }

}