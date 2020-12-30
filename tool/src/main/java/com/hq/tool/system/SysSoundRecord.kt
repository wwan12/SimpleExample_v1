package com.hq.tool.system

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.provider.MediaStore
import java.io.File; 
import java.io.FileInputStream; 
import java.io.FileOutputStream;
import android.media.AudioFormat; 
import android.media.AudioRecord;
import android.media.MediaRecorder
import android.os.Environment


/**
 * 文件描述：
 * 作者：Administrator
 * 创建时间：2018/9/13/013
 * 更改时间：2018/9/13/013
 * 版本号：1
 *
 */

class AudioRecordFunc(val context: Context) {
    // 缓冲区字节大小
    private var bufferSizeInBytes = 0
    //AudioName裸音频数据文件 ，麦克风
    private var AudioName = ""
    //NewAudioName可播放的音频文件
    private var NewAudioName = ""
    private var audioRecord: AudioRecord? = null
    private var isRecord = false// 设置正在录制的状态
    //音频输入-麦克风
    private val AUDIO_INPUT = MediaRecorder.AudioSource.MIC
    //采用频率
    //44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    private val AUDIO_SAMPLE_RATE = 44100L  //44.1KHz,普遍使用的频率


    fun startRecordAndFile(): Unit {
            if (audioRecord == null)
                creatAudioRecord()
            audioRecord?.startRecording()
            // 让录制状态为true
            isRecord = true
            // 开启音频文件写入线程
            Thread(object : Runnable{
                override fun run() {
                    writeDateTOFile()//往文件中写入裸数据
                    copyWaveFile(AudioName, NewAudioName)//给裸数据加上头文件
                }
            }).start()
    }

    fun stopRecordAndFile() :String{
            isRecord = false//停止文件写入
            audioRecord?.stop()
            audioRecord?.release()//释放资源
            audioRecord = null
        return NewAudioName
    }

    private fun creatAudioRecord() {
        // 获取音频文件路径
        AudioName = context.filesDir.path+DateAndTime.nowDateTime+".raw"
        NewAudioName = context.filesDir.path+DateAndTime.nowDateTime+".wav"
        // 获得缓冲区字节大小
        bufferSizeInBytes = AudioRecord.getMinBufferSize(AUDIO_SAMPLE_RATE.toInt(),
                AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT)
        // 创建AudioRecord对象
        audioRecord = AudioRecord(AUDIO_INPUT, AUDIO_SAMPLE_RATE.toInt(),
                AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, bufferSizeInBytes)
    }

    /**
     * 这里将数据写入文件，但是并不能播放，因为AudioRecord获得的音频是原始的裸音频，
     * 如果需要播放就必须加入一些格式或者编码的头信息。但是这样的好处就是你可以对音频的 裸数据进行处理，比如你要做一个爱说话的TOM
     * 猫在这里就进行音频的处理，然后重新封装 所以说这样得到的音频比较容易做一些音频的处理。
     */
    private fun writeDateTOFile() {
        // new一个byte数组用来存一些字节数据，大小为缓冲区大小
        val audiodata = ByteArray(bufferSizeInBytes)
        var fos: FileOutputStream?
        var readsize =0
        val file = File(AudioName)
        if (file.exists()) {
                file.delete()
        }
        fos = FileOutputStream(file)// 建立一个可存取字节的文件
        while (isRecord == true) {
            readsize = audioRecord!!.read(audiodata, 0, bufferSizeInBytes)
            if (AudioRecord.ERROR_INVALID_OPERATION != readsize) {
                    fos.write(audiodata)
            }
        }
            fos.close()// 关闭写入流
    }

    // 这里得到可播放的音频文件
    private fun copyWaveFile(inFilename: String, outFilename: String) {
        var ins: FileInputStream?
        var out: FileOutputStream?
        var totalAudioLen: Long = 0
        var totalDataLen = totalAudioLen + 36
        val longSampleRate = AUDIO_SAMPLE_RATE
        val channels = 2
        val byteRate = 16 * AUDIO_SAMPLE_RATE * channels / 8
        val data = ByteArray(bufferSizeInBytes)
        ins = FileInputStream(inFilename)
            out = FileOutputStream(outFilename)
            totalAudioLen = ins.channel.size()
            totalDataLen = totalAudioLen + 36
            WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
                    longSampleRate, channels, byteRate)
            while (ins.read(data) != -1) {
                out.write(data)
            }
        ins.close()
            out.close()
    }

    /**
     * 这里提供一个头信息。插入这些信息就可以得到可以播放的文件。
     */
    private fun WriteWaveFileHeader(out: FileOutputStream, totalAudioLen: Long,
                                    totalDataLen: Long, longSampleRate: Long, channels: Int, byteRate: Long) {
        val header = ByteArray(44)
        header[0] = 'R'.toByte() // RIFF/WAVE header
        header[1] = 'I'.toByte()
        header[2] = 'F'.toByte()
        header[3] = 'F'.toByte()
        header[4] = (totalDataLen and 0xff).toByte()
        header[5] = (totalDataLen shr 8 and 0xff).toByte()
        header[6] = (totalDataLen shr 16 and 0xff).toByte()
        header[7] = (totalDataLen shr 24 and 0xff).toByte()
        header[8] = 'W'.toByte()
        header[9] = 'A'.toByte()
        header[10] = 'V'.toByte()
        header[11] = 'E'.toByte()
        header[12] = 'f'.toByte() // 'fmt ' chunk
        header[13] = 'm'.toByte()
        header[14] = 't'.toByte()
        header[15] = ' '.toByte()
        header[16] = 16 // 4 bytes: size of 'fmt ' chunk
        header[17] = 0
        header[18] = 0
        header[19] = 0
        header[20] = 1 // format = 1
        header[21] = 0
        header[22] = channels.toByte()
        header[23] = 0
        header[24] = (longSampleRate and 0xff).toByte()
        header[25] = (longSampleRate shr 8 and 0xff).toByte()
        header[26] = (longSampleRate shr 16 and 0xff).toByte()
        header[27] = (longSampleRate shr 24 and 0xff).toByte()
        header[28] = (byteRate and 0xff).toByte()
        header[29] = (byteRate shr 8 and 0xff).toByte()
        header[30] = (byteRate shr 16 and 0xff).toByte()
        header[31] = (byteRate shr 24 and 0xff).toByte()
        header[32] = (2 * 16 / 8).toByte() // block align
        header[33] = 0
        header[34] = 16 // bits per sample
        header[35] = 0
        header[36] = 'd'.toByte()
        header[37] = 'a'.toByte()
        header[38] = 't'.toByte()
        header[39] = 'a'.toByte()
        header[40] = (totalAudioLen and 0xff).toByte()
        header[41] = (totalAudioLen shr 8 and 0xff).toByte()
        header[42] = (totalAudioLen shr 16 and 0xff).toByte()
        header[43] = (totalAudioLen shr 24 and 0xff).toByte()
        out.write(header, 0, 44)
    }
}


val SOUND_REQUEST = 3000

fun Activity.openRecordSound(): Unit {
    val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
    startActivityForResult(intent, SOUND_REQUEST)
}

    fun createAudio(path: String): MediaPlayer {
        val mMediaPlayer=MediaPlayer()
        mMediaPlayer.setDataSource(path)
        mMediaPlayer.prepareAsync()
        mMediaPlayer.start()
        return mMediaPlayer
    }
