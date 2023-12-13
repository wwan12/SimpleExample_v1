package com.hq.tool.model.preview

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.view.Surface
import android.view.TextureView
import androidx.core.app.ActivityCompat

class Camera2Proxy2(val activity: Activity) {

    var previewSize:Size?=null
    var mCameraId:String=""
    private val cameraId = "0"
    private lateinit var cameraDevice: CameraDevice
    private val cameraThread = HandlerThread("CameraThread").apply { start() }
    private val cameraHandler = Handler(cameraThread.looper)
    private lateinit var cameraManager: CameraManager
    private lateinit var session: CameraCaptureSession


    fun start(textures: ArrayList<TextureView>,cameraIndex: Int,call:(ArrayList<Surface>)->Unit): Unit {
        var lost=textures.size
        for (t in textures ){
            t.surfaceTextureListener= object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(
                    surface: SurfaceTexture,
                    width: Int,
                    height: Int
                ) {
                    lost--
                    if (lost==0){
                        //首先就需要设置相机，然后再打开相机
                        setupCamera(cameraIndex,width, height)
                        openCamera(mCameraId,textures,call)
                    }
                }

                //下面的方法可以先不看，我们先实现相机预览
                override fun onSurfaceTextureSizeChanged(
                    surface: SurfaceTexture,
                    width: Int,
                    height: Int
                ) {
                }

                override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                    return false
                }

                override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
            }
        }
    }

    private fun setupCamera(cameraIndex:Int,width:Int, height: Int) {
        cameraManager = activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            val cameraId = cameraManager.cameraIdList[cameraIndex]
//            for (cameraId in cameraIdList) {
                val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
//                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
//                    continue
//                }
                val map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
//相机支持的所有分辨率，下一步就是获取最合适的分辨率
                val outputSizes = map!!.getOutputSizes(SurfaceTexture::class.java)
                val size = getOptimalSize(outputSizes, width, height);
                previewSize = size
                mCameraId = cameraId
//                break;
//            }
        } catch (e: CameraAccessException) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    private fun openCamera(cameraId: String,textures: ArrayList<TextureView>,call:(ArrayList<Surface>)->Unit) {
        cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                startPreview(textures,call)
            }

            override fun onDisconnected(camera: CameraDevice) {
                activity.finish()
            }

            override fun onError(camera: CameraDevice, error: Int) {

            }
        }, cameraHandler)
    }

    private fun startPreview(previews:ArrayList<TextureView>,call:(ArrayList<Surface>)->Unit) {
//设置图片阅读器
//        setImageReader();
//注意这里：sufacetexture跟surfaceview是两个东西，需要注意！
//sufacetexture是textureview的重要属性
        val targets = arrayListOf<Surface>()
        for (p in previews) {
            val surfaceTexture = p.surfaceTexture
            //设置textureview的缓存区大小
            surfaceTexture?.setDefaultBufferSize(
                previewSize!!.width,
                previewSize!!.height
            );
            val surface = Surface(surfaceTexture)
            targets.add(surface)
        }

//设置surface进行预览图像数据

//创建CaptureRequest
        val captureRequest = setCaptureRequest(targets)
//创建capturesession
/*Surface表示有多个输出流，我们有几个显示载体，就需要几个输出流。
对于拍照而言，有两个输出流：一个用于预览、一个用于拍照。
对于录制视频而言，有两个输出流：一个用于预览、一个用于录制视频。*/
// previewSurface 用于预览， mImageReader.getSurface() 用于拍照
        try {
            cameraDevice.createCaptureSession(
                targets,
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(captureSession: CameraCaptureSession) {//赋值session
//当回调创建成功就会调用这个回调
                        session = captureSession;
                        session.setRepeatingRequest(captureRequest.build(), null, cameraHandler)
                        call(targets)
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {

                    }
                },
                cameraHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace();
        }
    }
    private fun setCaptureRequest(surfaces:ArrayList<Surface>):CaptureRequest.Builder{
//        try {
          val  mPreviewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            for (s in surfaces){
                mPreviewRequestBuilder.addTarget(s)
            }
//手动对焦，还不会
/*
*/
// 自动对焦
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
// 闪光灯
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
// 人脸检测模式
            mPreviewRequestBuilder.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE, CameraCharacteristics.STATISTICS_FACE_DETECT_MODE_SIMPLE)
                return mPreviewRequestBuilder
//        } catch (e:CameraAccessException ) {
//            e.printStackTrace();
//        }
    }

    private fun closeCamera() {
        //首先要关闭session
        if (session != null) {
            session.close()
        }
        if (cameraDevice != null) {
            cameraDevice.close()
        }
    }

    private fun getOptimalSize(outputSizes: Array<Size>, width: Int, height: Int): Size {
        var tempSize = Size(width, height)
        val sizes = ArrayList<Size>()
        for (outputSize in outputSizes) {
            if (width > height) {//横屏的时候
                if (outputSize.height > height && outputSize.width > width) {
                    sizes.add(outputSize)
                }
            } else {
//竖屏的时候
                if (outputSize.width > height && outputSize.height > width) {
                    sizes.add(outputSize)
                }
            }
        }
        if (sizes.size > 0) {
            //如果有多个符合条件找到一个差距最小的，最接近预览分辨率的
            tempSize = sizes.get(0)
            var minnum = 999999
            for (size in sizes) {
                val num = size.height * size.height - width * height
                if (num < minnum) {
                    minnum = num
                    tempSize = size
                }
            }
        }
        return tempSize;
    }
}