package ru.markn.engine.audio

import android.media.MediaDataSource

internal class MediaDataSource(
    private val dataSource: ByteArray
) : MediaDataSource() {

    override fun readAt(position: Long, buffer: ByteArray, offset: Int, size: Int): Int = size.also {
        System.arraycopy(dataSource, position.toInt(), buffer, offset, size)
    }

    override fun getSize(): Long = dataSource.size.toLong()

    override fun close() {}
}