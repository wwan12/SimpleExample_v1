package com.aisino.tool.system

import android.content.Context
import android.media.MediaPlayer
import android.util.Log

/**
 * createTime：2020/4/12 3:58 PM
 * author
 * descrip：mp3播放
 */
class MediaPlayerUtil {
    companion object{
       private var mMediaPlayer: MediaPlayer? = null
        val mediaPlayer: MediaPlayer?
            get() {
                if (null == mMediaPlayer) {
                    mMediaPlayer = MediaPlayer()
                    mMediaPlayer?.setOnCompletionListener {
                        mMediaPlayer?.release()
                        mMediaPlayer = null
                    }
                }
                return mMediaPlayer
            }
    }

    /**
     * 播放音频
     */
    fun playAudioByAssets(mContext: Context, fileName: String?) {
        try {
            if (null != mMediaPlayer) {
                if (mMediaPlayer!!.isPlaying) {
                    return
                }
            }
            val assetManager = mContext.assets
            val afd = assetManager.openFd(fileName)
            val mediaPlayer = mediaPlayer
            if (afd == null) {
                return
            }
            mediaPlayer!!.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            mediaPlayer.isLooping = false //循环播放
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            Log.e("播放音频失败", "")
        }
    }
}