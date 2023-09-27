package com.hq.tool.device;

import static com.hq.tool.device.CameraConfig.CAMERA_DIRECT;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import java.util.Arrays;


public class Camera2Proxy {

    private static final String TAG = "Camera2Proxy";

    private Activity mActivity;

    // camera
//    private int mCameraId = CameraCharacteristics.LENS_FACING_BACK; // 要打开的摄像头ID
    private Size mPreviewSize = new Size(480, 640); // 固定640*480演示
    private CameraDevice mCameraDevice; // 相机对象
    private CameraCaptureSession mCaptureSession;

    // handler
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    // output
    private Surface mPreviewSurface; // 输出到屏幕的预览
    private ImageReader mImageReader; // 预览回调的接收者
    private ImageReader.OnImageAvailableListener mOnImageAvailableListener;

    /**
     * 打开摄像头的回调
     */
    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            Log.d(TAG, "onOpened");
            mCameraDevice = camera;
            initPreviewRequest();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            Log.d(TAG, "onDisconnected");
            releaseCamera();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
          //  Log.e(TAG, "Camera Open failed, error: " + error);
            releaseCamera();
        }
    };

    @TargetApi(Build.VERSION_CODES.M)
    public Camera2Proxy(Activity activity) {
        mActivity = activity;

     //  int ac= mActivity.getWindowManager().getDefaultDisplay().getWidth()/mActivity.getWindowManager().getDefaultDisplay().getHeight();
      //  mPreviewSize=new Size(640,640);
    }

    @SuppressLint("MissingPermission")
    public void openCamera() {
        Log.v(TAG, "openCamera");
        startBackgroundThread(); // 对应 releaseCamera() 方法中的 stopBackgroundThread()
        try {
            CameraManager cameraManager = (CameraManager) mActivity.getSystemService(Context.CAMERA_SERVICE);
            mPreviewSize=  getPreviewSize(Integer.toString(CAMERA_DIRECT));
            Log.d(TAG, "preview size: " + mPreviewSize.getWidth() + "*" + mPreviewSize.getHeight());
            mImageReader = ImageReader.newInstance(mPreviewSize.getWidth(), mPreviewSize.getHeight(),
                    ImageFormat.YUV_420_888, 2);
            mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler);
            //先设置尺寸
            // 打开摄像头
            cameraManager.openCamera(Integer.toString(CAMERA_DIRECT), mStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void releaseCamera() {
        Log.v(TAG, "releaseCamera");
        try {
            mCaptureSession.stopRepeating();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mCaptureSession != null) {
            mCaptureSession.close();
            mCaptureSession = null;
        }
        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
        if (mImageReader != null) {
            mImageReader.close();
            mImageReader = null;
        }

        stopBackgroundThread(); // 对应 openCamera() 方法中的 startBackgroundThread()
    }

    public void setImageAvailableListener(ImageReader.OnImageAvailableListener onImageAvailableListener) {
        mOnImageAvailableListener = onImageAvailableListener;
    }

    public void setPreviewSurface(SurfaceTexture surfaceTexture) {
        // mPreviewSize必须先初始化完成
        surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        mPreviewSurface = new Surface(surfaceTexture);
    }

    private void initPreviewRequest() {
        try {
            final CaptureRequest.Builder builder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            // 添加输出到屏幕的surface
            builder.addTarget(mPreviewSurface);
            // 添加输出到ImageReader的surface。然后我们就可以从ImageReader中获取预览数据了
            builder.addTarget(mImageReader.getSurface());
            mCameraDevice.createCaptureSession(Arrays.asList(mPreviewSurface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            mCaptureSession = session;
                            // 设置连续自动对焦和自动曝光
                            builder.set(CaptureRequest.CONTROL_AF_MODE,
                                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                            builder.set(CaptureRequest.CONTROL_AE_MODE,
                                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                            CaptureRequest captureRequest = builder.build();
                            try {
                                // 一直发送预览请求
                                mCaptureSession.setRepeatingRequest(captureRequest, null, mBackgroundHandler);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                            Log.e(TAG, "ConfigureFailed. session: mCaptureSession");
                        }
                    }, mBackgroundHandler); // handle 传入 null 表示使用当前线程的 Looper
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public Size getPreviewSize() {
        return mPreviewSize;

    }

    public void  setPreviewSize(Size size){
        mPreviewSize=size;


    }


    public void switchCamera() {
        CAMERA_DIRECT = CAMERA_DIRECT == CameraCharacteristics.LENS_FACING_FRONT
                ? CameraCharacteristics.LENS_FACING_BACK
        : CameraCharacteristics.LENS_FACING_FRONT;
        Log.d(TAG, "switchCamera: mCameraId: " + CAMERA_DIRECT);
        releaseCamera();
        openCamera();
    }

    private void startBackgroundThread() {
        if (mBackgroundThread == null || mBackgroundHandler == null) {
            Log.v(TAG, "startBackgroundThread");
            mBackgroundThread = new HandlerThread("CameraBackground");
            mBackgroundThread.start();
            mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
        }
    }

    private void stopBackgroundThread() {
        Log.v(TAG, "stopBackgroundThread");
        if (mBackgroundThread != null) {
            mBackgroundThread.quitSafely();
        }
        try {
            if(mBackgroundThread != null){
                mBackgroundThread.join();
            }
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Size getPreviewSize(String mCameraId) {
        int bestPreviewWidth=0;
        int bestPreviewHeight=0;
        try {
            int diffs = Integer.MAX_VALUE;
            WindowManager windowManager =(WindowManager)  mActivity.getSystemService(Context.WINDOW_SERVICE);
            CameraManager mCameraManager=(CameraManager)  mActivity.getSystemService(Context.CAMERA_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point screenResolution = new Point(display.getWidth(), display.getHeight());

            CameraCharacteristics props = mCameraManager.getCameraCharacteristics(mCameraId);
            StreamConfigurationMap configurationMap = props.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            Size[] availablePreviewSizes = configurationMap.getOutputSizes(ImageFormat.YUV_420_888);

            for (Size previewSize : availablePreviewSizes) {

                int mCameraPreviewWidth = previewSize.getWidth();
                int mCameraPreviewHeight = previewSize.getHeight();
                int newDiffs = Math.abs(mCameraPreviewWidth - screenResolution.x) + Math.abs(mCameraPreviewHeight - screenResolution.y);
//                int newDiffs= Math.abs(mViewSize.getWidth()-mCameraPreviewWidth) + Math.abs(mViewSize.getHeight()-mCameraPreviewHeight);
//                Log.e(TAG,"sss"+ Math.abs(mViewSize.getWidth()-mCameraPreviewWidth)+"\t"+ Math.abs(mViewSize.getHeight()-mCameraPreviewHeight));
                if (newDiffs == 0) {
                    bestPreviewWidth = mCameraPreviewWidth;
                    bestPreviewHeight = mCameraPreviewHeight;
                    break;
                }
                if (diffs > newDiffs) {
                    bestPreviewWidth = mCameraPreviewWidth;
                    bestPreviewHeight = mCameraPreviewHeight;
                    diffs = newDiffs;
                }
            }
            Log.e(TAG, "bestPreviewHeight = " + bestPreviewHeight+"\n"+"bestPreviewWidth = " + bestPreviewWidth);
        } catch (CameraAccessException cae) {
            return new Size(480,640);
        }
        return   new Size(bestPreviewHeight,bestPreviewWidth);
    }
}
