package org.weijian.mydriversrss

/**
 * Created by weijian on 16-4-14.
 */
class ImageTask constructor(var imageManager: ImageManager) {

    companion object {
        // 16 KB for buffer
        const val BUFFER_SIZE = 16 * 1024
        const val READ_BUFFER_SIZE = 4 * 1024
        const val MAXIMUM_BUFFER_SIZE = 128 * 1024

        const val TASK_DOWNLOAD_COMPLETE = 0
        const val TASK_DOWNLOAD_FAILED = 1
        const val TASK_DOWNLOAD_INTERRUPTED = 2
    }

    lateinit var decodeRunnable: ImageDecodeRunnable
    lateinit var downloadRunnable: ImageDownloadRunnable

    lateinit var byteBuffer: ByteArray get set

    var byteOffset: Int = 0
        get set
    lateinit var url: String
        get set

    init {
    }


    fun initializeTask(url: String) {
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

    fun taskFinish(state: Int) {
        imageManager.handleState(this, state)
    }

}